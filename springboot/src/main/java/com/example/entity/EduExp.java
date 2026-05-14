package com.example.entity;

import lombok.Data;

// 接受前端参数的结构化数据
@Data
public class EduExp {
    private String id;
    private String school;
    private String speciality;
    private String education;
    private String start;
    private String end;
    private String course;
}
