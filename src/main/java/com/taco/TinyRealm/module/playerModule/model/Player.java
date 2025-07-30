package com.taco.TinyRealm.module.playerModule.model;
import com.fasterxml.jackson.annotation.JsonProperty;
// 新增Lombok註解
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("level")
    private int level;
    @JsonProperty("experience")
    private int experience; 
}