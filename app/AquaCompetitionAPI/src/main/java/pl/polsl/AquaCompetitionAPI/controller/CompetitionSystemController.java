package pl.polsl.AquaCompetitionAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pl.polsl.AquaCompetitionAPI.model.Competition;
import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.model.Result;
import pl.polsl.AquaCompetitionAPI.service.CompetitionService;
import pl.polsl.AquaCompetitionAPI.service.CompetitorService;
import pl.polsl.AquaCompetitionAPI.service.RaceService;
import pl.polsl.AquaCompetitionAPI.service.ResultService;

import java.util.List;

@Controller
@RequestMapping(path="/api")
public class CompetitionSystemController {

    @Autowired
    private CompetitorService competitorService;
    
    @Autowired
    private CompetitionService competitionService;
    
    @Autowired
    private RaceService raceService;
    
    @Autowired
    private ResultService resultService;
}
