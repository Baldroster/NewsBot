import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.UUID;


public class News {
    private String title;
    private String description;
    private String link;
    private String date;


    News(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 1; i < nodeList.getLength() - 1; i++) {
            Node nd = nodeList.item(i);
            switch (nd.getNodeName()) {
                case "title":
                    this.title = nd.getFirstChild().getTextContent().trim();
                    break;
                case "description":


                    this.description = nd.hasChildNodes() ? nd.getTextContent().replaceFirst("<м>", "").replaceFirst("<.*>", "").replaceAll("<.*>.*", "").trim()
                            : nd.getFirstChild().getTextContent()
                            .replaceFirst("<.*>", "").replaceFirst("<.*>", "").replaceAll("<.*>.*", "").trim();

                    break;
                case "link":
                    this.link = nd.getTextContent().trim();
                    break;
                case "pubDate":
                    this.date = convertDateFormat(nd.getTextContent()).trim();
                    break;

            }

        }


    }

    private String convertDateFormat(String inDate) {
        try {
            return LocalDateTime.parse(inDate, DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z").withLocale(Locale.US)).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(inDate, DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss z").withLocale(Locale.US)).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
    }


    public String getNews() {
        return "✉" + title + "✉\n\n"
                + description +
                "\n\n>Ссылка: " + link +
                "\n\n>Дата написания: "
                + date + "\n";
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
