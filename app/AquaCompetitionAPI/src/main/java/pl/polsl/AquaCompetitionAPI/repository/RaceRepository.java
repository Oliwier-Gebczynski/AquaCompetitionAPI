package pl.polsl.AquaCompetitionAPI.repository;

import pl.polsl.AquaCompetitionAPI.model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    List<Race> findByCompetitionId(Long competitionId);
}
