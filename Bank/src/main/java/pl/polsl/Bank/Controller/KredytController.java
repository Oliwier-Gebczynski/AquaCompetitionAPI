package pl.polsl.Bank.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.Bank.Model.Kredyt;
import pl.polsl.Bank.Model.Kredyt1;
import pl.polsl.Bank.Model.Kredyt2;

@Controller
@RequestMapping(path="/kredyt")
public class KredytController {
	  @Autowired
	    Kredyt kredyt;
	    @Autowired
	    Kredyt1 kredyt1;
	    @Autowired 
	    Kredyt2 kredyt2;

	    @GetMapping
	    public @ResponseBody Double getKredyt(@RequestParam Double kwota) {
	        return kredyt.obliczRate(kwota, 0.1, 12);
	    }

	    @GetMapping("/kredyt1")
	    public @ResponseBody Double getKredyt1(@RequestParam Double kwota) {
	        return kredyt1.obliczRate(kwota, 0.1, 12);
	    }

	    @GetMapping("/kredyt2")
	    public @ResponseBody Kredyt2 getKredyt2(@RequestParam Double kwota) {
	        kredyt2.obliczRate(kwota, 0.1, 12);
	        return kredyt2;
	    }

	    @PostMapping("/kredyt3")
	    public @ResponseBody Kredyt2 getKredyt3(@RequestBody Kredyt2 kredyt) {
	        System.out.println(kredyt.getKwota() + "," + kredyt.getProcent() + "," + kredyt.getIlerat() + "," + kredyt.getRata());

	        kredyt.obliczRate(kredyt.getKwota(), kredyt.getProcent(), kredyt.getIlerat());
	        return kredyt;
	    }
	}