package com.example.weatherproject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Громов on 08.02.2015.
 */
public class WeatherListFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        ;
        ArrayList<SimpleDayForecast> forecasts = (ArrayList<SimpleDayForecast>) bundle.getSerializable(WeatherFragmentActivity.LIST_KEY);
        ForecastAdapter adapter = new ForecastAdapter(forecasts);

        setListAdapter(adapter);
    }

    private class ForecastAdapter extends ArrayAdapter<SimpleDayForecast> {
        public ForecastAdapter(ArrayList<SimpleDayForecast> forecasts) {
            super(getActivity(), 0, forecasts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_fragment_layout, null);
            }

            SimpleDayForecast forecast = getItem(position);

            TextView date = (TextView) convertView.findViewById(R.id.date_month);
            date.setText(forecast.getDate());
            TextView day_text = (TextView) convertView.findViewById(R.id.day_text);
            day_text.setText(forecast.getDayName());
            TextView minTemperature = (TextView) convertView.findViewById(R.id.min_temperature);
            minTemperature.setText(forecast.getLow());
            TextView maxTemperature = (TextView) convertView.findViewById(R.id.max_temperature);
            maxTemperature.setText(forecast.getHigh());
            TextView description = (TextView) convertView.findViewById(R.id.weather_dicsription);
            description.setText(forecast.getText());
            convertView.setClickable(false);
            return convertView;
        }
    }


}
