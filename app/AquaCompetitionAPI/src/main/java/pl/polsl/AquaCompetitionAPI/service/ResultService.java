package pl.polsl.AquaCompetitionAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.model.Result;
import pl.polsl.AquaCompetitionAPI.repository.ResultRepository;
import pl.polsl.AquaCompetitionAPI.dto.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResultService {
	
	private static class MedalCount {
	    final Competitor competitor;
	    int gold;
	    int silver;
	    int bronze;
	    
	    MedalCount(Competitor competitor, int gold, int silver, int bronze) {
	        this.competitor = competitor;
	        this.gold = gold;
	        this.silver = silver;
	        this.bronze = bronze;
	    }
	}

    
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
    
    public List<StandingDto> getRaceStandings(Long raceId) {
        List<Result> raceResults = resultRepository.findByRaceId(raceId);
        
        if (raceResults.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Result> sortedResults = raceResults.stream()
                .sorted((r1, r2) -> {
                    if (r1.isDisqualified() && !r2.isDisqualified()) return 1;
                    if (!r1.isDisqualified() && r2.isDisqualified()) return -1;
                    
                    if (r1.isDisqualified() && r2.isDisqualified()) {
                        return Integer.compare(r1.getLane(), r2.getLane());
                    }
                    
                    if (r1.getFinalPosition() > 0 && r2.getFinalPosition() > 0) {
                        return Integer.compare(r1.getFinalPosition(), r2.getFinalPosition());
                    }
                    
                    if (r1.getTime() != null && r2.getTime() != null) {
                        return r1.getTime().compareTo(r2.getTime());
                    }
                    
                    return Integer.compare(r1.getLane(), r2.getLane());
                })
                .collect(Collectors.toList());
        
        List<StandingDto> standings = new ArrayList<>();
        int position = 1;
        
        for (Result result : sortedResults) {
            if (result.isDisqualified()) {
                standings.add(DtoMapper.toStandingDto(result, -1)); // -1 for disqualified
            } else {
                standings.add(DtoMapper.toStandingDto(result, position));
                position++;
            }
        }
        
        return standings;
    }
    
    public List<MedalTableDto> getCompetitionMedalTable(Long competitionId) {
        List<Race> races = raceService.getRacesByCompetition(competitionId);
        Map<Long, MedalCount> medalCounts = new HashMap<>();
        
        for (Race race : races) {
            List<Result> raceResults = resultRepository.findByRaceId(race.getId()).stream()
                    .filter(r -> !r.isDisqualified())
                    .sorted(Comparator.comparingInt(Result::getFinalPosition))
                    .collect(Collectors.toList());
            
            // Award medals for top 3 positions
            for (int i = 0; i < Math.min(3, raceResults.size()); i++) {
                Result result = raceResults.get(i);
                Competitor competitor = result.getCompetitor();
                
                if (competitor != null) {
                    Long competitorId = competitor.getId();
                    MedalCount count = medalCounts.computeIfAbsent(competitorId, 
                        k -> new MedalCount(competitor, 0, 0, 0));
                    
                    switch (i) {
                        case 0: count.gold++; break;
                        case 1: count.silver++; break;
                        case 2: count.bronze++; break;
                    }
                }
            }
        }
        
        return medalCounts.values().stream()
                .filter(mc -> mc.gold > 0 || mc.silver > 0 || mc.bronze > 0)
                .map(mc -> new MedalTableDto(
                    DtoMapper.toCompetitorDto(mc.competitor),
                    mc.gold, mc.silver, mc.bronze))
                .sorted((m1, m2) -> {
                    // Sort by gold medals first, then silver, then bronze
                    int goldCompare = Integer.compare(m2.getGoldMedals(), m1.getGoldMedals());
                    if (goldCompare != 0) return goldCompare;
                    
                    int silverCompare = Integer.compare(m2.getSilverMedals(), m1.getSilverMedals());
                    if (silverCompare != 0) return silverCompare;
                    
                    return Integer.compare(m2.getBronzeMedals(), m1.getBronzeMedals());
                })
                .collect(Collectors.toList());
    }
}