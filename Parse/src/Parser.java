import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {
    public static void main(String[] args) {
        String url = "https://www.gismeteo.ru/weather-yekaterinburg-4517/"; //Ссылка на город
        try {
            Document doc = Jsoup.connect(url).get();
            Elements city = doc.select("[class=page-title]");
            Elements temp = doc.select("[class=unit unit_temperature_c]");
            Parser parse = new Parser();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = new Date();
            parse.weatherSave(city.first().text(), dateFormat.format(date), temp.first().text()); // Запишем даные в JSON
            parse.weatherRead(); //Прочитаем JSON и выведем в консоль
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String weatherGismeteo(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements city = doc.select("[class=page-title]");
            Elements temp = doc.select("[class=unit unit_temperature_c]");
            return city.first().text() + ": " + temp.first().text() + " C";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return " ";
    }

    private void weatherSave(String site, String date, String temperature){
        JSONObject logTemerature = new JSONObject();
        JSONArray sites = new JSONArray();

        JSONObject site1 = new JSONObject();
        site1.put("site", site);
        site1.put("date", date);
        site1.put("temperature", temperature);
        sites.add(site1);
        logTemerature.put("sites", sites);

        try {
            FileWriter file = new FileWriter("D:\\java_ex\\Parse\\src\\Site-temperature.json", false);
            file.write(logTemerature.toJSONString());
            file.flush();
            file.close();
            System.out.println("Json файл успешно создан");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void weatherRead(){
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser
                    .parse(new FileReader("D:\\java_ex\\Parse\\src\\Site-temperature.json"));
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println("Корневой элемент: "
                    + jsonObject.keySet().iterator().next());
            JSONArray jsonArray = (JSONArray) jsonObject.get("sites");

            for (Object o :jsonArray){
                JSONObject weather = (JSONObject) o;
                System.out.println("Запомненая погода");
                System.out.println("Город:  " + weather.get("site"));
                System.out.println("Дата:  " + weather.get("date"));
                System.out.println("Температура: " + weather.get("temperature") + " C");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
