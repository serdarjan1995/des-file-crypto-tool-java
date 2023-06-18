import java.util.ArrayList;

public class StringUtils {

    public static ArrayList<String> generateWordlist(char[] characters, int length) {
        ArrayList<String> wordlist = new ArrayList<>();
        String s = String.valueOf(characters[0]).repeat(length);
        String charString = new String(characters);
        boolean finished = false;
        int i = 1;
        int nextCharIndex;
        while (!finished) {
            for (char character : characters) {
                s = s.substring(0, length - 1) + character;
                wordlist.add(s);
            }
            s = s.substring(0, length - 1) + characters[0];
            if (i < length) {
                nextCharIndex = charString.indexOf(s.charAt(length - i - 1));

                if (nextCharIndex >= characters.length - 1) {
                    i++;
                    nextCharIndex = charString.indexOf(s.charAt(length - i - 1));
                    if (nextCharIndex < characters.length - 1) {
                        s = s.substring(0, length - i - 1) + characters[nextCharIndex + 1] + Character.toString(characters[0]).repeat(length - 1);
                        i = 1;
                    }
                } else if (nextCharIndex < characters.length - 1) {
                    s = s.substring(0, length - i - 1) + characters[nextCharIndex + 1] + s.substring(length - i);
                }
                if (i >= length - 1) {
                    finished = true;
                }
            }
        }

        return wordlist;
    }

}
