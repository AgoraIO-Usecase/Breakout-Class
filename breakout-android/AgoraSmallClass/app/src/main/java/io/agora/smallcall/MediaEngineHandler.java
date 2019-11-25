package io.agora.smallcall;

import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcChannelEventHandler;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcChannel;

public class MediaEngineHandler {

    private ConcurrentHashMap<Integer, IMediaEngineHandler.MainChannel> mainHandlers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, IMediaEngineHandler.SubChannel> subHandlers = new ConcurrentHashMap<>();

    public void addMainEventHandler(IMediaEngineHandler.MainChannel handler) {
        mainHandlers.put(0, handler);
    }

    public void removeMainEventHandler(IMediaEngineHandler.MainChannel handler) {
        mainHandlers.remove(0);
    }

    public void addSubEventHandler(IMediaEngineHandler.SubChannel handler) {
        subHandlers.put(0, handler);
    }

    public void removeSubEventHandler(IMediaEngineHandler.SubChannel handler) {
        subHandlers.remove(0);
    }


    final IRtcEngineEventHandler mainEngineEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            Log.e("wbsTest-->", "success");
            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onJoinMainChannelSuccess(channel, uid, elapsed);
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Log.e("wbsTest-->", "main joined:" + uid);
            super.onUserJoined(uid, elapsed);
            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onMainUserJoined(uid, elapsed);
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.e("wbsTest-->", "error:" + err);

            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onMainError(err);
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            super.onUserOffline(uid, reason);
            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onMainUserOffline(uid, reason);
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onMainLeaveChannel(stats);
        }

        @Override
        public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
            super.onFirstRemoteVideoFrame(uid, width, height, elapsed);

            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onMainFirstRemoteVideoFrame(uid, width, height, elapsed);
        }

        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
            super.onFirstLocalVideoFrame(width, height, elapsed);

            if (!mainHandlers.isEmpty())
                mainHandlers.values().iterator().next().onMainFirstLocalVideoFrame(width, height, elapsed);

        }
    };


    final IRtcChannelEventHandler channelEventHandler = new IRtcChannelEventHandler() {
        @Override
        public void onJoinChannelSuccess(RtcChannel rtcChannel, int uid, int elapsed) {
            super.onJoinChannelSuccess(rtcChannel, uid, elapsed);
            Log.e("wbsTest-->", "success");
            if (!subHandlers.isEmpty())
                subHandlers.values().iterator().next().onJoinSubChannelSuccess(rtcChannel.channelId(), uid, elapsed);
        }

//        @Override
//        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
//            super.onJoinChannelSuccess(channel, uid, elapsed);
//            Log.e("wbsTest-->", "success");
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onJoinSubChannelSuccess(channel, uid, elapsed);
//        }


        @Override
        public void onUserJoined(RtcChannel rtcChannel, int uid, int elapsed) {
            super.onUserJoined(rtcChannel, uid, elapsed);
            Log.e("wbsTest-->", "subjoined:" + uid);
            if (!subHandlers.isEmpty())
                subHandlers.values().iterator().next().onSubUserJoined(rtcChannel.channelId(), uid, elapsed);
        }

//        @Override
//        public void onUserJoined(int uid, int elapsed) {
//            Log.e("wbsTest-->", "subjoined:" + uid);
//            super.onUserJoined(uid, elapsed);
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onSubUserJoined(uid, elapsed);
//        }


        @Override
        public void onChannelError(RtcChannel rtcChannel, int err) {
            super.onChannelError(rtcChannel, err);
            Log.e("wbsTest-->", "error:" + err);

            if (!subHandlers.isEmpty())
                subHandlers.values().iterator().next().onSubError(err);
        }

//        @Override
//        public void onError(int err) {
//            super.onError(err);
//            Log.e("wbsTest-->", "error:" + err);
//
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onSubError(err);
//        }


        @Override
        public void onUserOffline(RtcChannel rtcChannel, int uid, int reason) {
            super.onUserOffline(rtcChannel, uid, reason);
            if (!subHandlers.isEmpty())
                subHandlers.values().iterator().next().onSubUserOffline(uid, reason);
        }

//        @Override
//        public void onUserOffline(int uid, int reason) {
//            super.onUserOffline(uid, reason);
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onSubUserOffline(uid, reason);
//        }


        @Override
        public void onLeaveChannel(RtcChannel rtcChannel, IRtcEngineEventHandler.RtcStats stats) {
            super.onLeaveChannel(rtcChannel, stats);
            Log.e("wbsTest-->", "leave sub channel!");
            if (!subHandlers.isEmpty())
                subHandlers.values().iterator().next().onSubLeaveChannel(stats);
        }

//        @Override
//        public void onLeaveChannel(RtcStats stats) {
//            super.onLeaveChannel(stats);
//
//            Log.e("wbsTest-->", "leave sub channel!");
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onSubLeaveChannel(stats);
//        }


        @Override
        public void onFirstRemoteVideoFrame(RtcChannel rtcChannel, int uid, int width, int height, int elapsed) {
            super.onFirstRemoteVideoFrame(rtcChannel, uid, width, height, elapsed);
            if (!subHandlers.isEmpty())
                subHandlers.values().iterator().next().onSubFirstRemoteVideoFrame(uid, width, height, elapsed);
        }

//        @Override
//        public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
//            super.onFirstRemoteVideoFrame(uid, width, height, elapsed);
//
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onSubFirstRemoteVideoFrame(uid, width, height, elapsed);
//        }


//        @Override
//        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
//            super.onFirstLocalVideoFrame(width, height, elapsed);
//
//            if (!subHandlers.isEmpty())
//                subHandlers.values().iterator().next().onSubFirstLocalVideoFrame(width, height, elapsed);
//
//        }
    };
}
