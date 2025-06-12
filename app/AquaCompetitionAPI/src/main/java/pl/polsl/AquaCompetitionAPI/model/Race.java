package pl.polsl.AquaCompetitionAPI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    @JsonBackReference(value = "competition-races")
    private Competition competition;

    private String style;
    private int distance;
    private String category;
    private String gender;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "race-results")
    private Set<Result> results = new HashSet<>();

    public void addResult(Result result) {
        results.add(result);
        result.setRace(this);
    }
    public void removeResult(Result result) {
        results.remove(result);
        result.setRace(null);
    }
}
