package pl.polsl.AquaCompetitionAPI.service;

import pl.polsl.AquaCompetitionAPI.model.Competition;
import pl.polsl.AquaCompetitionAPI.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionService {
    
    @Autowired
    private CompetitionRepository competitionRepository;
    
    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }
    
    public Competition getCompetitionById(Long id) {
        return competitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competition not found with id " + id));
    }
    
    public Competition saveCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }
    
    public void deleteCompetition(Long id) {
        competitionRepository.deleteById(id);
    }
}
