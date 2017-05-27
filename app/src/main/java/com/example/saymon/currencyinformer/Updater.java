package com.example.saymon.currencyinformer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by saymon on 23.12.16.
 */
public class Updater implements Runnable {
    double current;
    double max;
    double min;

    public Updater(double current, double max, double min) {
        this.current = current;
        this.max = max;
        this.min = min;
    }

    @Override
    public void run() {
        if (MainActivity.maxValue != 0){
            max = MainActivity.maxValue;
        }
        if (MainActivity.minValue != 0){
            min = MainActivity.minValue;
        }
        double d = -1;
        try {
            d = getCurrent();

//            System.out.println(d);
//            System.out.println(current);

        } catch (Exception e) {
            current = -1;
        }

        current = d;
        MainActivity.subService.current = d;
        MainActivity.currentValue = current;


    }

    private double getCurrent() throws IOException {
        String line;
        double result = -1;
//
//        Pattern pattern = Pattern.compile(".*<div class='uccResultAndAdWrapper.*");
//        URL url = null;
//        try {
//            url = new URL("http://www.xe.com/currencyconverter/convert/?Amount=1&From=USD&To=RUB");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new InputStreamReader(url.openStream()));
//        } catch (IOException e) {
//            System.out.println("ошибка стрима");
//        }
//
//
//        try {
//            while ((line = reader.readLine()) != null) {
//                Matcher matcher = pattern.matcher(line);
//
//                if (matcher.matches()) {
//
//                    String line3 = line.replaceAll("[^.\\d\\s]", "");
//                    String[] tmp = line3.split(" ");
//                    for (int i = 0; i < tmp.length; i++) {
//                        String tmp2 = tmp[i].trim();
//
//                        if (tmp2.length() == 7) {
//                            result = Double.parseDouble(tmp2);
//                            break;
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("ошибка парсинга");
//        }
//        reader.close();


        Document doc = Jsoup.connect("http://www.xe.com/currencyconverter/convert/?Amount=1&From=USD&To=RUB").userAgent(" Chrome/58.0.3029.81").timeout(5000).get();
        String s = doc.getElementsByClass("uccResultAmount").first().text();
//        System.out.println(s);

        try {
            result = Double.parseDouble(s);
        } catch (Exception e) {
            result = -1;
        }
        System.out.println(result);

        return result;

    }
}
