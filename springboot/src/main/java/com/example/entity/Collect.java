package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class Collect {
    // 数据库字段
    private Integer id;
    private Integer studentId;
    private Integer positionId;

    // 关联查询字段
    private String positionName;
    private String positionSalary;
    private List<String> tagList;
    private String employAvatar;
    private String employName;
    private String industryName;
    private String employStage;

}
