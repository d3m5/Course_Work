package Bot;

import Config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
import org.apache.shiro.session.Session;

import java.util.Optional;

public class Bot extends TelegramLongPollingSessionBot {
    Config config = new Config();

    public void onUpdateReceived(Update update, Optional<Session> optional) {
        Logger logger = LoggerFactory.getLogger(Bot.class);
        ProcessingMessage processingMessage = new ProcessingMessage();
        // Ждем входящее сообщение
        if (update.hasMessage() && update.getMessage().hasText()) {
            processingMessage.incomingMessage(update, optional);
        } else if (update.hasCallbackQuery()) {
            processingMessage.incomingMessage(update, optional);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getConfig("BotName");
    }

    @Override
    public String getBotToken() {
        return config.getConfig("BotToken");
    }
}
