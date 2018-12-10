/*
 * ============================================================================
 * Copyright (C) 2017 Kaltura Inc.
 *
 * Licensed under the AGPLv3 license, unless a different license for a
 * particular library is specified in the applicable library path.
 *
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/agpl-3.0.html
 * ============================================================================
 */

package com.kaltura.playkit;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.kaltura.playkit.plugins.ads.AdsListenerAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("WeakerAccess")
class DefaultMessageBus implements MessageBus {
    private static final String TAG = "MessageBus";
    public static final int INITIAL_LISTENER_CAPACITY = 10;

    private Handler postHandler = new Handler(Looper.getMainLooper());
    private Map<Object, Set<PKListener>> listeners;

    enum ListenerType {
        player, ads
    }

    DefaultMessageBus() {
        listeners = new ConcurrentHashMap<>();

        // Pre-allocate the sets for player and ads listeners.
        listeners.put(PlayerListener.class, new HashSet<>(INITIAL_LISTENER_CAPACITY));
        listeners.put(AdsListener.class, new HashSet<>(INITIAL_LISTENER_CAPACITY));

        // Forward new-style player events to legacy listeners
        addListener(new PlayerListenerAdapter(this));

        // Forward new-style ads events to legacy listeners
        addListener(new AdsListenerAdapter(this));
    }

    @Override
    public void postPlayerEvent(@NonNull PKMessage<PlayerListener> message) {
        final Set<PKListener> listeners = getListeners(ListenerType.player);
        if (listeners == null) {
            return;
        }
        post(() -> {
            for (Object listener : listeners) {
                if (listener instanceof PlayerListener) {
                    message.run(((PlayerListener) listener));
                }
            }
        });
    }

    @Override
    public void postAdsEvent(@NonNull PKMessage<AdsListener> message) {
        final Set<PKListener> listeners = this.getListeners(ListenerType.ads);
        if (listeners == null) {
            return;
        }
        post(() -> {
            for (Object listener : listeners) {
                if (listener instanceof AdsListener) {
                    message.run(((AdsListener) listener));
                }
            }
        });
    }

    @Override
    public void removeListener(@NonNull PKListener listener) {
        for (Set<PKListener> listenerSet : listeners.values()) {
            Iterator<PKListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == listener) {
                    iterator.remove();
                }
            }
        }
    }

    void post(Runnable runnable) {
        postHandler.post(runnable);
    }

    @Override
    public void post(@NonNull final PKEvent event) {

        final Set<PKListener> listeners = this.listeners.get(event.eventType());

        if (listeners == null) {
            return;
        }

        postHandler.post(() -> {
            for (PKListener listener : new HashSet<>(listeners)) {
                if (listener instanceof PKEvent.Listener) {
                    ((PKEvent.Listener) listener).onEvent(event);
                }
            }
        });
    }

    @Override
    public void remove(@NonNull PKEvent.Listener listener, @NonNull Enum... eventTypes) {
        for (Enum eventType : eventTypes) {
            Set<PKListener> listenerSet = listeners.get(eventType);
            if (listenerSet != null) {
                listenerSet.remove(listener);
            }
        }
    }

    @NonNull
    @Override
    public PKEvent.Listener listen(@NonNull PKEvent.Listener listener, @NonNull Enum... eventTypes) {
        for (Enum eventType : eventTypes) {
            Set<PKListener> listenerSet = listeners.get(eventType);
            if (listenerSet == null) {
                listenerSet = new HashSet<>();
                listenerSet.add(listener);
                listeners.put(eventType, listenerSet);
            } else {
                listenerSet.add(listener);
            }
        }
        return listener;
    }

    @Override
    public void addListener(@NonNull AdsListener listener) {
        getListeners(AdsListener.class).add(listener);
    }

    @Override
    public void addListener(@NonNull PlayerListener listener) {
        getListeners(PlayerListener.class).add(listener);
    }

    private Set<PKListener> getListeners(Object type) {
        return listeners.get(type);
    }
}
