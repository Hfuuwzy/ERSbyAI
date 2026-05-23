package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.BatchUpdateForm;
import com.example.entity.Position;
import com.example.entity.Resume;
import com.example.entity.Submit;
import com.example.exception.CustomException;
import com.example.mapper.PositionMapper;
import com.example.mapper.ResumeMapper;
import com.example.mapper.SubmitMapper;
import com.example.utils.AiUtil;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmitService {

    private static final Log log = LogFactory.get();

    @Resource
    private SubmitMapper submitMapper;
    @Resource
    private ResumeMapper resumeMapper;
    @Resource
    private PositionMapper positionMapper;
    @Resource
    private AiUtil aiUtil;

    public void add(Submit submit) {
        submit.setTime(DateUtil.now());
        submit.setStatus("已投递");
        List<Submit> list = submitMapper.selectByUserIdAndPositionId(submit.getUserId(), submit.getPositionId());
        if (CollectionUtil.isNotEmpty(list)) {
            Submit existing = list.get(0);
            throw new CustomException("500", "您已投递该职位，当前状态：" + existing.getStatus());
        }
        //进行ai打分和审核
        Resume resume = resumeMapper.selectById(submit.getResumeId());
        Position position = positionMapper.selectById(submit.getPositionId());
        try {
            List<String> scoreMessageList = buildScoreMessages(resume, position);
            String aiScore = aiUtil.ai(scoreMessageList);
            submit.setAiScore(Integer.parseInt(aiScore.trim()));
            List<String> reviewMessageList = buildReviewMessages(resume, position);
            String aiReview = aiUtil.ai(reviewMessageList);
            submit.setAiReview(aiReview.trim());
            submit.setStatus("不合格".equals(submit.getAiReview())?"不适合":"通过");
        } catch (RuntimeException e) {
            log.warn("AI简历审核失败，转为人工审核：{}", e.getMessage());
            submit.setAiReview("AI审核失败，待人工审核");
            submit.setStatus("已投递");
        }
        submitMapper.insert(submit);
    }

    private List<String> buildScoreMessages(Resume resume, Position position) {
        List<String> messages = new ArrayList<>();
        messages.add("你是招聘平台的简历匹配评分助手。请基于候选人简历和岗位要求进行匹配评分，评分范围为0-100。匹配维度包括学历要求、工作年限、技能标签、项目经验和岗位职责相关性。只返回一个整数，不要返回解释、单位或其他文本。");
        messages.add(buildResumePositionContext(resume, position));
        return messages;
    }

    private List<String> buildReviewMessages(Resume resume, Position position) {
        List<String> messages = new ArrayList<>();
        messages.add("你是招聘平台的简历初筛助手。请基于候选人简历和岗位要求判断是否进入下一轮。只返回其中一个词：合格 或 不合格。不要返回原因、标点或其他文本。");
        messages.add(buildResumePositionContext(resume, position));
        return messages;
    }

    private String buildResumePositionContext(Resume resume, Position position) {
        return "候选人简历：" + resume + "\n岗位要求：" + position;
    }

    public void updateById(Submit submit) {
        submitMapper.updateById(submit);
    }

    public void deleteById(Integer id) {
        submitMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            submitMapper.deleteById(id);
        }
    }

    public Submit selectById(Integer id) {
        return submitMapper.selectById(id);
    }

    public List<Submit> selectAll(Submit submit) {
        return submitMapper.selectAll(submit);
    }

    public PageInfo<Submit> selectPage(Submit submit, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.USER.name().equals(currentUser.getRole())) {
            submit.setUserId(currentUser.getId());
        }
        if (currentUser != null && RoleEnum.EMPLOY.name().equals(currentUser.getRole())) {
            submit.setEmployId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Submit> list = submitMapper.selectAll(submit);
        return PageInfo.of(list);
    }

    public void batchUpdate(BatchUpdateForm form) {
        if (CollectionUtil.isEmpty(form.getIds()) || form.getStatus() == null) {
            return;
        }
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null || !RoleEnum.EMPLOY.name().equals(currentUser.getRole())) {
            return;
        }
        submitMapper.updateBatch(form.getIds(), form.getStatus(), currentUser.getId());
    }

}
