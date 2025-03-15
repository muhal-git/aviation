package com.example.aviation.modules.locations.entity;

import com.example.aviation.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Location entity class to store location details in the database.
 * 
 * @see BaseEntity
 */
@Table(name = "TBL_LOCATIONS")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Location extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;
    
    @Column(nullable = false)
    private String city;

    @Column(nullable = false, unique = true)
    private String locationCode;

}
