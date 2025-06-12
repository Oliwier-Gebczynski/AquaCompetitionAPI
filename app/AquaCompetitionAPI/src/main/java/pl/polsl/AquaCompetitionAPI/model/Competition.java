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
@ToString(exclude = "races")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    private String location;
    private String organizer;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "competition-races")
    private Set<Race> races = new HashSet<>();

    public void addRace(Race race) {
        races.add(race);
        race.setCompetition(this);
    }
    public void removeRace(Race race) {
        races.remove(race);
        race.setCompetition(null);
    }
}
