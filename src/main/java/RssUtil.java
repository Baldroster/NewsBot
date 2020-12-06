import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssUtil {

    private static Document getRss(String Host) throws ParserConfigurationException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = null;


        try {
            HttpResponse<String> response = Unirest.get(Host + "/rss")
                    .asString();
            if (response.getStatus() < 400) {

                //Дебаг
                System.out.println(response.getBody());

                InputSource src = new InputSource();
                src.setCharacterStream(new StringReader(response.getBody()));
                doc = builder.parse(src);
            } else {
                throw new AssertionError("Unsupported response status code " + response.getStatus());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new AssertionError("Unsupported action in http request");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return doc;

    }

    private static List<News> createNewsList(Document doc, String pattern) {
        List<News> newsList = new ArrayList<>();

        NodeList newsNodes = doc.getElementsByTagName("item");
        for (int i = 0; i < newsNodes.getLength(); i++) {
            News news = new News(newsNodes.item(i));
            if (compareNode(news, pattern)) {
                newsList.add(news);
            }

        }
        return newsList;
    }


    public static List<News> getNews(String text) {
        String jsonConf = null;
        try {
            jsonConf = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\sites.json")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<News> news = new ArrayList<>();
        List<String> sites = new Gson().fromJson(jsonConf,Sites.class).urls;
        for (int i = 0; i < sites.size(); i++) {
            try {
                news.addAll(createNewsList(getRss(sites.get(i)), text));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }


        return news;

    }

    private static boolean compareNode(News news, String pattern) {
        List<String> patterns = DeclensionUtil.getDeclensionWords(pattern);

        String text = news.getTitle().toLowerCase().trim() + " " + news.getDescription().toLowerCase().trim();
        for (int i = 0; i < patterns.size(); i++) {
            Matcher matcher = Pattern.compile(patterns.get(i)).matcher(text);
            if (!matcher.find()) {
                return false;
            }
        }
        return true;
    }
}
