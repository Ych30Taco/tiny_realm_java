package com.taco.TinyRealm.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int level;
    private int power;
    private int vipLevel;
    private LocalDateTime lastLoginTime;

    // ...getter, setter...
}
