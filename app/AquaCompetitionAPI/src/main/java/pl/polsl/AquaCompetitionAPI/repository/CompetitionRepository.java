package pl.polsl.AquaCompetitionAPI.repository;

import pl.polsl.AquaCompetitionAPI.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}