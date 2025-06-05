package com.example.camera_tflit.core

import android.opengl.GLES20

object Yuv2RgbFilter {
    var mProgram: Int? = null
    var mPositionHandle: Int? = null
    var mTexCoordHandle: Int? = null
    var mTextureHandle = IntArray(3)

    val vertices = floatArrayOf(
        -1.0f, -1.0f, 0.0f,  // bottom left
        1.0f, -1.0f, 0.0f,  // bottom right
        -1.0f,  1.0f, 0.0f,  // top left
        1.0f,  1.0f, 0.0f   // top right
    )

    val texCoord = floatArrayOf(
        0.0f, 1.0f,  // bottom left
        1.0f, 1.0f,  // bottom right
        0.0f, 0.0f,  // top left
        1.0f, 0.0f   // top right
    )

    fun initShader() {
        mProgram = ShaderManager.mProgram
        mPositionHandle = GLES20.glGetAttribLocation(mProgram!!, "position")
        ShaderManager.checkGlError("glGetAttribLocation")

        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram!!, "texcoord")
        ShaderManager.checkGlError("glGetAttribLocation")

        mTextureHandle[0] = GLES20.glGetUniformLocation(mProgram!!, "samplerY");
        ShaderManager.checkGlError("glGetUniformLocation");
        mTextureHandle[1] = GLES20.glGetUniformLocation(mProgram!!, "samplerU");
        ShaderManager.checkGlError("glGetUniformLocation");
        mTextureHandle[2] = GLES20.glGetUniformLocation(mProgram!!, "samplerV");
        ShaderManager.checkGlError("glGetUniformLocation");
    }

    fun generateTexture() {
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1)
        GLES20.glUseProgram(mProgram!!)
        GLES20.glGenTextures(3, mTextureHandle, 0)
        for (i in 0..2) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle[i])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
    }



}