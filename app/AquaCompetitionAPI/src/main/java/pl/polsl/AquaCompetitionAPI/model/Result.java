package pl.polsl.AquaCompetitionAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class Result {
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;
    
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;
    
    private String time;

    private int lane;
    private int finalPosition;
    private boolean disqualified;
    
    public boolean isDisqualified() { 
        return disqualified; 
    }
}
