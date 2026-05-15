package com.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Employ extends Account{
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String city;
    private String address;
    private Integer industryId;
    private String scale;
    private String stage;
    private String status;
    // 关联id
    private String industryName;
}
