package com.taco.TinyRealm.module.buildingModule.model;


import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBuliding {
    private String ownerId;
    private String buildingId;
    private String instanceId;
    private int level;
    private BuildingStatus status;
    private long buildStartTime;
    private long buildEndTime;
    private int positionX;
    private int positionY;

    
}

