package pl.polsl.AquaCompetitionAPI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    private String location;
    private String organizer;
    
    @OneToMany(mappedBy = "competition")
    @JsonManagedReference
    private Set<Race> races;

}
