package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelateDTO {
    // 用户id
    private Integer userId;
    // 岗位id
    private Integer positionId;
    // 指数
    private Integer index;
}
