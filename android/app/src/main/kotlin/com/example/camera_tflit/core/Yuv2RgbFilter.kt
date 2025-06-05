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


}