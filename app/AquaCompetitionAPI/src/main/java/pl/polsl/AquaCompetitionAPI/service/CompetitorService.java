package pl.polsl.AquaCompetitionAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.repository.CompetitorRepository;

import java.util.List;

@Service
public class CompetitorService {
    
    @Autowired
    private CompetitorRepository competitorRepository;
    
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
}