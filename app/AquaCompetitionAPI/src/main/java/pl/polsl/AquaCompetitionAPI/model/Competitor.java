package pl.polsl.AquaCompetitionAPI.model;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class Competitor {
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    private String gender;
    private String club;
    private String category;
    
    @OneToMany(mappedBy = "competitor")
    private Set<Result> results;
}
