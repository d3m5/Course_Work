package Config;

import java.io.*;
import java.util.Properties;

public class Config {
    public String getConfig(String key) {
        FileInputStream file;
        Properties property = new Properties();
        try {
            file = new FileInputStream("src\\config.properties");
            property.load(file);
            if (property.getProperty(key) != null) {
                return property.getProperty(key);
            }
        } catch (FileNotFoundException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        } catch (IOException e) {
            System.err.println("ОШИБКА: не возможно получить доступ к файлу");
        }
        return "Ошибка";
    }
}
