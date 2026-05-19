package com.example.mapper;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class PositionMapperXmlTest {

    @Test
    void selectAllIncludesSearchFiltersAndEmploySummaryFields() throws IOException {
        String xml = new String(
                getClass().getClassLoader().getResourceAsStream("mapper/PositionMapper.xml").readAllBytes(),
                StandardCharsets.UTF_8);

        assertThat(xml).contains("employ.city as employCity");
        assertThat(xml).contains("employ.scale as employScale");
        assertThat(xml).contains("employ.city = #{employCity}");
        assertThat(xml).contains("position.salary = #{salary}");
        assertThat(xml).contains("position.experience = #{experience}");
        assertThat(xml).contains("position.education = #{education}");
    }

    @Test
    void selectAllSupportsSafeSortOptions() throws IOException {
        String xml = new String(
                getClass().getClassLoader().getResourceAsStream("mapper/PositionMapper.xml").readAllBytes(),
                StandardCharsets.UTF_8);

        assertThat(xml).contains("sort == 'salary_desc'");
        assertThat(xml).contains("sort == 'salary_asc'");
        assertThat(xml).contains("regexp_substr(position.salary, '[0-9]+'");
        assertThat(xml).contains("position.id desc");
    }
}
