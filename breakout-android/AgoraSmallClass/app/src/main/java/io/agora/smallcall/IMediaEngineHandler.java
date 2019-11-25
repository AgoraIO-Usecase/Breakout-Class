package io.agora.smallcall;

import io.agora.rtc.IRtcEngineEventHandler;

public interface IMediaEngineHandler {
    interface MainChannel {
        void onJoinMainChannelSuccess(String channel, int uid, int elapsed);

        void onMainUserJoined(int uid, int elapsed);

        void onMainError(int err);

        void onMainUserOffline(int uid, int reason);

        void onMainLeaveChannel(IRtcEngineEventHandler.RtcStats stats);

        void onMainFirstRemoteVideoFrame(int uid, int width, int height, int elapsed);

        void onMainFirstLocalVideoFrame(int width, int height, int elapsed);
    }

    interface SubChannel {
        void onJoinSubChannelSuccess(String channel, int uid, int elapsed);

        void onSubUserJoined(String channel, int uid, int elapsed);

        void onSubError(int err);

        void onSubUserOffline(int uid, int reason);

        void onSubLeaveChannel(IRtcEngineEventHandler.RtcStats stats);

        void onSubFirstRemoteVideoFrame(int uid, int width, int height, int elapsed);

        void onSubFirstLocalVideoFrame(int width, int height, int elapsed);
    }
}
