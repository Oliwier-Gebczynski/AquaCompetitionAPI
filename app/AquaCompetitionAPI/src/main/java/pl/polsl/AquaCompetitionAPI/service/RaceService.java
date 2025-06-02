package pl.polsl.AquaCompetitionAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.polsl.AquaCompetitionAPI.model.Competition;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.repository.RaceRepository;


import java.util.List;

@Service
public class RaceService {
    
    @Autowired
    private RaceRepository raceRepository;
    
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }
    
    public Race getRaceById(Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Race not found with id " + id));
    }
    
    public List<Race> getRacesByCompetition(Long competitionId) {
        return raceRepository.findByCompetitionId(competitionId);
    }
    
    public Race createRaceForCompetition(Competition competition, Race race) {
        race.setCompetition(competition);
        return raceRepository.save(race);
    }
    
    public Race saveRace(Race race) {
        return raceRepository.save(race);
    }
    
    public void deleteRace(Long id) {
        raceRepository.deleteById(id);
    }
}