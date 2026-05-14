package com.example.entity;

import lombok.Data;

@Data
public class Advertise {
    private Integer id;
    private String img;
    private Integer positionId;
    private String location;
    // 关联查询
    private String positionName;
    private String positionSalary;
    private String positionEducation;
    private String employName;

}
