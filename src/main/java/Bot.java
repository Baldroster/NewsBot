
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.List;


public class Bot extends TelegramLongPollingBot {
    public String getBotUsername() {
        return "";
    }

    public String getBotToken() {
        return "";
    }

    public void onUpdateReceived(Update update) {
        if(update.getMessage().getText().toLowerCase().trim().equals("/help")||update.getMessage().getText().toLowerCase().trim().equals("/start")){
            sendMsg(update, "Данный бот ищет новости по новостным сайтам. Для того чтобы произвести поиск введите интересующие вас ключевые слова через пробел");
        }else {
            List<News> newsList = RssUtil.getNews(update.getMessage().getText().toLowerCase());
            newsList.forEach(news -> {

                sendMsg(update, news.getNews());
            });
        }
    }

    public synchronized void sendMsg(Update update, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setText(text);
        sendMsg.setChatId(update.getMessage().getChatId().toString());
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
