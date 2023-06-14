package Image;

import Config.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Image {
    public String generateImages(String[] weather) {
        BufferedImage image;
        BufferedImage imageWeather;
        Config config = new Config();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm"); //Задаем формат даты
            Date date = new Date();
            DataInputStream inputFile = new DataInputStream(new FileInputStream(config.getConfig("pathImageExample")));//Исходаная картинка
            DataOutputStream outputFile = new DataOutputStream(new FileOutputStream(config.getConfig("PathImageOutput")));//Картинка отредактированая
            URL url = new URL(config.getConfig("getIcon") + weather[7] + "@2x.png");//Получаем иконку
            BufferedInputStream clouds = new BufferedInputStream(url.openStream());

            image = ImageIO.read(inputFile);//Октроем файл шаблон
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            Graphics2D g2 = image.createGraphics();

            g2.setColor(Color.white);//Установим цвет шрифта
            g2.setFont(new Font("Helvetica", Font.BOLD, 36));// Установим шрифт 36 для вспомогательного текста
            g2.drawString(dateFormat.format(date), 36, 48);//Дата и время
            g2.drawString(weather[2], 200, 300);//Статус погоды
            int pressure = (int) (Integer.valueOf(weather[8]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString(String.valueOf(pressure), 119, 470);//Давление
            g2.drawImage(imageWeather, 5, 180, 240, 240, null);//Иконка погоды

            g2.setFont(new Font("Helvetica", Font.BOLD, 80)); //Установим шрифт 80 для основного текста
            g2.drawString(weather[0], 460, 80);//Город
            g2.drawString(weather[5].substring(0, 2), 490, 495);//Температура текущая
            g2.drawString(weather[6].substring(0, 2), 490, 660);//Влажнось текущая

            ImageIO.write(image, "PNG", outputFile); //Запишем модифицированный файл
            //Закроем файлы
            outputFile.close();
            inputFile.close();
            clouds.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return config.getConfig("PathImageOutput");
    }

    public String genImageTheeDays(String[][] weather) {
        BufferedImage image;
        BufferedImage imageWeather;
        Config config = new Config();
        try {
            long epoch;
            String date;
            int pressure, block, x, y;
            URL url;
            BufferedInputStream clouds;

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm"); //Задаем формат даты
            Date dateCurrent = new Date();
            String dateCurrentStr = dateFormat.format(dateCurrent);//Просто так надо

            DataInputStream inputFile = new DataInputStream(new FileInputStream(config.getConfig("pathImageTheeDayExample")));//Исходаная картинка
            DataOutputStream outputFile = new DataOutputStream(new FileOutputStream(config.getConfig("PathImageOutput")));//Картинка отредактированая


            image = ImageIO.read(inputFile);//Октроем файл шаблон

            Graphics2D g2 = image.createGraphics();
            //Нижний блок
            g2.setFont(new Font("Helvetica", Font.BOLD, 36));
            g2.setColor(Color.white);
            g2.drawString(dateCurrentStr, 36, 48);//Дата и время запроса
            g2.setFont(new Font("Helvetica", Font.BOLD, 50));
            g2.drawString(weather[0][2], 460, 60);//Город

            block = 0;//Номер блока
            x = 100;// Смещение по х
            y = 0;//Смещение по y
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 1;
            x = 400;
            y = 0;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 2;
            x = 700;
            y = 0;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 3;
            x = 100;
            y = 250;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 4;
            x = 400;
            y = 250;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 5;
            x = 700;
            y = 250;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 6;
            x = 100;
            y = 500;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 7;
            x = 400;
            y = 500;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            block = 8;
            x = 700;
            y = 500;
            g2.setFont(new Font("Helvetica", Font.BOLD, 20));
            epoch = Long.valueOf(weather[block][1]);
            date = new java.text.SimpleDateFormat("dd.MM HH:mm").format(new java.util.Date(epoch * 1000));// Получаем время по Москве
            url = new URL(config.getConfig("getIcon") + weather[block][6] + "@2x.png");//Получаем иконку
            clouds = new BufferedInputStream(url.openStream());
            imageWeather = ImageIO.read(clouds);//Откроем иконку
            g2.drawImage(imageWeather, x, 100 + y, 128, 128, null);//Иконка погоды
            clouds.close();
            g2.drawString(date, x, 115 + y);//Время
            g2.drawString(weather[block][3], x, 210 + y);//Статус погоды
            pressure = (int) (Integer.valueOf(weather[block][7]) / 1.333);//Перевод hPa в мм.рт.ст
            g2.drawString("Давление: " + pressure + "мм.", x, 240 + y);//Давление
            g2.drawString("Температура: " + weather[block][4].substring(0, 2) + " C", x, 270 + y);//Температура
            g2.drawString("Влажность: " + weather[block][5].substring(0, 2) + " %", x, 300 + y);//Влажнось

            ImageIO.write(image, "PNG", outputFile); //Запишем модифицированный файл
            //Закроем все файлы
            outputFile.close();
            inputFile.close();
            clouds.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return config.getConfig("PathImageOutput");
    }

}
