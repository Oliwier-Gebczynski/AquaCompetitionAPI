package pl.polsl.AquaCompetitionAPI.repository;

import pl.polsl.AquaCompetitionAPI.model.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
}
