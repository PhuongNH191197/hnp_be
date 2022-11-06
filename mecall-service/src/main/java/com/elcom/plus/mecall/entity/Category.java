package com.elcom.plus.mecall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private Long catOrder;
    private Integer catType;
    private Long numberItem;
    private Integer updateType;
    private Long creatorId;
    private Integer parentId;
    private Long updateInterval;
    private Long sourceCat;
    private String fullPath;
    private String username;

    private Integer langId;
    private String catName;
    private String catDescription;
}
