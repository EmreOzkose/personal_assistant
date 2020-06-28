package com.example.personalassistant;

import java.util.concurrent.ThreadLocalRandom;

public class Assistan {

    String [] motivate_quotes = {
            "İleride çok başarılı olacaksın, emin ol",
            "İleride kazanacağın paraları düşün!",
            "H-index'in 103 olur inşallah !!!",
            "Kendine zaman tanı ve hayal et. Hayal etmek her zaman insanı motive eder!"
    };

    Assistan (){

    }

    /**
     *
     * @return one of motivation quotes randomly
     */
    public String motivate(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, motivate_quotes.length);
        return motivate_quotes[randomNum];
    }
}
