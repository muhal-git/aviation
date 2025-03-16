package com.example.aviation.modules.transportations.entity;

import java.util.List;

import com.example.aviation.common.entity.BaseEntity;
import com.example.aviation.modules.locations.entity.Location;
import com.example.aviation.modules.transportations.enums.TransportationType;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Transportation entity class to store transportation details in the database.
 * 
 * @see BaseEntity
 * @see Location
 * @see TransportationType
 */
@Table(name = "TBL_TRANSPORTATIONS", uniqueConstraints = {
    @UniqueConstraint(name = "uk_transportation_origin_destination_type", 
                     columnNames = {"origin_id", "destination_id", "transportation_type"})
})
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Transportation extends BaseEntity {

    @ManyToOne
    private Location origin;

    @ManyToOne
    private Location destination;

    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;

    @ElementCollection
    private List<Integer> operatingDays;

    public boolean isOriginMatch(Location location) {
        return this.origin.getLocationCode().equals(location.getLocationCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Transportation that = (Transportation) o;
        
        if (origin == null || that.origin == null || 
            destination == null || that.destination == null) {
            return false;
        }
        
        if (!origin.getLocationCode().equals(that.origin.getLocationCode())) return false;
        if (!destination.getLocationCode().equals(that.destination.getLocationCode())) return false;
        if (transportationType != that.transportationType) return false;
        
        // Compare operatingDays list
        return operatingDays == null ? that.operatingDays == null : 
               operatingDays.size() == that.operatingDays.size() && 
               operatingDays.containsAll(that.operatingDays);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (origin != null ? origin.getLocationCode().hashCode() : 0);
        result = 31 * result + (destination != null ? destination.getLocationCode().hashCode() : 0);
        result = 31 * result + (transportationType != null ? transportationType.hashCode() : 0);
        result = 31 * result + (operatingDays != null ? operatingDays.hashCode() : 0);
        return result;
    }
}
