package pl.polsl.AquaCompetitionAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Time;

@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;
    
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;
    
    private Time time;
    private int lane;
    private int finalPosition;
    private boolean disqualified;
}

