package pl.polsl.AquaCompetitionAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.model.Result;
import pl.polsl.AquaCompetitionAPI.repository.ResultRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {
    
    @Autowired
    private ResultRepository resultRepository;
    
    @Autowired
    private RaceService raceService;
    
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }
    
    public Result getResultById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id " + id));
    }
    
    public List<Result> getResultsByCompetitor(Long competitorId) {
        return resultRepository.findByCompetitorId(competitorId);
    }
    
    public List<Result> getResultsByRace(Long raceId) {
        return resultRepository.findByRaceId(raceId);
    }
    
    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }
    
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }
    
    public Result createResultForRace(Race race, Result result) {
        result.setRace(race);
        return resultRepository.save(result);
    }
    
    public Competitor getRaceWinner(Long raceId) {
        List<Result> raceResults = resultRepository.findByRaceId(raceId);

        if (raceResults.isEmpty()) {
            throw new RuntimeException("No results found for race with id " + raceId);
        }

        Result bestResult = raceResults.stream()
                .filter(r -> !r.isDisqualified())
                .min(Comparator.comparingInt(Result::getFinalPosition))
                .orElseThrow(() -> new RuntimeException("No valid (non-disqualified) results found for race with id " + raceId));

        Competitor winner = bestResult.getCompetitor();
        if (winner == null) {
            throw new RuntimeException("Winner result has no competitor, result id " + bestResult.getId());
        }
        return winner;
    }
    
    public List<Competitor> getCompetitionMedallists(Long competitionId) {
        List<Competitor> medallists = new ArrayList<>();
        List<Race> races = raceService.getRacesByCompetition(competitionId);
        
        for (Race race : races) {
            Long rId = race.getId();
            List<Result> raceResults = resultRepository.findByRaceId(rId).stream()
                    .filter(r -> !r.isDisqualified())
                    .sorted(Comparator.comparingInt(Result::getFinalPosition))
                    .collect(Collectors.toList());
            
            raceResults.stream()
                    .limit(3)
                    .map(Result::getCompetitor)
                    .forEach(medallists::add);
        }
        
        return medallists;
    }
}