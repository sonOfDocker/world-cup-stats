package com.worldcupstats.api.teams;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
class TeamEntity {
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String code;

    // JPA needs a default constructor
    protected TeamEntity() {}

    public TeamEntity(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
}
