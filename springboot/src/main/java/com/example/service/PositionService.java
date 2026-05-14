package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.*;
import com.example.utils.AiUtil;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 业务层方法
 */
@Service
public class PositionService {

    @Resource
    private PositionMapper positionMapper;
    @Resource
    private EmployMapper employMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private SubmitMapper submitMapper;
    @Resource
    private ResumeMapper resumeMapper;

    public void add(Position position) {
        Account currentUser = TokenUtils.getCurrentUser();
        Employ employ = employMapper.selectById(currentUser.getId());
        if (!"审核通过".equals(employ.getStatus())){
            throw new CustomException("500","您的资质未审核通过，不允许添加新的岗位");
        }
        position.setEmployId(employ.getId());
        position.setIndustryId(employ.getIndustryId());
        position.setStatus("待审核");
        //判断是否需要ai生成职位描述
        if(position.getAiGenerate()){
            List<String> messageList = new ArrayList<>();
            messageList.add("我给你一些岗位信息，你帮我生成一份职位描述，其他任何内容都不要回复，格式是百度富文本格式，类似：<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">华为云数据库相关岗位，此岗位为OD岗位，入职后满足条件可转华为。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>【岗位职责】<br>1. 负责华为云数据库的测试设计、测试验证与交付工作,保证产品高质量发布。<br>2. 负责华为云数据库的测试架构设计和开发,支撑自动化测试,提升测试质量和效率。<br>3. 支撑负责华为云数据库的性能/过载测试、可靠性/混沌测试、安全测试、客户化测试等。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>【技能要求】<br>1. 熟悉测试基本的技术和方法,包括测试设计、测试执行、测试分析、测试工具、测试仿真、故障注入、自动化等。<br>2. 熟练掌握测试需求分析和设计方法,熟悉黑白盒测试流程,熟悉常见缺陷管理工具。<br>3. 熟悉自动化脚本的开发,熟练使用常用的自动化测试框架,具备独立的问题定位分析能力。<br>4. 有强烈的责任心和使命感,具备良好的沟通能力和团队合作意识。<br>5. 至少熟悉一种编程语言C、C++、java、Python、Go、shell等。</span></p>");
            messageList.add("岗位信息："+position.toString());
            position.setContent(AiUtil.ai(messageList).trim());
        }
        positionMapper.insert(position);
    }

    public void updateById(Position position) {

        positionMapper.updateById(position);
    }

    public void deleteById(Integer id) {
        positionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            positionMapper.deleteById(id);
        }
    }

    public Position selectById(Integer id) {
        Position position = positionMapper.selectById(id);
        String tags = position.getTags();
        if (ObjectUtil.isNotEmpty(tags)) {
            String[] split = tags.split(",");
            position.setTagList(Arrays.asList(split));
        }
        return position;
    }

    public List<Position> selectAll(Position position) {
        List<Position> positions = positionMapper.selectAll(position);
        extracted(positions);
        return positions;
    }

    private static void extracted(List<Position> positions) {
        for (Position dbPositions : positions) {
            String tags = dbPositions.getTags();
            if (ObjectUtil.isNotEmpty(tags)) {
                String[] split = tags.split(",");
                List<String> list = Arrays.asList(split);
                if (list.size() > 3) {
                    dbPositions.setTagList(list.subList(0, 3));
                } else {
                    dbPositions.setTagList(list);
                }
            }
        }
    }

    public PageInfo<Position> selectPage(Position position, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.EMPLOY.name().equals(currentUser.getRole())) {
            position.setEmployId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Position> list = positionMapper.selectAll(position);
        return PageInfo.of(list);
    }


    /**
     * 推荐算法,基于协同过滤推荐算法(基于用户行为推荐)
     */
    public List<Position> recommend() {
//
//        extracted(positions);
//        return positions;
        // 获取当前用户
        Account currentUser = TokenUtils.getCurrentUser();
        // 1.获取所有的用户信息
        List<User> users = userMapper.selectAll(new User());
        // 2.获取所有的岗位信息
        List<Position> positions = positionMapper.selectAll(new Position());
        // 3.获取所有的收藏信息
        List<Collect> collects = collectMapper.selectAll(new Collect());
        // 4.获取所有的投递信息
        List<Submit> submits = submitMapper.selectAll(new Submit());
        // 存储所有的用户和所有的岗位之间的相关性指数的数据
        ArrayList<RelateDTO> data = new ArrayList<>();
        // 开始计算所有用户和所有岗位之间的相关性指数的数据
        for (Position position : positions) {
            Integer positionId = position.getId();
            for (User user : users) {
                Integer userId = user.getId();
                int index = 1; // 默认相关指数设置为1
                // 如果用户收藏过该岗位，就给出权重 + 1
                List<Collect> collectList = collects.stream().filter(x -> x.getPositionId().equals(positionId) && x.getStudentId().equals(userId)).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collectList)) {
                    index += 1;
                }
                // 如果用户投递过该岗位，就给处权重 + 2
                List<Submit> submitList = submits.stream().filter(x -> x.getPositionId().equals(positionId) && x.getUserId().equals(userId)).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(submitList)) {
                    index += 2;
                }
                if (index > 1) {
                    RelateDTO relateDTO = new RelateDTO(userId, positionId, index);
                    data.add(relateDTO);
                }
            }
        }
        // 调用基于用户行为的UserCF的推荐方法获取到被推荐岗位的id的list
        List<Integer> positionIds = UserCF.recommend(currentUser.getId(), data);
        // 把list对应的岗位id变成岗位信息
        List<Position> result = positions.stream().filter(x -> positionIds.contains(x.getId())).collect(Collectors.toList());

        // 如果推荐结果是空的，随机推荐3个
        if (CollectionUtil.isEmpty(result)) {
            Collections.shuffle(positions); // 顺序打乱
            if (positions.size() > 3) {
                result = positions.subList(0, 3);
            }else {
                result = positions;
            }
        }
        // 如果推荐结果不够3个，不写了
        extracted(result);
        return result;
    }

    /**
     * ai推荐求职者
     * @param positionId
     * @return
     */
    public Result getReferrers(Integer positionId) {
        Position position = positionMapper.selectById(positionId);
        //查询所有简历
        List<Resume> list = resumeMapper.selectAll(new Resume());
        List<String> messageList = new ArrayList<>();
        messageList.add("我会给你一份岗位信息，和多份的简历信息，你帮我从中推荐几份比较合适这个岗位的简历，你可以根据简历中的教育经历，项目经历以及实习经历来辅助判断，返回简历id就行，多个之间用,隔开，其他任何内容都不要返回，就返回id即可");
        StringBuilder sb = new StringBuilder();
        sb.append("岗位信息："+position.toString()+"简历信息：");
        list.stream().forEach(t ->{
            sb.append(t.toString()+",");
        });
        messageList.add(sb.toString());
        String ids = AiUtil.ai(messageList).trim();
        if(StrUtil.isBlank(ids)){
            return Result.success();
        }else{
            String[] split = ids.split(",");
            List<Resume> referrers = new ArrayList<>();
            for (int i=0;i<split.length;i++){
                Integer index = i;
                Optional<Resume> any = list.stream().filter(t -> t.getId().toString().equals(split[index])).findAny();
                if(any.isPresent()){
                    referrers.add(any.get());
                }
            }
            return Result.success(referrers);
        }
    }
}
