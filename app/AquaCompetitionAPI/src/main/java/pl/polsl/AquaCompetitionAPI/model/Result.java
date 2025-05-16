package pl.polsl.AquaCompetitionAPI.model;

import jakarta.persistence.*;
import lombok.Data;

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
    
    private long time;

    private int lane;
    private int finalPosition;
    private boolean disqualified;
    
	public boolean isDiscqualified() { return disqualified; }
//	public int getFinalPosition() { return finalPosition; }
//	public long getTime() { return time; }
//	public Competitor getCompetitor() {	return competitor; }
}

