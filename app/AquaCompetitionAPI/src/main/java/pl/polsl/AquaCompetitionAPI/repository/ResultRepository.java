package pl.polsl.AquaCompetitionAPI.repository;

import pl.polsl.AquaCompetitionAPI.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByCompetitorId(Long competitorId);
    List<Result> findByRaceId(Long raceId);
}