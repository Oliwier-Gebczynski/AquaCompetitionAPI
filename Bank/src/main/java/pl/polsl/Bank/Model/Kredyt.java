package pl.polsl.Bank.Model;

import org.springframework.stereotype.Component;

@Component
public class Kredyt {
	
	public double obliczRate(
			double kwota, double procent, double rat) {
			double m = 1 - 1 / Math.pow(1.0 + procent / 12, rat);
			double rata = kwota * (procent / 12) / m;
			return rata;
	}
}
