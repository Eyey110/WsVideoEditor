package com.whensunset.wsvideoeditortest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.whensunset.wsvideoeditorsdk.WsVideoEditorUtils;
import com.whensunset.wsvideoeditorsdk.inner.VideoDecodeService;
import com.whensunset.wsvideoeditorsdk.model.EditorProject;
import com.whensunset.wsvideoeditorsdk.model.MediaAsset;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import pub.devrel.easypermissions.EasyPermissions;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        WsVideoEditorUtils.initJni();
        EasyPermissions.requestPermissions(this, "权限", 1, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        initButton();
    }

    TextView showState;
    TextView showFrame;
    VideoDecodeService mVideoDecodeService;
    AtomicLong timestamp = new AtomicLong(0);
    final StringBuilder stringBuilder = new StringBuilder();
    AtomicInteger times = new AtomicInteger(1);

    private void initButton() {
        showState = findViewById(R.id.show_state);
        showFrame = findViewById(R.id.show_frame);
        mVideoDecodeService = new VideoDecodeService(10);

        final EditorProject.Builder videoEditorProjectBuilder = EditorProject.newBuilder();
        MediaAsset.Builder builder = MediaAsset.newBuilder();
        builder.setAssetId(System.currentTimeMillis()).setAssetPath(new File(getExternalFilesDir(null), "test.mp4").getAbsolutePath());
        videoEditorProjectBuilder.addMediaAsset(builder.build());

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder.delete(0, stringBuilder.toString().length());
                times.set(1);
                timestamp.set(0);

                mVideoDecodeService.setProject(0, videoEditorProjectBuilder.build());
                mVideoDecodeService.start();
            }
        });

        findViewById(R.id.seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder.delete(0, stringBuilder.toString().length());
                times.set(1);
                timestamp.set(3000);

                mVideoDecodeService.seek(3);
            }
        });

        findViewById(R.id.stop).setOnClickListener(v -> {
            stringBuilder.delete(0, stringBuilder.toString().length());
            times.set(1);
            timestamp.set(0);

            mVideoDecodeService.stop();
        });

        findViewById(R.id.draw).setOnClickListener(v -> startActivity(new Intent(TestActivity.this, VideoActivity.class)));

        new Thread(() -> {
            while (true) {
                if (mVideoDecodeService.stopped() || mVideoDecodeService.ended()) {
                    continue;
                }
                long startTime = System.currentTimeMillis();
                final String frameString = mVideoDecodeService.getRenderFrame(timestamp.get() * 1f / 1000);
                final long costTime = System.currentTimeMillis() - startTime;
                final String finalFrameString = frameString + "，获取一帧花费的时间:" + costTime + "，预期当前帧的时间戳:" + timestamp.get() * 1f / 1000 + "s，当前帧是第几个有效帧:" + times.get() + "\n\n";
                if (!finalFrameString.contains("当前帧属于哪个视频文件:，")) {
                    times.set(times.get() + 1);
                }
                timestamp.set(timestamp.get() + 30);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showFrame.post(() -> {
                    stringBuilder.append(finalFrameString);
                    showFrame.setText(stringBuilder.toString());
                });
            }
        }).start();

        new Thread(() -> {
            while (true) {
                showState.post(() -> showState.setText("是否解码到了视频的结尾:" + mVideoDecodeService.ended() + "，是否暂停解码:" + mVideoDecodeService.stopped() + "，队列中的帧数量:" + mVideoDecodeService.getBufferedFrameCount()));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
