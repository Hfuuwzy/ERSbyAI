package com.example.service;

import com.example.entity.Position;
import com.example.entity.Resume;
import com.example.entity.Submit;
import com.example.mapper.PositionMapper;
import com.example.mapper.ResumeMapper;
import com.example.mapper.SubmitMapper;
import com.example.utils.AiUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmitServiceTest {

    @Mock
    private SubmitMapper submitMapper;
    @Mock
    private ResumeMapper resumeMapper;
    @Mock
    private PositionMapper positionMapper;
    @Mock
    private AiUtil aiUtil;

    @InjectMocks
    private SubmitService submitService;

    @Test
    void addUsesStructuredResumeMatchingPromptsForScoreAndReview() {
        Submit submit = new Submit();
        submit.setUserId(10);
        submit.setPositionId(20);
        submit.setResumeId(30);
        when(submitMapper.selectByUserIdAndPositionId(10, 20)).thenReturn(List.of());
        when(resumeMapper.selectById(30)).thenReturn(resume());
        when(positionMapper.selectById(20)).thenReturn(position());
        when(aiUtil.ai(anyList())).thenReturn("87", "合格");

        submitService.add(submit);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<String>> prompts = ArgumentCaptor.forClass(List.class);
        verify(aiUtil, times(2)).ai(prompts.capture());
        List<List<String>> allPrompts = prompts.getAllValues();
        assertThat(allPrompts.get(0).get(0)).contains("0-100", "只返回一个整数", "匹配维度");
        assertThat(allPrompts.get(1).get(0)).contains("合格", "不合格", "只返回其中一个词");
        assertThat(allPrompts.get(0).get(1)).contains("候选人简历", "岗位要求");
    }

    @Test
    void addStillSubmitsForManualReviewWhenAiServiceFails() {
        Submit submit = new Submit();
        submit.setUserId(10);
        submit.setPositionId(20);
        submit.setResumeId(30);
        when(submitMapper.selectByUserIdAndPositionId(10, 20)).thenReturn(List.of());
        when(resumeMapper.selectById(30)).thenReturn(resume());
        when(positionMapper.selectById(20)).thenReturn(position());
        when(aiUtil.ai(anyList())).thenThrow(new RuntimeException("AI API 请求失败: HTTP 401 - 无效的令牌"));

        submitService.add(submit);

        ArgumentCaptor<Submit> captor = ArgumentCaptor.forClass(Submit.class);
        verify(submitMapper).insert(captor.capture());
        Submit inserted = captor.getValue();
        assertThat(inserted.getStatus()).isEqualTo("已投递");
        assertThat(inserted.getAiScore()).isNull();
        assertThat(inserted.getAiReview()).isEqualTo("AI审核失败，待人工审核");
    }

    private Resume resume() {
        Resume resume = new Resume();
        resume.setName("Java后端开发简历");
        resume.setUsername("张三");
        resume.setEducation("本科");
        resume.setExperience("3年");
        resume.setWorkExps("Spring Boot、MySQL、Redis项目经验");
        resume.setProExps("招聘系统、订单系统");
        return resume;
    }

    private Position position() {
        Position position = new Position();
        position.setName("Java后端工程师");
        position.setEducation("本科");
        position.setExperience("3年以上");
        position.setTags("Java,Spring Boot,MySQL");
        position.setContent("负责后端接口开发和数据库设计");
        return position;
    }
}
