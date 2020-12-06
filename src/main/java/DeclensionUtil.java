import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeclensionUtil {
    static final String wordEndings = "ой|ый|ого|ому|ым|ом|ий|его|ему|им|ем|ая|ую|яя|ей|юю|ую|ое|ее|ые|ых|ыми|их|ие|ими|ёй|ам|ём|ям|ою|ею|ью|ов|ами|ями|о|ах|ях|а|я|ы|и|е|у|ю|ь|";
static final String signs = "|\\\\.|,|\\?|:|!|-|;";
    public static List<String> getDeclensionWords(String text) {
        String[] words = text.toLowerCase().split(" ");
        List<String> patterns = new ArrayList<>();
        for (String word : words) {

            patterns.add(word.length() > 1 ?"(\\s|^"+signs+ ")" + word.replaceAll("(" + wordEndings + ")$", "(" + wordEndings + ")") + "(\\s"+signs+ ")" :"(\\s|^"+signs+ ")" + word+ "(\\s"+signs+ ")");

        }

        return patterns;
    }


}
