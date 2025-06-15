package pl.polsl.AquaCompetitionAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.repository.CompetitorRepository;

import java.util.List;

import pl.polsl.AquaCompetitionAPI.repository.ResultRepository;
import pl.polsl.AquaCompetitionAPI.model.Result;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.model.Competition;
import pl.polsl.AquaCompetitionAPI.dto.PersonalBestDto;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompetitorService {
    
    @Autowired
    private CompetitorRepository competitorRepository;
    
    @Autowired
    private ResultRepository resultRepository;

    
    public List<Competitor> getAllCompetitors() {
        return competitorRepository.findAll();
    }
    
    public Competitor getCompetitorById(Long id) {
        return competitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competitor not found with id " + id));
    }
    
    public Competitor saveCompetitor(Competitor competitor) {
        return competitorRepository.save(competitor);
    }
    
    public void deleteCompetitor(Long id) {
        competitorRepository.deleteById(id);
    }
    
    public List<PersonalBestDto> getPersonalBests(Long competitorId) {
        Competitor competitor = getCompetitorById(competitorId);
        
        List<Result> allResults = resultRepository.findByCompetitorId(competitorId);
        
        List<Result> validResults = allResults.stream()
                .filter(r -> !r.isDisqualified())
                .filter(r -> r.getTime() != null && !r.getTime().trim().isEmpty())
                .collect(Collectors.toList());
        
        Map<String, List<Result>> groupedResults = validResults.stream()
                .collect(Collectors.groupingBy(result -> {
                    Race race = result.getRace();
                    return race.getStyle() + "-" + race.getDistance() + "-" + 
                           race.getCategory() + "-" + race.getGender();
                }));
        
        List<PersonalBestDto> personalBests = new ArrayList<>();
        
        for (Map.Entry<String, List<Result>> entry : groupedResults.entrySet()) {
            List<Result> raceTypeResults = entry.getValue();
            
            Result bestResult = raceTypeResults.stream()
                    .min(Comparator.comparing(Result::getTime))
                    .orElse(null);
            
            if (bestResult != null) {
                Race race = bestResult.getRace();
                Competition competition = race.getCompetition();
                
                PersonalBestDto personalBest = new PersonalBestDto();
                personalBest.setStyle(race.getStyle());
                personalBest.setDistance(race.getDistance());
                personalBest.setCategory(race.getCategory());
                personalBest.setGender(race.getGender());
                personalBest.setBestTime(bestResult.getTime());
                personalBest.setCompetitionName(competition != null ? competition.getName() : "Unknown");
                
                if (race.getDateTime() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    personalBest.setRaceDate(dateFormat.format(race.getDateTime()));
                }
                
                personalBests.add(personalBest);
            }
        }
        
        personalBests.sort(Comparator
                .comparing(PersonalBestDto::getStyle)
                .thenComparingInt(PersonalBestDto::getDistance));
        
        return personalBests;
    }
}