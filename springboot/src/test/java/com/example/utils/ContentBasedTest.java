package com.example.utils;

import com.example.entity.Position;
import com.example.entity.Resume;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentBasedTest {

    @Test
    void ranksPositionsByResumeKeywordsAndWeightedFields() {
        Resume resume = new Resume();
        resume.setEducation("本科");
        resume.setExperience("3-5年");
        resume.setSalary("15-20K");
        resume.setWorkExps("Java 后端开发 Spring Boot MySQL Redis 推荐系统");
        resume.setProExps("智能招聘推荐算法 内容推荐 协同过滤");

        Position designer = position(2, "视觉设计师", "Figma,品牌设计", "视觉设计和品牌物料", "本科", "3-5年", "15-20K");
        Position backend = position(1, "Java后端开发工程师", "Java,Spring Boot,MySQL,Redis", "负责智能招聘推荐系统和后端接口开发", "本科", "3-5年", "15-20K");
        Position tester = position(3, "软件测试工程师", "测试,自动化", "负责接口测试和质量保障", "本科", "1-3年", "10-15K");

        List<Integer> recommendations = ContentBased.recommend(7, List.of(designer, backend, tester), resume, List.of("后端开发", "Java职位"));

        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.get(0)).isEqualTo(1);
    }

    @Test
    void usesHistoryKeywordsWhenResumeTextIsSparse() {
        Resume resume = new Resume();
        resume.setEducation("本科");

        Position product = position(10, "产品经理", "需求分析,原型", "负责产品规划和用户调研", "本科", "3-5年", "15-20K");
        Position ai = position(11, "AI算法工程师", "机器学习,推荐算法", "负责内容推荐和机器学习模型", "本科", "3-5年", "20-30K");

        List<Integer> recommendations = ContentBased.recommend(7, List.of(product, ai), resume, List.of("机器学习", "推荐算法", "AI"));

        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.get(0)).isEqualTo(11);
    }

    @Test
    void returnsEmptyForMissingInputsAndNullResumeFeatures() {
        assertThat(ContentBased.recommend(7, null, new Resume(), List.of("Java"))).isEmpty();
        assertThat(ContentBased.recommend(7, List.of(position(1, "Java开发", "Java", "后端", "本科", "3-5年", "15-20K")), null, List.of("Java"))).isEmpty();
        assertThat(ContentBased.recommend(7, List.of(position(1, "Java开发", "Java", "后端", "本科", "3-5年", "15-20K")), new Resume(), List.of())).isEmpty();
    }

    private Position position(Integer id, String name, String tags, String content, String education, String experience, String salary) {
        Position position = new Position();
        position.setId(id);
        position.setName(name);
        position.setTags(tags);
        position.setContent(content);
        position.setEducation(education);
        position.setExperience(experience);
        position.setSalary(salary);
        position.setIndustryName("互联网");
        position.setStatus("审核通过");
        return position;
    }
}
