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
public class RaceDto {
    private Long id;
    private String style;
    private int distance;
    private String category;
    private String gender;
    private String dateTime;
    private List<ResultDto> results; 
}
