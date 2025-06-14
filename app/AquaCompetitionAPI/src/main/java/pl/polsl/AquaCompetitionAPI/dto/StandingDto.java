package pl.polsl.AquaCompetitionAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandingDto {
    private int position;
    private CompetitorDto competitor;
    private String time;
    private int lane;
    private boolean disqualified;
    private String status;
}