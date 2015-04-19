package com.example.weatherproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Громов on 08.02.2015.
 */

public class FullForecastFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        TextView city = (TextView) view.findViewById(R.id.city_field);
        TextView temperature = (TextView) view.findViewById(R.id.current_temperature_field);
        TextView details = (TextView) view.findViewById(R.id.details_field);
        FullDayForecast dayForecast = (FullDayForecast) getArguments().getSerializable(WeatherFragmentActivity.FULL_FORECAST_KEY);
        city.setText("Dnepropetrovsk");
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.weather_icon);
        setWeatherIcon(dayForecast, imageButton);
        imageButton.setClickable(false);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView lastUpdate = (TextView) view.findViewById(R.id.updated_field);


        lastUpdate.setText("Last update: " + dayForecast.getLastUpdate());

        temperature.setText(dayForecast.getTemperature());

        details.setText(dayForecast.getW().toString() + "\n" + dayForecast.getAstronomy().toString() + "\n" +
                        dayForecast.getAtmosphere()
        );

        return view;
    }


    private void setWeatherIcon(FullDayForecast forecast, ImageButton image) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
        String currentTime = format.format(date).toLowerCase();

        boolean isDay = isDay(forecast, currentTime);
        switch (forecast.getText()) {
            case "Clear":
                image.setImageResource(R.drawable.sunny_night);
                break;
            case "Sunny":
                image.setImageResource(R.drawable.sunny);
                break;
            case "Fair": {
                if (isDay)
                    image.setImageResource(R.drawable.cloudy1);
                else image.setImageResource(R.drawable.cloudy1_night);
                break;
            }

            case "Mostly Cloudy": {
                if (isDay)
                    image.setImageResource(R.drawable.cloudy2);
                else image.setImageResource(R.drawable.cloudy2_night);
                break;

            }

            case "Partly Cloudy": {
                if (isDay)
                    image.setImageResource(R.drawable.cloudy3);
                else image.setImageResource(R.drawable.cloudy3_night);
                break;

            }

            case "Cloudy": {
                if (isDay)
                    image.setImageResource(R.drawable.cloudy4);
                else image.setImageResource(R.drawable.cloudy4_night);
                break;
            }
            case "Light Snow": {
                if (isDay)
                    image.setImageResource(R.drawable.snow1);
                else image.setImageResource(R.drawable.snow1_night);
                break;
            }
            case "Light Snow Shower": {
                image.setImageResource(R.drawable.sleet);
                break;
            }
            default:
                image.setImageResource(R.drawable.dunno);
                break;


        }


    }

    private boolean isDay(FullDayForecast forecast, String s2) {
        return forecast.getAstronomy().getSunrise().compareTo(s2) < 0 &&
                forecast.getAstronomy().getSunset().compareTo(s2) > 0;
    }

}
