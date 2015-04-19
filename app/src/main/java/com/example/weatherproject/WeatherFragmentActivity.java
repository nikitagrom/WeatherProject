package com.example.weatherproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Громов on 08.02.2015.
 */
public class WeatherFragmentActivity extends FragmentActivity {

    public static final String FULL_FORECAST_KEY = "FULL_FORECAST";
    public static final String LIST_KEY = "LIST_DAYS";
    private static String cityWoeid ="918981";
    private static final String GET_FORECAST_DP = "http://weather.yahooapis.com/forecastrss?w=+"+cityWoeid+"&u=c&d=15";
    private static final int PAGES = 2;
    private static final int FULL_DAY_FORECAST_FRAGMENT = 0;
    private static final int WEATHER_LIST_FRAGMENT = 1;
    private ViewPager mViewPager;
    private FullDayForecast fullDayForecast;
    private SimpleXmlWeatherParser parser;
    private ArrayList<SimpleDayForecast> simpleDayForecasts;

    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            fullDayForecast = (FullDayForecast) savedInstanceState.getSerializable(FULL_FORECAST_KEY);
            simpleDayForecasts = (ArrayList<SimpleDayForecast>) savedInstanceState.get(LIST_KEY);


        }

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        mViewPager.setBackgroundResource(R.drawable.list_background);
        setContentView(mViewPager);

        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            int currentItem = mViewPager.getCurrentItem();

            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            fragments.get(FULL_DAY_FORECAST_FRAGMENT).getArguments().putSerializable(FULL_FORECAST_KEY, fullDayForecast);
            fragments.get(WEATHER_LIST_FRAGMENT).getArguments().putSerializable(LIST_KEY, simpleDayForecasts);
            mViewPager.setAdapter(new SimplePagerAdapter(fragmentManager));
            mViewPager.setCurrentItem(currentItem);
            mViewPager.getAdapter().notifyDataSetChanged();
        }
        else if (isOnline(this)) {

            MyTask task = new MyTask();
            task.execute();

            try {
                simpleDayForecasts = task.get();


            } catch (Exception e) {
                showErrorMessage();
            }

            fullDayForecast = (FullDayForecast) simpleDayForecasts.remove(0);

            FragmentManager fragmentManager = getSupportFragmentManager();

            mViewPager.setAdapter(new SimplePagerAdapter(fragmentManager));
        } else {

            checkConnectionMessage();


        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void checkConnectionMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherFragmentActivity.this);
        builder.setTitle("Oops")

                .setCancelable(false)
                .setPositiveButton("Please check internet connection",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alert = builder.create();

        alert.show();
    }

    private void showErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherFragmentActivity.this);
        builder.setTitle("Oops")

                .setCancelable(false)
                .setPositiveButton("Something happens,please restart application",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();

        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_acrivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;

        if (item.getItemId() == R.id.update_item) {

            if (isOnline(WeatherFragmentActivity.this)) {


                ret = true;
                try {
                    simpleDayForecasts = reloadInformation();
                    fullDayForecast = (FullDayForecast) simpleDayForecasts.remove(0);
                    int currentItem = mViewPager.getCurrentItem();
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    fragments.get(FULL_DAY_FORECAST_FRAGMENT).getArguments().putSerializable(FULL_FORECAST_KEY, fullDayForecast);
                    fragments.get(WEATHER_LIST_FRAGMENT).getArguments().putSerializable(LIST_KEY, simpleDayForecasts);
                    mViewPager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager()));
                    mViewPager.setCurrentItem(currentItem);
                    mViewPager.getAdapter().notifyDataSetChanged();


                } catch (Exception e) {

                    showErrorMessage();
                }
            }
            else checkConnectionMessage();
        } else if (item.getItemId() == R.id.showList) {
            ret = true;
            if (mViewPager.getCurrentItem() == WEATHER_LIST_FRAGMENT) {
                mViewPager.setCurrentItem(FULL_DAY_FORECAST_FRAGMENT);
            } else mViewPager.setCurrentItem(WEATHER_LIST_FRAGMENT);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return ret;
    }

    @Override
    protected void onRestart() {

        super.onRestart();

    }

    private ArrayList<SimpleDayForecast> reloadInformation() throws Exception {
        MyTask task = new MyTask();

        task.execute();

        ArrayList<SimpleDayForecast> forecasts = null;

        forecasts = task.get();


        return forecasts;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(LIST_KEY, simpleDayForecasts);
        outState.putSerializable(FULL_FORECAST_KEY, fullDayForecast);
        super.onSaveInstanceState(outState);

    }

    class SimplePagerAdapter extends FragmentPagerAdapter {

        SimplePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }


        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case FULL_DAY_FORECAST_FRAGMENT: {
                    Bundle args = new Bundle();
                    args.putSerializable(FULL_FORECAST_KEY, fullDayForecast);
                    Fragment fragment = new FullForecastFragment();
                    fragment.setArguments(args);

                    return fragment;
                }
                case WEATHER_LIST_FRAGMENT: {
                    Fragment fragment = new WeatherListFragment();
                    Bundle args = new Bundle();
                    args.putSerializable(LIST_KEY, simpleDayForecasts);
                    fragment.setArguments(args);

                    return fragment;
                }
                default: {
                    Bundle args = new Bundle();
                    args.putSerializable(FULL_FORECAST_KEY, fullDayForecast);
                    Fragment fragment = new FullForecastFragment();
                    fragment.setArguments(args);

                    return fragment;
                }

            }

        }

        @Override
        public int getCount() {
            return PAGES;
        }

    }

    class MyTask extends AsyncTask<Void, Integer, ArrayList<SimpleDayForecast>> {
        private ProgressDialog pDialog;
        private HttpURLConnection connection;
        ArrayList<SimpleDayForecast> list;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WeatherFragmentActivity.this);
            pDialog.setMessage("Working ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);

            pDialog.show();
            try {
                URL url = new URL(GET_FORECAST_DP);
                connection = (HttpURLConnection) url.openConnection();
            }   catch (Exception e){
                pDialog.dismiss();
               checkConnectionMessage();
            }

        }

        @Override
        protected ArrayList<SimpleDayForecast> doInBackground(Void... params) {
            try {

                parser = new SimpleXmlWeatherParser(connection.getInputStream());

               list = parser.parsing();

                return list;
            } catch (Exception e) {
checkConnectionMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(ArrayList<SimpleDayForecast> simpleDayForecasts) {
            super.onPostExecute(simpleDayForecasts);
            pDialog.dismiss();

        }
    }
}


