package com.metanit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawler {
    private int maxPages;
    private int maxDepth;
    private String startUrl;
    private int threadCount;

    public WebCrawler(int maxPages, int maxDepth, String startUrl, int threadCount) {
        this.maxPages = maxPages;
        this.maxDepth = maxDepth;
        this.startUrl = startUrl;
        this.threadCount = threadCount;
    }

    /**
     * Method loads pages and
     * counts specified words on them
     * @param  words  words to find on the internet pages
     * @return list of pages urls and count of words found on the page
     */
    public List<PageWordsCount> process(String[] words) throws Exception{
        // queue of pairs url and its text
        Queue<Pair<String, String>> queue = loadPages();
        List<PageWordsCount> pageWordsList = new ArrayList<>();
        Map<String, Boolean> visited = new HashMap<>();
        while (queue.size() > 0){
            Pair<String, String> urlText = queue.poll();
            String url = urlText.key;
            if (visited.containsKey(url))
                continue;
            else
                visited.put(url, true);
            String text = urlText.value;
            PageWordsCount pageWords = new PageWordsCount(url);
            for (int i = 0; i < words.length; i++)
            {
                String word = words[i];
                int count = CountWordsInTextTask.wordCount(text, word);
                pageWords.addValue(count);
            }
            pageWordsList.add(pageWords);
        }
        return pageWordsList;
    }

    /**
     * Loads pages asynchronously,
     * starting from the startUrl
     * @return queue of pairs url: its text
     */
    private Queue<Pair<String, String>> loadPages() throws Exception{
        Queue<LoadPageTextTask> queue = new ConcurrentLinkedQueue<>();
        Queue<Pair<String, String>> pageText = new ConcurrentLinkedQueue<>();
        LoadPageTextTask task = new LoadPageTextTask(startUrl, 1);
        // if bad url might throw exception
        pageText.add(task.process(queue));
        // starting threads
        List<Thread> threads = new ArrayList<>();
        Map<String, Boolean> visited = new ConcurrentHashMap<String, Boolean>();
        AtomicInteger count = new AtomicInteger(1);
        for (int i = 0; i < threadCount; i++){
            Thread thread = new Thread(new Loader(queue, pageText, visited, count, maxPages, maxDepth), "thread" + i);
            thread.start();
            threads.add(thread);
        }
        for (int i = 0; i < threadCount; i++){
            try{
                threads.get(i).join();
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return pageText;
    }
}
