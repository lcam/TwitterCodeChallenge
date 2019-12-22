package com.twitter.challenge.util;

import com.twitter.challenge.model.Conditions;

import java.util.List;

public class StandardDev {
    /**
     * Converts temperature in Celsius to temperature in Fahrenheit.
     *
     * @param conditionsList List of conditions data to calculate standard deviation with.
     * @return Standard Deviation of temperatures in Celsius.
     */
    public static double calculate(List<Conditions> conditionsList) {
        int len = conditionsList.size();
        float sum = 0;
        float avg = 0;
        for(int i=0; i<len; i++){
            sum += conditionsList.get(i).getWeather().getTemp();
        }
        avg = sum / 5;
        sum = 0; //reset sum, going to use it again below

        for(int i=0; i<len; i++){
            sum += Math.pow(conditionsList.get(i).getWeather().getTemp() - avg, 2);
        }

        return Math.sqrt(sum / (len-1));
    }
}
