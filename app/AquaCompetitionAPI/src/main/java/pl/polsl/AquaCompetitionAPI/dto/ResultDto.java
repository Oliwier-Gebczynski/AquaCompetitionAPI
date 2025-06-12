package pl.polsl.AquaCompetitionAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private Long id;
    private String time;
    private int lane;
    private int finalPosition;
    private boolean disqualified;
    private CompetitorDto competitor;
}
