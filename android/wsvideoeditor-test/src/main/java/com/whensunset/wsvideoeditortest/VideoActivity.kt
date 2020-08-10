package com.whensunset.wsvideoeditortest

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import com.whensunset.wsvideoeditorsdk.WsMediaPlayer
import com.whensunset.wsvideoeditorsdk.WsMediaPlayerView
import com.whensunset.wsvideoeditorsdk.WsVideoEditorUtils
import com.whensunset.wsvideoeditorsdk.model.EditorProject
import com.whensunset.wsvideoeditorsdk.model.MediaAsset
import kotlinx.android.synthetic.main.video_activity.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class VideoActivity : Activity() {
    private var mPreviewView: WsMediaPlayerView? = null
    private var mPlayer: WsMediaPlayer? = null
    private var playBtn: Button? = null
    private fun updatePlayStateShow() {
        if (mPlayer != null) {
            if (!mPlayer!!.isPlaying) {
                playBtn!!.text = "Play"
            } else {
                playBtn!!.text = "Pause"
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (mPlayer != null) {
            mPlayer!!.pause()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            EasyPermissions.requestPermissions(this, "权限", 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        WsVideoEditorUtils.initJni()
        setContentView(R.layout.video_activity)
        mPreviewView = findViewById(R.id.ws_media_player_view)
        val videoEditorProjectBuilder = EditorProject.newBuilder()
        val trackAssetBuilder = MediaAsset.newBuilder()
        trackAssetBuilder.setAssetId(System.currentTimeMillis()).setAssetPath(File(getExternalFilesDir(null), "test.mp4").absolutePath).volume = 1.0
        videoEditorProjectBuilder.addMediaAsset(trackAssetBuilder.build()).blurPaddingArea = true
        mPlayer = WsMediaPlayer()
        mPlayer!!.project = videoEditorProjectBuilder.build()
        try {
            mPlayer!!.loadProject()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
        mPreviewView?.setPreviewPlayer(mPlayer)
        mPlayer!!.play()
        playBtn = findViewById<View>(R.id.btnPlayVideo) as Button
        playBtn!!.setOnClickListener {
            if (mPlayer!!.isPlaying) {
                mPlayer!!.pause()
            } else {
                mPlayer!!.play()
            }
            updatePlayStateShow()
        }
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }


            override fun onStartTrackingTouch(seekBar: SeekBar?){

            }


            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mPlayer?.seek(seekBar!!.progress)
            }
        })
        updatePlayStateShow()
    }

    override fun onDestroy() {
        if (mPlayer != null) {
            mPreviewView!!.onPause()
            mPreviewView!!.setPreviewPlayer(null)
            mPlayer!!.release()
        }
        mPreviewView!!.onPause()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mPreviewView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mPreviewView!!.onResume()
    }
}