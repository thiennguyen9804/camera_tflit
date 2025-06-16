package com.example.camera_tflit.core

import android.opengl.GLES20

object FboManager {
    val mFboHandler = IntArray(1)
    val mTexture = IntArray(1)
    fun initBuffer() {
        GLES20.glGenFramebuffers(1, mFboHandler, 0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFboHandler[0])

        GLES20.glGenTextures(1, mTexture, 0)
//        GLES20.glBindTexture()
    }
}