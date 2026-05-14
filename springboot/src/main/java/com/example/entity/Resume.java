package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class Resume {
    // 数据库字段
    private Integer id;
    private String name; // 简历名称
    private String username;
    private String sex;
    private String salary;
    private String education;
    private String experience;
    private String phone;
    private String email;
    private String eduExps; //list
    private String workExps;
    private String proExps;
    private Integer userId;

    //结构化数据
    private List<EduExp> eduExpList; // 接受前端数据，再转为String
    private List<WorkExp> workExpList;
    private List<ProExp> proExpList;

    private String userAvatar;
}

