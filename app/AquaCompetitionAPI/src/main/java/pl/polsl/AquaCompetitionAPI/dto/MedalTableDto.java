package pl.polsl.AquaCompetitionAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedalTableDto {
    private CompetitorDto competitor;
    private int goldMedals;
    private int silverMedals;
    private int bronzeMedals;
    private int totalMedals;
    
    public MedalTableDto(CompetitorDto competitor, int gold, int silver, int bronze) {
        this.competitor = competitor;
        this.goldMedals = gold;
        this.silverMedals = silver;
        this.bronzeMedals = bronze;
        this.totalMedals = gold + silver + bronze;
    }
}