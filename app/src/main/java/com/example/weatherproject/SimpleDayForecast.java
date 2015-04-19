package com.example.weatherproject;

import java.io.Serializable;

/**
 * Created by Громов on 07.02.2015.
 */
public class SimpleDayForecast implements Serializable {
    private String dayName;
    private String date;
    private String low;
    private String high;
    private String text;

    SimpleDayForecast(String dayName, String date, String low, String high, String text) {
        this.dayName = dayName;
        this.date = date.substring(0, 6);

        this.low = "Min\u0020" + low;
        this.high = "Max\u0020" + high;

        this.text = text;

    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLow() {
        return low;
    }

    public String getHigh() {
        return high;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return dayName + " " + date + " low temperature " + low + " high temperature " + high + " text" + text;
    }

}


