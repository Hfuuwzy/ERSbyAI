package com.example.entity;

import lombok.Data;

@Data
public class Submit {
    private Integer id;
    private Integer employId;
    private Integer positionId;
    private Integer userId;
    private Integer resumeId;
    private String time;
    private String status;

    private String employName;
    private String positionName;
    private String userName;
    private Integer aiScore;
    private String aiReview;

}