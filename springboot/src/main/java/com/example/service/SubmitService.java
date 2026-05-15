package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmitService {

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
            throw new CustomException("-1", "您已经投递过该岗位，请勿重复投递！");
        }
        //进行ai打分和审核
        Resume resume = resumeMapper.selectById(submit.getResumeId());
        Position position = positionMapper.selectById(submit.getPositionId());
        List<String> scoreMessageList = new ArrayList<>();
        scoreMessageList.add("我会给你一份简历信息和一份岗位信息，你帮我打个分，分数在0-100之间，你只需要返回分数即可，其他任何话都不要说，只返回分数的数字");
        scoreMessageList.add("简历信息为："+resume.toString()+",岗位信息为："+position.toString());
        String aiScore = aiUtil.ai(scoreMessageList);
        submit.setAiScore(Integer.parseInt(aiScore.trim()));
        List<String> reviewMessageList = new ArrayList<>();
        reviewMessageList.add("我会给你一份简历信息和一份岗位信息，你帮我审核一下，你认为合不合格，合格你就返回合格，不合格你就返回不合格就行，要求可以不用特别严格，其他任何话都不要说");
        reviewMessageList.add("简历信息为："+resume.toString()+",岗位信息为："+position.toString());
        String aiReview = aiUtil.ai(reviewMessageList);
        submit.setAiReview(aiReview.trim());
        submit.setStatus("不合格".equals(submit.getAiReview())?"不适合":"通过");
        submitMapper.insert(submit);
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

}
