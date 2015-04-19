package com.example.weatherproject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Громов on 08.02.2015.
 */

public class FullDayForecast extends SimpleDayForecast implements Serializable {

    private Wind w;
    private Atmosphere at;
    private Astronomy as;
    private String temperature;
    private String lastUpdate;

    public FullDayForecast(SimpleDayForecast dayForecast) {
        super(dayForecast.getDayName(),
                dayForecast.getDate(),
                dayForecast.getLow(),
                dayForecast.getHigh(),
                dayForecast.getText());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        lastUpdate = dateFormat.format(date);
    }

    public String getLastUpdate() {

        return lastUpdate;
    }


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature + "°С";
    }

    public Atmosphere getAtmosphere() {
        return at;
    }

    public void setAtmosphere(String humidity, String visibility, String pressure, String rising) {
        this.at = new Atmosphere(humidity, visibility, pressure, rising);
    }

    public Astronomy getAstronomy() {
        return as;
    }

    public void setAstronomy(String sunrise, String sunset) {
        this.as = new Astronomy(sunrise, sunset);
    }

    public Wind getW() {
        return w;
    }

    public void setWind(String chill, String speed, String direction) {
        this.w = new Wind(chill, speed, direction);
    }

    public class Wind implements Serializable {
        private String chill;
        private String direction;
        private String speed;

        Wind(String chill, String direction, String speed) {
            this.chill = chill + " " + "°С";
            this.speed = speed + " " + "km/h";
            int tmpDirection = Integer.parseInt(direction);

            if (getRange(11, 78, tmpDirection)) this.direction = "Northeast";

            else if (getRange(78, 123, tmpDirection)) this.direction = "East wind";

            else if (getRange(123, 168, tmpDirection)) this.direction = "Southeast";

            else if (getRange(168, 213, tmpDirection)) this.direction = "South wind";

            else if (getRange(213, 258, tmpDirection)) this.direction = "Southwest";

            else if (getRange(258, 303, tmpDirection)) this.direction = "West wind";


            else if (getRange(303, 348, tmpDirection)) this.direction = "Northwest";
            else this.direction = "North wind";

        }

        @Override
        public String toString() {
            return "Feels like\t\t" + chill + "\n" + direction + "\t\t" + speed;
        }

        private boolean getRange(int first, int second, int key) {
            if (key >= first && key <= second) return true;
            else return false;
        }
    }

    public class Atmosphere implements Serializable {
        private String humidity;
        private String visibility;
        private String pressure;
        private String rising;

        private Atmosphere(String humidity, String visibility, String pressure, String rising) {
            this.humidity = humidity + "%";
            this.visibility = visibility + " km";
            this.pressure = pressure + " mb";
            this.rising = rising + "°С";


        }

        public String toString() {
            return "Humidity\t\t" + humidity + "\nVisibility\t\t\t" + visibility + "\nPressure\t\t" + pressure;
        }
    }

    public class Astronomy implements Serializable {
        private String sunrise;
        private String sunset;

        private Astronomy(String sunrise, String sunset) {
            this.sunrise = sunrise;
            this.sunset = sunset;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        @Override
        public String toString() {
            return "Sunrise at \t" + sunrise + "\nSunset  at \t" + sunset;
        }
    }
}
