package com.metanit;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Config {
    String startUrl;
    String[] words;
    int maxPages;
    int maxDepth;
    int topN;

    public Config(String startUrl, String[] words, int maxPages, int maxDepth, int topN) {
        this.startUrl = startUrl;
        this.words = words;
        this.maxPages = maxPages;
        this.maxDepth = maxDepth;
        this.topN = topN;
    }

    /**
     * Loads data from file.
     * File should be:
     * string startUrl
     * string1, string2, ...
     * int maxPagesCount
     * int maxDepth
     * int topN
     */
    public static Config fromFile(String filename) throws IOException {
        Scanner scanner = new Scanner(new File(filename));
        String startUrl = scanner.nextLine();
        String line = scanner.nextLine();
        String[] words = line.split(", ");
        int maxPages = scanner.nextInt();
        int maxDepth = scanner.nextInt();
        int topN = scanner.nextInt();
        return new Config(startUrl, words, maxPages, maxDepth, topN);
    }
}
