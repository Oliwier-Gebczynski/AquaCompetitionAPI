package pl.polsl.AquaCompetitionAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polsl.AquaCompetitionAPI.model.Competition;
import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.model.Result;
import pl.polsl.AquaCompetitionAPI.service.CompetitionService;
import pl.polsl.AquaCompetitionAPI.service.CompetitorService;
import pl.polsl.AquaCompetitionAPI.service.RaceService;
import pl.polsl.AquaCompetitionAPI.service.ResultService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompetitionSystemController {

	private final CompetitorService competitorService;
	private final CompetitionService competitionService;
	private final RaceService raceService;
	private final ResultService resultService;
	
	@Autowired
    public CompetitionSystemController(
            CompetitorService competitorService,
            CompetitionService competitionService,
            RaceService raceService,
            ResultService resultService
    ) {
        this.competitorService = competitorService;
        this.competitionService = competitionService;
        this.raceService = raceService;
        this.resultService = resultService;
    }

    // Competitors
	@GetMapping("/competitors")
	public List<Competitor> getAllCompetitors() {
	    List<Competitor> list = competitorService.getAllCompetitors();
	    return list;
	}
    @GetMapping("/competitors/{id}")
    public Competitor getCompetitor(@PathVariable Long id) {
        return competitorService.getCompetitorById(id);
    }

    @PostMapping("/competitors")
    public Competitor createCompetitor(@RequestBody Competitor c) {
        return competitorService.saveCompetitor(c);
    }

    @PutMapping("/competitors/{id}")
    public Competitor updateCompetitor(@PathVariable Long id, @RequestBody Competitor c) {
        c.setId(id);
        return competitorService.saveCompetitor(c);
    }

    @DeleteMapping("/competitors/{id}")
    public ResponseEntity<Void> deleteCompetitor(@PathVariable Long id) {
        competitorService.deleteCompetitor(id);
        return ResponseEntity.noContent().build();
    }

    // Competitions
    @GetMapping("/competitions")
    public List<Competition> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }

    @GetMapping("/competitions/{id}")
    public Competition getCompetition(@PathVariable Long id) {
        return competitionService.getCompetitionById(id);
    }
    
    @GetMapping("/competitions/{competitionId}/races")
    public List<Race> getRacesForCompetition(@PathVariable Long competitionId) {
    	return raceService.getRacesByCompetition(competitionId);
    }

    @PostMapping("/competitions")
    public Competition createCompetition(@RequestBody Competition comp) {
        return competitionService.saveCompetition(comp);
    }

    @PutMapping("/competitions/{id}")
    public Competition updateCompetition(@PathVariable Long id, @RequestBody Competition comp) {
        comp.setId(id);
        return competitionService.saveCompetition(comp);
    }
    
    @PostMapping("/competitions/{competitionId}/races")
    public Race createRaceForCompetition(@PathVariable Long competitionId, @RequestBody Race race) {
        return raceService.createRaceForCompetition(competitionService.getCompetitionById(competitionId), race);
    }

    @DeleteMapping("/competitions/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        competitionService.deleteCompetition(id);
        return ResponseEntity.noContent().build();
    }

    // Races
    @GetMapping("/races")
    public List<Race> getAllRaces() {
        return raceService.getAllRaces();
    }

    @GetMapping("/races/{id}")
    public Race getRace(@PathVariable Long id) {
        return raceService.getRaceById(id);
    }

    @GetMapping("/races/competition/{competitionId}")
    public List<Race> getRacesByCompetition(@PathVariable Long competitionId) {
        return raceService.getRacesByCompetition(competitionId);
    }

    @PostMapping("/races")
    public Race createRace(@RequestBody Race race) {
        return raceService.saveRace(race);
    }

    @PostMapping("/races/{id}/results")
    public Result createResultForRace(@PathVariable Long id, @RequestBody Result result) {
    	return resultService.createResultForRace(raceService.getRaceById(id), result);
    }
    @PutMapping("/races/{id}")
    public Race updateRace(@PathVariable Long id, @RequestBody Race race) {
        race.setId(id);
        return raceService.saveRace(race);
    }

    @DeleteMapping("/races/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }

    // Results
    @GetMapping("/results")
    public List<Result> getAllResults() {
        return resultService.getAllResults();
    }

    @GetMapping("/results/{id}")
    public Result getResult(@PathVariable Long id) {
        return resultService.getResultById(id);
    }

    @GetMapping("/results/competitor/{competitorId}")
    public List<Result> getResultsByCompetitor(@PathVariable Long competitorId) {
        return resultService.getResultsByCompetitor(competitorId);
    }

    @GetMapping("/results/race/{raceId}")
    public List<Result> getResultsByRace(@PathVariable Long raceId) {
        return resultService.getResultsByRace(raceId);
    }

    @PostMapping(value = "/results", consumes = "application/json")
    public Result createResult(@RequestBody Result result) {
        return resultService.saveResult(result);
    }

    @PutMapping(value = "/results/{id}", consumes = "application/json")
    public Result updateResult(@PathVariable Long id, @RequestBody Result result) {
        result.setId(id);
        return resultService.saveResult(result);
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/results/winner/{raceId}")
    public Competitor getWinner(@PathVariable Long raceId) {
        return resultService.getRaceWinner(raceId);
    }

    @GetMapping("/results/medallists/{competitionId}")
    public List<Competitor> getMedallists(@PathVariable Long competitionId) {
        return resultService.getCompetitionMedallists(competitionId);
    }
}
