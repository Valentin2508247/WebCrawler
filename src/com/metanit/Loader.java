package com.metanit;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Loader implements Runnable {
    Queue<LoadPageTextTask> queue;
    Queue<Pair<String, String>> pageText;
    private Map<String, Boolean> visited;
    private AtomicInteger count;
    private int maxPages;
    private int maxDepth;

    /**
     * Class runs asynchronously
     *
     * @param  queue  queue of tasks to load
     * @param  pageText resulting queue of urls and their texts
     * @param  visited urls that have already been visited
     * @param  count total pages loaded
     * @param  maxPages max pages to load
     * @param  maxDepth max depth to go
     */
    public Loader(Queue<LoadPageTextTask> queue, Queue<Pair<String, String>> pageText, Map<String, Boolean> visited, AtomicInteger count, int maxPages, int maxDepth) {
        this.queue = queue;
        this.pageText = pageText;
        this.visited = visited;
        this.count = count;
        this.maxPages = maxPages;
        this.maxDepth = maxDepth;
    }

    /**
     * Class runs asynchronously.
     * It takes urls to load from the queue
     * and adds their content to the result queue
     */
    @Override
    public void run() {
        LoadPageTextTask task;
        while (queue.size() > 0){
            task = queue.poll();
            if (count.incrementAndGet() > maxPages)
                return;
            if (task.getDepth() > maxDepth)
                return;
            String url = task.getUrl();
            if (visited.containsKey(url))
                continue;
            else
                visited.put(url, true);
            try {
                Pair<String, String> pair = task.process(queue);
                if (pair != null)
                    pageText.add(pair);
            }
            catch (Exception ex){
                continue;
            }
        }
    }
}
