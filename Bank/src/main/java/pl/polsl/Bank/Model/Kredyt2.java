package pl.polsl.Bank.Model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component("kredyt2")
@Getter 
@Setter

public class Kredyt2 {
    private Double Kwota;
    private Double Procent;
    private Integer Ilerat;
    private Double Rata;

    public Double obliczRate(double kwota, double procent, int ilerat) {
        this.Kwota = kwota;
        this.Procent = procent;
        this.Ilerat = ilerat;
        double m = 1 - 1 / Math.pow(1.0 + procent / 12, ilerat);
        this.Rata = kwota * (procent / 12) / m;
        return Rata;
    }
}