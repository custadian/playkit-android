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

package com.kaltura.playkit.plugins.ads;

import com.kaltura.playkit.PKError;
import com.kaltura.playkit.PKEvent;

/**
 * @deprecated Please use {@link com.kaltura.playkit.AdsListener} with {@link com.kaltura.playkit.Player#addAdsListener(com.kaltura.playkit.AdsListener)} instead.
 */
@Deprecated
public class AdEvent implements PKEvent {

    public Type type;

    AdEvent(Type type) {
        this.type = type;
    }

    public static class AdLoadedEvent extends AdEvent {

        public final AdInfo adInfo;

        AdLoadedEvent(AdInfo adInfo) {
            super(Type.LOADED);
            this.adInfo = adInfo;
        }
    }

    public static class AdStartedEvent extends AdEvent {

        public final AdInfo adInfo;

        AdStartedEvent(AdInfo adInfo) {
            super(Type.STARTED);
            this.adInfo = adInfo;
        }
    }

    public static class AdPausedEvent extends AdEvent {

        public final AdInfo adInfo;

        AdPausedEvent(AdInfo adInfo) {
            super(Type.PAUSED);
            this.adInfo = adInfo;
        }
    }

    public static class AdResumedEvent extends AdEvent {

        public final AdInfo adInfo;

        AdResumedEvent(AdInfo adInfo) {
            super(Type.RESUMED);
            this.adInfo = adInfo;
        }
    }

    public static class AdSkippedEvent extends AdEvent {

        public final AdInfo adInfo;

        AdSkippedEvent(AdInfo adInfo) {
            super(Type.SKIPPED);
            this.adInfo = adInfo;
        }
    }

    public static class AdCuePointsUpdateEvent extends AdEvent {

        public final AdCuePoints cuePoints;

        AdCuePointsUpdateEvent(AdCuePoints cuePoints) {
            super(Type.CUEPOINTS_CHANGED);
            this.cuePoints = cuePoints;
        }
    }

    public static class AdPlayHeadEvent extends AdEvent {

        public final long adPlayHead;

        AdPlayHeadEvent(long adPlayHead) {
            super(Type.PLAY_HEAD_CHANGED);
            this.adPlayHead = adPlayHead;
        }
    }

    public static class AdRequestedEvent extends AdEvent {

        public final String adTagUrl;

        AdRequestedEvent(String adTagUrl) {
            super(Type.AD_REQUESTED);
            this.adTagUrl = adTagUrl;
        }
    }

    public static class AdBufferStart extends AdEvent {

        public final long adPosition;

        AdBufferStart(long adPosition) {
            super(Type.AD_BUFFER_START);
            this.adPosition = adPosition;
        }
    }

    public static class AdBufferEnd extends AdEvent {

        public final long adPosition;

        AdBufferEnd(long adPosition) {
            super(Type.AD_BUFFER_END);
            this.adPosition = adPosition;
        }
    }

    public static class AdPlaybackInfoUpdated extends AdEvent {

        public final int width;
        public final int height;
        public final int bitrate;

        public AdPlaybackInfoUpdated(int width, int height, int bitrate) {
            super(Type.AD_PLAYBACK_INFO_UPDATED);
            this.width = width;
            this.height = height;
            this.bitrate = bitrate;
        }
    }

    public static class Error extends AdEvent {

        public final PKError error;

        Error(PKError error) {
            super(Type.ERROR);
            this.error = error;
        }
    }

    public enum Type {
        AD_REQUESTED,
        AD_FIRST_PLAY,
        STARTED,
        AD_DISPLAYED_AFTER_CONTENT_PAUSE,
        PAUSED,
        RESUMED,
        COMPLETED,
        FIRST_QUARTILE,
        MIDPOINT,
        THIRD_QUARTILE,
        SKIPPED,
        SKIPPABLE_STATE_CHANGED,
        CLICKED,
        TAPPED,
        ICON_TAPPED,
        AD_BREAK_READY,
        AD_PROGRESS,
        AD_BREAK_STARTED,
        AD_BREAK_ENDED,
        AD_BREAK_IGNORED,
        CUEPOINTS_CHANGED,
        PLAY_HEAD_CHANGED,
        LOADED,
        CONTENT_PAUSE_REQUESTED,
        CONTENT_RESUME_REQUESTED,
        ALL_ADS_COMPLETED,
        AD_LOAD_TIMEOUT_TIMER_STARTED,
        AD_BUFFER_START,
        AD_BUFFER_END,
        AD_PLAYBACK_INFO_UPDATED,
        ERROR
    }


    @Override
    public Enum eventType() {
        return this.type;
    }
}
