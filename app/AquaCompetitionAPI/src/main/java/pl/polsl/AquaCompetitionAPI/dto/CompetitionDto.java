package pl.polsl.AquaCompetitionAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDto {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;  
    private String location;
    private String organizer;
    private List<RaceDto> races; 
}
