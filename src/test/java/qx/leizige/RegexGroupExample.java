package qx.leizige;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leizige
 */
public class RegexGroupExample {
    public static void main(String[] args) {


        String regex = "(\\d+)@163.com";

        String str = "12343434@163.com";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(str);
        matcher.find();

        System.out.println(matcher.group(0));
        System.out.println(matcher.group(1));
    }
}
