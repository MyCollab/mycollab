/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class NotificationBroadcaster implements Serializable {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface BroadcastListener {
        void broadcastNotification(AbstractNotification notification);

        void removeNotification(AbstractNotification notification);
    }

    private static Collection<AbstractNotification> globalNotifications = new ArrayList<>();

    private static LinkedList<BroadcastListener> listeners = new LinkedList<>();

    public static synchronized void register(BroadcastListener listener) {
        listeners.add(listener);
        for (AbstractNotification notification : globalNotifications) {
            listener.broadcastNotification(notification);
        }
    }

    public static void removeGlobalNotification(Class<?> notificationCls) {
        Iterator<AbstractNotification> iter = globalNotifications.iterator();
        while (iter.hasNext()) {
            final AbstractNotification notification = iter.next();
            if (notification.getClass() == notificationCls) {
                iter.remove();
                for (final BroadcastListener listener : listeners)
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.removeNotification(notification);
                        }
                    });
            }
        }
    }

    public static synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(final AbstractNotification notification) {
        if (notification.isGlobalScope() && !globalNotifications.contains(notification)) {
            globalNotifications.add(notification);
        }
        for (final BroadcastListener listener : listeners)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.broadcastNotification(notification);
                }
            });
    }
}
