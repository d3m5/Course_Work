package Parse;

import Bot.Bot;
import Config.Config;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;

public class Parse {
    public String[] getWeatherOneDay(String city) {
        String[] weatherNow;
        try {
            Config config = new Config();
            String url = config.getConfig("urlCurrent") + city + config.getConfig("url2");
            String jsonOpenWeather = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONObject obj = (JSONObject) JSONValue.parseWithException(jsonOpenWeather);
            JSONObject object = (JSONObject) obj.get("main");
            JSONArray obj2 = (JSONArray) obj.get("weather");
            JSONObject weather = (JSONObject) obj2.get(0);
            weatherNow = new String[]{
                    String.valueOf(obj.get("name")),
                    String.valueOf(weather.get("main")),
                    String.valueOf(weather.get("description")),
                    String.valueOf(object.get("temp_min")),
                    String.valueOf(object.get("temp_max")),
                    String.valueOf(object.get("temp")),
                    String.valueOf(object.get("humidity")),
                    String.valueOf(weather.get("icon")),
                    String.valueOf(object.get("pressure")),
                    String.valueOf(obj.get("dt")),
                    String.valueOf(obj.get("timezone"))};
            return weatherNow;
        } catch (HttpStatusException e) {
            weatherNow = new String[]{"404",city};
            System.out.println("Город не найден " + e);
            return weatherNow;
        } catch (IOException | ParseException e) {
            System.out.println("Ошибка доступа" + e);
        }
        return new String[]{"Ошибка"};
    }

    public String[][] getWeatherTheeDay(String city) {
        String[][] weatherNow = new String[10][];
        try {
            Config config = new Config();
            String url = config.getConfig("urlFourDays") + city + config.getConfig("url2");
            String jsonOpenWeather = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONObject obj = (JSONObject) JSONValue.parseWithException(jsonOpenWeather);
            JSONArray jsonArrayList = (JSONArray) obj.get("list");//Запрос массива
            JSONObject objCity = (JSONObject) obj.get("city");
            int count = 0;
            for (int i = 0; 24 > i; i++) {
                JSONObject objInArray = (JSONObject) jsonArrayList.get(i);//Запрос элемента в массиве по номеру вхождения
                JSONObject objMain = (JSONObject) objInArray.get("main");
                JSONObject objWind = (JSONObject) objInArray.get("wind");
                JSONArray jsonArrayWeather = (JSONArray) objInArray.get("weather");
                JSONObject objArrayWeather = (JSONObject) jsonArrayWeather.get(0);
                long epoch = (long) objInArray.get("dt") - 18000;
                String date = new java.text.SimpleDateFormat("HH").format(new java.util.Date(epoch * 1000));
                if (date.equals("03") || date.equals("09") || date.equals("15")) {
                    weatherNow[count] = new String[]{String.valueOf(objInArray.get("dt_txt")),//дата в текстовом формате
                            String.valueOf(objInArray.get("dt")),//Дата в unix формате
                            String.valueOf(objCity.get("name")),//Город
                            String.valueOf(objArrayWeather.get("description")),//Описание погоды
                            String.valueOf(objMain.get("temp")),//Температура
                            String.valueOf(objMain.get("humidity")),//Влажность
                            String.valueOf(objArrayWeather.get("icon")),//Код картинки температуры
                            String.valueOf(objMain.get("pressure")),//Давление
                            String.valueOf(objWind.get("speed")),//Скорость ветра
                            String.valueOf(objCity.get("timezone"))};//Часовой пояс
                    count++;
                }
            }
            return weatherNow;
        } catch (HttpStatusException e) {
            weatherNow[0] = new String[]{"404",city};
            System.out.println("Город не найден " + e);
            return weatherNow;
        } catch (IOException | ParseException e) {
            System.out.println("Ошибка доступа" + e);
        } catch (NullPointerException e) {
            System.out.println("Параметр не найден");
        }
        return weatherNow;
    }
}
