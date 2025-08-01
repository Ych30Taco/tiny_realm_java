package com.taco.TinyRealm.module.buildingModule.model;


import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Building {

    private String id;
    private String name;
    private String type;
    private int maxCount;
    private List<LevelData> levels;
}


