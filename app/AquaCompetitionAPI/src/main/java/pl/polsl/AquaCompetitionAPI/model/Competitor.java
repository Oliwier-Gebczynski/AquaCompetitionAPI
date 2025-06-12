package pl.polsl.AquaCompetitionAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "results")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String firstName;
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String gender;
    private String club;
    private String category;

    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "competitor-results")
    private Set<Result> results = new HashSet<>();

    public void addResult(Result result) {
        results.add(result);
        result.setCompetitor(this);
    }
    public void removeResult(Result result) {
        results.remove(result);
        result.setCompetitor(null);
    }
}
