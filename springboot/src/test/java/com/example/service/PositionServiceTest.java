package com.example.service;

import com.example.entity.Collect;
import com.example.entity.Position;
import com.example.entity.Submit;
import com.example.entity.User;
import com.example.mapper.CollectMapper;
import com.example.mapper.PositionMapper;
import com.example.mapper.SubmitMapper;
import com.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionMapper positionMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CollectMapper collectMapper;
    @Mock
    private SubmitMapper submitMapper;

    @InjectMocks
    private PositionService positionService;

    @Test
    void recommendV2HybridMergesWeightedStrategiesAndRemovesDuplicates() {
        List<Position> positions = List.of(
                position(1, "Java后端工程师", "Java,Spring Boot,MySQL", 1, "全职", "本科", "3年"),
                position(2, "数据库工程师", "MySQL,Redis", 1, "全职", "本科", "3年"),
                position(10, "Go后端工程师", "Go,微服务", 2, "全职", "本科", "3年"),
                position(20, "Java平台工程师", "Java,Spring Boot,微服务", 1, "全职", "本科", "3年"),
                position(30, "市场运营", "运营,活动", 3, "全职", "大专", "1年"));
        stubRecommendationData(positions,
                List.of(user(1), user(2)),
                List.of(collect(1, 1), collect(2, 1), collect(2, 10)),
                List.of(submit(1, 2), submit(2, 2)));

        List<Position> result = positionService.recommendV2(1, "hybrid");

        List<Integer> ids = ids(result);
        assertThat(ids).doesNotHaveDuplicates();
        assertThat(ids).contains(10, 20);
        assertThat(ids.get(0)).isEqualTo(10);
        assertThat(result).hasSizeLessThanOrEqualTo(10);
        assertThat(result.get(0).getTagList()).contains("Go", "微服务");
    }

    @Test
    void recommendV2ContentUsesInteractedPositionProfileAndExcludesSeenPositions() {
        List<Position> positions = List.of(
                position(1, "Java后端工程师", "Java,Spring Boot,MySQL", 1, "全职", "本科", "3年"),
                position(2, "Java平台工程师", "Java,Spring Boot,微服务", 1, "全职", "本科", "3年"),
                position(3, "客服专员", "客服,售后", 4, "全职", "大专", "不限"));
        stubRecommendationData(positions,
                List.of(user(1)),
                List.of(collect(1, 1)),
                List.of());

        List<Position> result = positionService.recommendV2(1, "content");

        assertThat(ids(result)).startsWith(2).doesNotContain(1);
    }

    @Test
    void recommendV2FallsBackToRandomTopTenWhenStrategyHasNoRecommendations() {
        List<Position> positions = IntStream.rangeClosed(1, 12)
                .mapToObj(id -> position(id, "岗位" + id, "标签" + id, id, "全职", "本科", "不限"))
                .collect(Collectors.toList());
        stubRecommendationData(positions, List.of(), List.of(), List.of());

        List<Position> result = positionService.recommendV2(99, "usercf");

        assertThat(result).hasSize(10);
        assertThat(ids(result)).allMatch(id -> id >= 1 && id <= 12);
    }

    private void stubRecommendationData(List<Position> positions, List<User> users, List<Collect> collects, List<Submit> submits) {
        when(positionMapper.selectAll(any(Position.class))).thenReturn(positions);
        when(userMapper.selectAll(any(User.class))).thenReturn(users);
        when(collectMapper.selectAll(any(Collect.class))).thenReturn(collects);
        when(submitMapper.selectAll(any(Submit.class))).thenReturn(submits);
    }

    private Position position(Integer id, String name, String tags, Integer industryId, String type, String education, String experience) {
        Position position = new Position();
        position.setId(id);
        position.setName(name);
        position.setTags(tags);
        position.setIndustryId(industryId);
        position.setType(type);
        position.setEducation(education);
        position.setExperience(experience);
        return position;
    }

    private User user(Integer id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Collect collect(Integer userId, Integer positionId) {
        Collect collect = new Collect();
        collect.setStudentId(userId);
        collect.setPositionId(positionId);
        return collect;
    }

    private Submit submit(Integer userId, Integer positionId) {
        Submit submit = new Submit();
        submit.setUserId(userId);
        submit.setPositionId(positionId);
        return submit;
    }

    private List<Integer> ids(List<Position> positions) {
        return positions.stream().map(Position::getId).collect(Collectors.toList());
    }
}
