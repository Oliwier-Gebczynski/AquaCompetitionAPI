package pl.polsl.AquaCompetitionAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalBestDto {
    private String style;
    private int distance;
    private String category;
    private String gender;
    private String bestTime;
    private String competitionName;
    private String raceDate;
}