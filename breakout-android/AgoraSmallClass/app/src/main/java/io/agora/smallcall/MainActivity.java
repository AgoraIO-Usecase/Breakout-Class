package io.agora.smallcall;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class MainActivity extends AppCompatActivity implements IMediaEngineHandler.SubChannel, IMediaEngineHandler.MainChannel{
    private boolean hasJoinMainChannel = false;
    private boolean hasJoinSubChannel = false;

    private RecyclerView smallView;
    private FrameLayout bigView;
    private RecyclerView msgView;

    private CRLRecycleAdapter smallVideoAdapter;
    private CRMRecycleAdapter msgAdapter;

    private List<UserInfo> userList = new ArrayList<>();
    private List<String> msgList = new ArrayList<>();

    private WorkThread workThread;

    private List<String> permissionList = new ArrayList<>();


    private int teacherUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_main);

        if (checkSelfPermissions()) {
            if (workThread == null) {
                workThread = new WorkThread(new WeakReference<Context>(this));
                workThread.start();
                workThread.waitForReady();
            }

            initWidget();
        }
    }

    private boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.RECORD_AUDIO, 100) &&
                checkSelfPermission(Manifest.permission.CAMERA, 101) &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 102);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            permissionList.add(permission);
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initWidget() {
        worker().handler().addMainEventHandler(this);
        worker().handler().addSubEventHandler(this);

        smallView = findViewById(R.id.rv_small_video);
        bigView = findViewById(R.id.fl_big_video);
        msgView = findViewById(R.id.rv_chat_room_main_message);

        smallVideoAdapter = new CRLRecycleAdapter(this, userList);
        smallView.setHasFixedSize(true);
        smallView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        smallView.setAdapter(smallVideoAdapter);

        msgAdapter = new CRMRecycleAdapter(new WeakReference<Context>(this), msgList);
        msgView.setHasFixedSize(true);
        msgView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        msgView.setAdapter(msgAdapter);
        msgView.addItemDecoration(new CRMItemDecor());
    }

    public void onStudentChannelJoinClicked(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.pop_view_join_main_channel, null);
        alertDialog.setView(rootView);
        final AlertDialog dialog = alertDialog.create();
        if (null != dialog.getWindow())
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        Button btn = rootView.findViewById(R.id.btn_main_join);
        final EditText et = rootView.findViewById(R.id.et_main_join);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    Toast.makeText(MainActivity.this, "please input a channel name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (hasJoinMainChannel){
                    Toast.makeText(MainActivity.this, "you had joined!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                hasJoinMainChannel = true;
                worker().joinChannel(et.getText().toString(), 0);
                sendMessage("joinchannel:" + et.getText().toString());
                dialog.dismiss();
            }
        });

        Button btnMainLeave = rootView.findViewById(R.id.btn_main_leave);
        btnMainLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasJoinMainChannel)
                    worker().leaveChannel();
                dialog.dismiss();
            }
        });
    }

    public void onTeacherChannelJoinClicked(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.pop_view_join_sub_channel, null);
        alertDialog.setView(rootView);
        final AlertDialog dialog = alertDialog.create();
        if (null != dialog.getWindow())
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        Button btn = rootView.findViewById(R.id.btn_sub_join);
        final EditText et = rootView.findViewById(R.id.et_sub_join);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    Toast.makeText(MainActivity.this, "please input a channel name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (hasJoinSubChannel){
                    Toast.makeText(MainActivity.this, "you had joined!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                hasJoinSubChannel = true;
                worker().joinSubChannel(et.getText().toString(), 0);
                sendMessage("joinSubChannel:" + et.getText().toString());
                dialog.dismiss();
            }
        });

        Button btnSubLeave = rootView.findViewById(R.id.btn_sub_leave);
        btnSubLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasJoinSubChannel)
                    worker().leaveSubChannel();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        worker().leaveSubChannel();
        worker().leaveChannel();
        hasJoinMainChannel = false;
        hasJoinSubChannel = false;

        worker().handler().removeMainEventHandler(this);
        worker().handler().removeSubEventHandler(this);
    }

    private List<UserInfo> getUserList(int uid) {
        for (UserInfo info: userList) {
            if (info.uid == uid) {
                userList.remove(info);
                break;
            }
        }

        return userList;
    }

    public void sendMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgList.add(message);

                if (msgList.size() > 14) {// max value is 15
                    int len = msgList.size() - 15;
                    for (int i = 0; i < len; i++) {
                        msgList.remove(i);
                    }
                }

                msgAdapter.upDateDataSet(msgList);
                msgView.smoothScrollToPosition(msgAdapter.getItemCount() - 1);
            }
        });
    }

    @Override
    public void onJoinMainChannelSuccess(String channel, final int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SurfaceView sv = RtcEngine.CreateRendererView(MainActivity.this);
                sv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                sv.setZOrderOnTop(true);
                sv.setZOrderMediaOverlay(true);

                UserInfo info = new UserInfo();
                info.sv = sv;
                info.uid = uid;

                userList.add(info);
                worker().preview(true, sv, uid);
                smallVideoAdapter.update(userList);
                sendMessage("joinChannel main success:" + uid);
            }
        });
    }

    @Override
    public void onMainUserJoined(final int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SurfaceView sv = RtcEngine.CreateRendererView(MainActivity.this);
                sv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                sv.setZOrderOnTop(false);
                sv.setZOrderMediaOverlay(false);

                UserInfo info = new UserInfo();
                info.sv = sv;
                info.uid = uid;

                userList.add(info);
                rtcEngine().setupRemoteVideo(new VideoCanvas(sv, Constants.RENDER_MODE_HIDDEN, uid));
                smallVideoAdapter.update(userList);
                sendMessage("other users join channel:" + uid);
            }
        });
    }

    @Override
    public void onMainError(int err) {
        sendMessage("sdk main error: " + err);
    }

    @Override
    public void onMainUserOffline(final int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                smallVideoAdapter.update(getUserList(uid));
                sendMessage("your classmate leave channel!!" + uid);
            }
        });
    }

    @Override
    public void onMainLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userList.clear();
                smallVideoAdapter.update(userList);
                hasJoinMainChannel = false;
            }
        });

    }

    @Override
    public void onMainFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onMainFirstLocalVideoFrame(int width, int height, int elapsed) {

    }

    // ---------------------------------------sub channel for teacher------------------------------------------------
    @Override
    public void onJoinSubChannelSuccess(String channel, int uid, int elapsed) {
        sendMessage("joinChannel sub success:" + uid);
    }

    @Override
    public void onSubUserJoined(final String channel, final int uid, final int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bigView.getChildCount() == 0) {
                    SurfaceView sv = RtcEngine.CreateRendererView(MainActivity.this);
                    sv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    sv.setZOrderOnTop(false);
                    sv.setZOrderMediaOverlay(false);

                    rtcEngine().setupRemoteVideo(new VideoCanvas(sv, Constants.RENDER_MODE_HIDDEN, channel, uid));

                    if (bigView.getChildCount() > 0)
                        bigView.removeAllViews();
                    bigView.addView(sv);
                    teacherUid = uid;
                } else {
                   onMainUserJoined(uid, elapsed);
                }

                sendMessage("teacher join channel:" + uid);
            }
        });
    }

    @Override
    public void onSubError(int err) {
        sendMessage("sdk sub error: " + err);
    }

    @Override
    public void onSubUserOffline(final int uid, final int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (teacherUid == uid) {
                    bigView.removeAllViews();
                } else {
                    onMainUserOffline(uid, reason);
                }
                sendMessage("The teacher leaved channel! Class Over!!" + uid);
            }
        });
    }

    @Override
    public void onSubLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bigView.removeAllViews();
                hasJoinSubChannel = false;
            }
        });
    }

    @Override
    public void onSubFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onSubFirstLocalVideoFrame(int width, int height, int elapsed) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, 101);

                    permissionList.remove(Manifest.permission.CAMERA);
                    Log.e("wbsTest-->",Manifest.permission.CAMERA);
                } else {
                    finish();
                }
                break;
            case 101:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 102);
                    permissionList.remove(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Log.e("wbsTest-->",Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    finish();
                }
                break;

            case 102:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionList.remove(Manifest.permission.RECORD_AUDIO);
                    Log.e("wbsTest-->",Manifest.permission.RECORD_AUDIO);
                } else {
                    finish();
                }
                break;

        }
        if (permissionList.isEmpty()) {
            if (workThread == null) {
                workThread = new WorkThread(new WeakReference<Context>(MainActivity.this));
                workThread.start();
                workThread.waitForReady();
            }

            initWidget();
        }
    }


    protected RtcEngine rtcEngine() {
        return workThread.rtcEngine();
    }

    protected final WorkThread worker() {
        return workThread;
    }
}
