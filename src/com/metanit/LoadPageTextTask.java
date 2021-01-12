package com.metanit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Queue;

public class LoadPageTextTask{
    private String url;
    private int depth;

    public int getDepth() {
        return depth;
    }

    public String getUrl() {
        return url;
    }

    public LoadPageTextTask(String url, int depth) {
        int index = url.indexOf('#');
        if (index != -1)
            url = url.substring(0, index);
        this.url = url;
        this.depth = depth;
    }

    /**
     * Loads html document from the url.
     * Finds all links in the document and pushes them to the queue
     * @param  queue  queue to push new loading tasks
     * @return      pair of url and its text
     */
    public Pair<String, String> process(Queue<LoadPageTextTask> queue) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.attr("abs:href");
            //no need in location.hash
            int index = href.indexOf('#');
            if (index != -1)
                href = href.substring(0, index);
            queue.add(new LoadPageTextTask(href, this.depth + 1));
        }
        String text = doc.body().text();
        if (text == null)
            return null;
        return new Pair<>(url, text);
    }
}
