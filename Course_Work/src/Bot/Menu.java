package Bot;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Menu extends Bot {
    public void staticCommand(String chatID, String command, Optional<Session> optional) {
        SendMessage message = new SendMessage();
        Send send = new Send();
        String text;
        //Обработка статичных команд через слеш и отвечает тому же человеку
        switch (command) {
            case ("/start") -> {
                message.setChatId(chatID);
                message.setText("На данный момент бот умеет отправлять текущую температуру и прогноз на три дня");
                send.sendMessage(message);
                sendInLineMenu(chatID);
            }
            case ("/menu") -> {
                sendInLineMenu(chatID);
            }
            case ("/weatherCurrent") -> {
                optional.get().setAttribute("menu", "CurrentСityQuestion");
                message.setChatId(chatID);
                message.setText("Погода сейчас. \n" +
                        "Введите город:");
                send.sendMessage(message);

            }
            case ("/weatherThreeDay") -> {
                optional.get().setAttribute("menu", "weatherThreeDay");
                message.setChatId(chatID);
                message.setText("Прогноз на три дня. \n" +
                        "Введите город:");
                send.sendMessage(message);
            }
            default -> {
                message.setChatId(chatID);
                message.setText("Неизвестная команда");
                send.sendMessage(message);
            }
        }
    }

    public void sendInLineMenu(String chatID) {
        SendMessage message = new SendMessage();
        Send send = new Send();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inLineBT1 = new InlineKeyboardButton();
        inLineBT1.setText("Погода сейчас");
        inLineBT1.setCallbackData("/weatherCurrent");

        InlineKeyboardButton inLineBT2 = new InlineKeyboardButton();
        inLineBT2.setText("Прогноз на три дня");
        inLineBT2.setCallbackData("/weatherThreeDay");

        List<InlineKeyboardButton> btRow1 = new ArrayList<>();
        btRow1.add(inLineBT1);
        btRow1.add(inLineBT2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(btRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        message.setReplyMarkup(inlineKeyboardMarkup);
        message.setChatId(chatID);
        message.setText("Погода: ");
        send.sendMessage(message);
    }
}
