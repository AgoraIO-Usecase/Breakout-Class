package io.agora.smallcall;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcChannel;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.ChannelMediaOptions;
import io.agora.rtc.video.VideoCanvas;

public class WorkThread extends Thread {
    private final static String TAG = WorkThread.class.getName();

    private Context mContext;

    private RtcEngine mRtcEngine;
    private RtcChannel mRtcChannel;
    private WorkHandler mWorkHandler;
    private MediaEngineHandler mMediaEngineHandler;

    private boolean isThreadReady = false;

    private static class WorkHandler extends Handler {
        private WorkThread workThread;

        public WorkHandler(WorkThread wt) {
            this.workThread = wt;
        }

        public void release() {
            workThread = null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SConstants.HANDLER_MESSAGE_JOIN_CHANNEL:
                    workThread.joinChannel((String) msg.obj, msg.arg1);
                    break;
                case SConstants.HANDLER_MESSAGE_JOIN_SUB_CHANNEL:
                    workThread.joinSubChannel((String) msg.obj, msg.arg1);
                    break;
                case SConstants.HANDLER_MESSAGE_PREVIEW:
                    Object[] previewData = (Object[]) msg.obj;
                    workThread.preview((boolean) previewData[0], (SurfaceView) previewData[1], (int) previewData[2]);
                    break;
                case SConstants.HANDLER_MESSAGE_EXIT:
                    workThread.exit();
                    break;
                case SConstants.HANDLER_MESSAGE_LEAVE_CHANNEL:
                    workThread.leaveChannel();
                    break;
                case SConstants.HANDLER_MESSAGE_REMOTE_VIEW:
                    Object[] remoteData = (Object[]) msg.obj;
                    workThread.setmRemoteView((SurfaceView) remoteData[0], (String) remoteData[1], (int) remoteData[2]);
                    break;
                case SConstants.HANDLER_MESSAGE_LEAVE_SUB_CHANNEL:
                    workThread.leaveSubChannel();
                    break;
                default:
                    throw new RuntimeException("unknown handler event");
            }
        }
    }

    public WorkThread(WeakReference<Context> ctx) {
        this.mContext = ctx.get();
        this.mMediaEngineHandler = new MediaEngineHandler();
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        mWorkHandler = new WorkHandler(this);
        ensureEnineCreated();
        isThreadReady = true;
        Looper.loop();
    }

    public void waitForReady() {
        while (!isThreadReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ensureEnineCreated() {
        if (mRtcEngine != null)
            return;

        if (TextUtils.isEmpty(SConstants.MEDIA_APP_ID))
            throw new RuntimeException("media app id is null");

        try {
            mRtcEngine = RtcEngine.create(mContext, SConstants.MEDIA_APP_ID, mMediaEngineHandler.mainEngineEventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRtcEngine.setParameters("{\"rtc.log_filter\": 65535}");
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        mRtcEngine.enableVideo();
        mRtcEngine.enableAudio();
        mRtcEngine.enableWebSdkInteroperability(true);
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_480P_3, false);
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public MediaEngineHandler handler() {
        return mMediaEngineHandler;
    }

    public void exit() {
        if (Thread.currentThread() != this) {
            mWorkHandler.release();
            return;
        }
        Looper.myLooper().quit();
        isThreadReady = false;
    }

    /**
     * 先调用joinChannel
     * 加入学生们的房间
     * @param channel
     * @param uid
     */
    public final void joinChannel(final String channel, int uid) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = SConstants.HANDLER_MESSAGE_JOIN_CHANNEL;
            envelop.obj = channel;
            envelop.arg1 = uid;
            mWorkHandler.sendMessage(envelop);
            return;
        }

        ensureEnineCreated();
        int ret = mRtcEngine.joinChannel(null, channel, "", uid);
        Log.e(TAG, "joinChannel:" + ret);
    }

    /**
     * 后调用joinSubChannel
     * 加入老师的房间
     * @param channel
     * @param uid
     */
    public final void joinSubChannel(final String channel, int uid) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = SConstants.HANDLER_MESSAGE_JOIN_SUB_CHANNEL;
            envelop.obj = channel;
            envelop.arg1 = uid;
            mWorkHandler.sendMessage(envelop);
            return;
        }

        ensureEnineCreated();

        if (mRtcChannel != null) {
            mRtcChannel.destroy();
            mRtcChannel = null;
        }

        mRtcChannel = mRtcEngine.createRtcChannel(channel);
        if (mRtcChannel == null) return;
        mRtcChannel.setRtcChannelEventHandler(mMediaEngineHandler.channelEventHandler);
        ChannelMediaOptions options = new ChannelMediaOptions();
        options.autoSubscribeAudio = true;
        options.autoSubscribeVideo = true;
        int ret = mRtcChannel.joinChannel("", "",  uid, options);

//        int ret = mRtcEngine.joinSubChannel(null, channel, "", uid, mMediaEngineHandler.subEngineEventHandler);
        Log.e(TAG, "joinSubChannel:" + ret);
    }

    public final void preview(boolean start, SurfaceView view, int uid) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = SConstants.HANDLER_MESSAGE_PREVIEW;
            envelop.obj = new Object[]{start, view, uid};
            mWorkHandler.sendMessage(envelop);
            return;
        }

        ensureEnineCreated();
        if (start) {
            mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            mRtcEngine.startPreview();
        } else {
            mRtcEngine.stopPreview();
        }
    }

    public final void setmRemoteView(SurfaceView view, String channel, int uid) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = SConstants.HANDLER_MESSAGE_REMOTE_VIEW;
            envelop.obj = new Object[]{view, uid};
            mWorkHandler.sendMessage(envelop);
            return;
        }

        ensureEnineCreated();
        mRtcEngine.setupRemoteVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, channel, uid));
    }


    /**
     * 后 leave
     */
    public final void leaveChannel() {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = SConstants.HANDLER_MESSAGE_LEAVE_CHANNEL;
            mWorkHandler.sendMessage(envelop);
            return;
        }
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
    }

    /**
     * 先leave sub
     */
    public final void leaveSubChannel() {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = SConstants.HANDLER_MESSAGE_LEAVE_SUB_CHANNEL;
            mWorkHandler.sendMessage(envelop);
            return;
        }
        if (mRtcChannel != null) {
            mRtcChannel.leaveChannel();
        }
    }
}
