package com.whensunset.wsvideoeditortest

import android.app.Application
import android.util.Log
import java.io.*
import kotlin.jvm.Throws

/**
 * 2020/8/10
 *
 * @author zhengliao
 */

class AppImpl : Application() {
    override fun onCreate() {
        super.onCreate()
        copyAssets()
    }

    private fun copyAssets() {
        val assetManager = assets
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = assetManager.open("test.mp4")
            val outFile = File(getExternalFilesDir(null), "test.mp4")
            out = FileOutputStream(outFile)
            copyFile(`in`, out)
        } catch (e: IOException) {
            Log.e("tag", "Failed to copy asset file: " + "test.mp4", e)
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    // NOOP
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream?, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`!!.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }
}