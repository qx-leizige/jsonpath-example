package qx.leizige;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest extends BaseTest {


    @Test
    public void test() {

        String text = "/orderLine/1/item";

        String replacement = "/order_line_list/$1/item";

        String regex = "/orderLine/(\\d+)/item";

        String result = text.replaceAll(regex, replacement);
        System.out.println(result);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        matcher.find();//必须要有这句
        System.out.println("matcher.group(0) = " + matcher.group(0));
        System.out.println("matcher.group(1) = " + matcher.group(1));
    }
}
