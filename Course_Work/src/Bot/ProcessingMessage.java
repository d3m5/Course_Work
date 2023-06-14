package Bot;

import Image.Image;
import Parse.Parse;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessingMessage extends Bot {
    public void incomingMessage(Update update, Optional<Session> optional) {

        //Блок обработки сообщений из личных чатов
        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatID = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            Pattern staticCommand = Pattern.compile("^" + "/" + ".*"); //Регулярное выражение
            Matcher matcher = staticCommand.matcher(text); //Передаем строку входящего сообщения
            //Обработка статичных команд через слеш.
            // Если выражение найдено создаем объект и передаем в него сообщение
            if (matcher.find()) {
                Menu command = new Menu();
                command.staticCommand(chatID, text, optional);
            }
            if (optional.get().getAttributeKeys().size() > 0) {
                if (optional.get().getAttribute("menu").equals("CurrentСityQuestion")) {
                    sendWeather(chatID, text, 1, optional);
                } else if (optional.get().getAttribute("menu").equals("weatherThreeDay")) {
                    sendWeather(chatID, text, 2, optional);
                }
            } else {
                System.out.println(update.getMessage().getChat().getFirstName()
                        + " пишет: " +
                        update.getMessage().getText());
            }
        }
        //Обработка обратных заросов
        if (update.hasCallbackQuery()) {
            String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
            String text = update.getCallbackQuery().getData();

            Pattern staticCommand = Pattern.compile("^" + "/" + ".*"); //Регулярное выражение
            Matcher matcher = staticCommand.matcher(text); //Передаем строку входящего сообщения
            //Обработка статичных команд через слеш.
            // Если выражение найдено создаем объект и передаем в него сообщение
            if (matcher.find()) {
                Menu command = new Menu();
                command.staticCommand(chatID, text, optional);
            }
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
            Send send = new Send();
            send.sendMessage(answerCallbackQuery);
        }
    }

    public void sendWeather(String chatID, String city, int variant, Optional<Session> optional) {
        Parse parse = new Parse();
        Image image = new Image();
        SendMessage message = new SendMessage();
        Send send = new Send();
        SendChatAction chatAction = new SendChatAction();

        SendPhoto sendPhoto = new SendPhoto();
        if (variant == 1) {
            String[] weatherCurrent = parse.getWeatherOneDay(city);
            if (weatherCurrent[0].equals("404")) {
                message.setChatId(chatID);
                message.setText("Город "
                        + weatherCurrent[1]
                        + " не найден, попробуйте уточнить.");
                send.sendMessage(message);

            } else {
                chatAction.setChatId(chatID);
                chatAction.setAction(ActionType.TYPING);
                send.sendMessage(chatAction);

                sendPhoto.setPhoto(new InputFile(new File(image.generateImages(weatherCurrent))));
                sendPhoto.setChatId(chatID);
                send.sendMessage(sendPhoto);
                optional.get().stop(); //Закроем сессию

                System.out.println("id: "
                        + chatID
                        + " получил погоду по городу "
                        + city);
            }
        } else if (variant == 2) {
            String[][] weather = parse.getWeatherTheeDay(city);
            if (weather[0][0].equals("404")) {
                message.setChatId(chatID);
                message.setText("Город ,"
                        + weather[0][1]
                        + " не найден, попробуйте уточнить.");
                send.sendMessage(message);
            } else {
                chatAction.setChatId(chatID);
                chatAction.setAction(ActionType.TYPING);
                send.sendMessage(chatAction);

                sendPhoto.setPhoto(new InputFile(new File(image.genImageTheeDays(weather))));
                sendPhoto.setChatId(chatID);
                send.sendMessage(sendPhoto);
                optional.get().stop(); //Закроем сессию

                System.out.println("id: "
                        + chatID
                        + " получил погоду по городу "
                        + city);
            }
        }
    }
}
