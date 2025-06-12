package pl.polsl.AquaCompetitionAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth; 
    private String gender;
    private String club;
    private String category;
}
