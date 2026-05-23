package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class BatchUpdateForm {

    private List<Integer> ids;

    private String status;
}
