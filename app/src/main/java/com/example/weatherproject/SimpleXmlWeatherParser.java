package com.example.weatherproject;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Громов on 08.02.2015.
 */
public class SimpleXmlWeatherParser {

    private Document document;

    public SimpleXmlWeatherParser(InputStream in)throws Exception{


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        document = documentBuilder.parse(in);



    }

    public ArrayList<SimpleDayForecast> parsing() {

        ArrayList<SimpleDayForecast> lists = new ArrayList<>();

        NodeList list = document.getElementsByTagName("yweather:forecast");

        for (int i = 0; i < list.getLength(); i++) {
            NamedNodeMap atr = list.item(i).getAttributes();
            SimpleDayForecast simpleDayForecast = new SimpleDayForecast(
                    atr.item(0).getNodeValue(),
                    atr.item(1).getNodeValue(),
                    atr.item(2).getNodeValue(),
                    atr.item(3).getNodeValue(),
                    atr.item(4).getNodeValue()
            );
            lists.add(simpleDayForecast);


        }
        FullDayForecast tmp = parsingFullForecastToday(lists.get(0));
        lists.set(0, tmp);
        return lists;
    }

    private FullDayForecast parsingFullForecastToday(SimpleDayForecast dayForecast) {
        FullDayForecast today = new FullDayForecast(dayForecast);
        NodeList list = document.getElementsByTagName("yweather:wind");
        NamedNodeMap atr = list.item(0).getAttributes();
        today.setWind(atr.item(0).getNodeValue(),
                atr.item(1).getNodeValue(),
                atr.item(2).getNodeValue());
        list = document.getElementsByTagName("yweather:atmosphere");
        atr = list.item(0).getAttributes();
        today.setAtmosphere(atr.item(0).getNodeValue(),
                atr.item(1).getNodeValue(),
                atr.item(2).getNodeValue(),
                atr.item(3).getNodeValue());

        list = document.getElementsByTagName("yweather:astronomy");
        atr = list.item(0).getAttributes();
        today.setAstronomy(atr.item(0).getNodeValue(),
                atr.item(1).getNodeValue());
        list = document.getElementsByTagName("yweather:condition");
        atr = list.item(0).getAttributes();
        today.setText(atr.item(0).getNodeValue());
        today.setTemperature(atr.item(2).getNodeValue());
        today.setDayName(atr.item(3).getNodeValue().substring(0, 2));
        return today;
    }
}
