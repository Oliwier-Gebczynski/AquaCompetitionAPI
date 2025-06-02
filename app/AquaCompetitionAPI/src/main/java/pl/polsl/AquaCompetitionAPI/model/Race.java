package pl.polsl.AquaCompetitionAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Data
public class Race {
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "competition_id")
    @JsonBackReference
    private Competition competition;
    
    private String style;
    private int distance;
    private String category;
    private String gender;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    
    @OneToMany(mappedBy = "race")
    private Set<Result> results;
}

