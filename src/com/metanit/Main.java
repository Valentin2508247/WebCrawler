package com.metanit;

import java.io.PrintWriter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config;
        try{
            config = Config.fromFile("config.txt");
        }
        catch (Exception ex){
            System.out.println("Error parsing config file.");
            return;
        }
        WebCrawler crawler = new WebCrawler(config.maxPages,config.maxDepth, config.startUrl, 8);
        List<PageWordsCount> list;
        try{
            // url: words count list
            list =  crawler.process(config.words);
        }
        catch (Exception ex){
            System.out.println("Error occurred while loading starting url. Check your internet connection and starting url.");
            return;
        }
        String csv = getCSV(list, list.size());
        // pages statistics
        writeToFile(csv, "output.csv");
        list.sort((object1, object2) -> -object1.getTotal() + object2.getTotal());
        // top n
        csv = getCSV(list, config.topN);
        System.out.println(csv);
        writeToFile(csv, "top.csv");
    }

    public static void writeToFile(String text, String file){
        try (PrintWriter out = new PrintWriter(file)) {
            out.print(text);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static String getCSV(List<PageWordsCount> list, int count){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (PageWordsCount element: list){
            if (i >= count)
                break;
            sb.append(element.toString());
            sb.append('\n');
            i++;
        }
        return sb.toString();
    }
}

