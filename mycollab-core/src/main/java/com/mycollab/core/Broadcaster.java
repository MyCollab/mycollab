package com.mycollab.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class Broadcaster implements Serializable {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static LinkedList<BroadcastListener> listeners = new LinkedList<>();

    public static synchronized void register(BroadcastListener listener) {
        listeners.add(listener);
    }

    public static synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(final BroadcastMessage notification) {
        for (final BroadcastListener listener : listeners)
            executorService.execute(() -> listener.broadcast(notification));
    }
}
