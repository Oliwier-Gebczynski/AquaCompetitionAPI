package pl.polsl.AquaCompetitionAPI.dto;

import pl.polsl.AquaCompetitionAPI.model.Competition;
import pl.polsl.AquaCompetitionAPI.model.Competitor;
import pl.polsl.AquaCompetitionAPI.model.Race;
import pl.polsl.AquaCompetitionAPI.model.Result;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DtoMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    // Competition -> CompetitionDto
    public static CompetitionDto toCompetitionDto(Competition comp) {
        if (comp == null) return null;
        CompetitionDto dto = new CompetitionDto();
        dto.setId(comp.getId());
        dto.setName(comp.getName());
        if (comp.getStartDate() != null) {
            dto.setStartDate(DATE_FORMAT.format(comp.getStartDate()));
        }
        if (comp.getEndDate() != null) {
            dto.setEndDate(DATE_FORMAT.format(comp.getEndDate()));
        }
        dto.setLocation(comp.getLocation());
        dto.setOrganizer(comp.getOrganizer());
        
        List<RaceDto> raceDtos = comp.getRaces().stream()
                .map(DtoMapper::toRaceDto)
                .collect(Collectors.toList());
        dto.setRaces(raceDtos);
        return dto;
    }

    // Race -> RaceDto
    public static RaceDto toRaceDto(Race race) {
        if (race == null) return null;
        RaceDto dto = new RaceDto();
        dto.setId(race.getId());
        dto.setStyle(race.getStyle());
        dto.setDistance(race.getDistance());
        dto.setCategory(race.getCategory());
        dto.setGender(race.getGender());
        if (race.getDateTime() != null) {
            dto.setDateTime(DATETIME_FORMAT.format(race.getDateTime()));
        }
        
        List<ResultDto> resultDtos = race.getResults().stream()
                .map(DtoMapper::toResultDto)
                .collect(Collectors.toList());
        dto.setResults(resultDtos);
        return dto;
    }

    // Result -> ResultDto
    public static ResultDto toResultDto(Result result) {
        if (result == null) return null;
        ResultDto dto = new ResultDto();
        dto.setId(result.getId());
        dto.setTime(result.getTime());
        dto.setLane(result.getLane());
        dto.setFinalPosition(result.getFinalPosition());
        dto.setDisqualified(result.isDisqualified());
        
        dto.setCompetitor(toCompetitorDto(result.getCompetitor()));
        return dto;
    }

    // Competitor -> CompetitorDto
    public static CompetitorDto toCompetitorDto(Competitor comp) {
        if (comp == null) return null;
        CompetitorDto dto = new CompetitorDto();
        dto.setId(comp.getId());
        dto.setFirstName(comp.getFirstName());
        dto.setLastName(comp.getLastName());
        if (comp.getDateOfBirth() != null) {
            dto.setDateOfBirth(DATE_FORMAT.format(comp.getDateOfBirth()));
        }
        dto.setGender(comp.getGender());
        dto.setClub(comp.getClub());
        dto.setCategory(comp.getCategory());
        
        return dto;
    }
    
    public static StandingDto toStandingDto(Result result, int position) {
        if (result == null) return null;
        
        StandingDto dto = new StandingDto();
        dto.setPosition(position);
        dto.setCompetitor(toCompetitorDto(result.getCompetitor()));
        dto.setTime(result.getTime());
        dto.setLane(result.getLane());
        dto.setDisqualified(result.isDisqualified());
        
        if (result.isDisqualified()) {
            dto.setStatus("DISQUALIFIED");
        } else if (result.getTime() != null && !result.getTime().trim().isEmpty()) {
            dto.setStatus("FINISHED");
        } else {
            dto.setStatus("DNS"); 
        }
        
        return dto;
    }
}
