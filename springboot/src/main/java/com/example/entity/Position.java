package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class Position {
    private Integer id;
    private String name;
    private Integer employId;
    private Integer industryId;
    private String type;
    private String experience;
    private String salary;
    private String education;
    private String tags;
    private String content;
    private String status;
    //关联查询保存的数据
    private String employName; //企业名称
    private String employAvatar; //企业头像
    private String employStage; //企业阶段
    private String employScale; //企业规模
    private String employCity; //企业规模
    private String employAddress; //企业规模
    private String industryName;//行业名称
    private List<String> tagList; //标签
    private Boolean aiGenerate;//是否需要ai生成描述

}
