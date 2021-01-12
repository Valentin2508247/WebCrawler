package com.metanit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountWordsInTextTask {

    /**
     * Loads html document from the url.
     * Finds all links in the document and pushes them to the queue
     * @param  text  text
     * @param  word  word to find in the text
     * @return count of given word in the text
     */
    public static int wordCount(String text, String word){
        int count = 0;
        text = text.toLowerCase();
        Pattern pattern = Pattern.compile(word.toLowerCase());
        Matcher matcher = pattern.matcher(text);
        while (matcher.find())
            count++;
        return count;
    }
}
