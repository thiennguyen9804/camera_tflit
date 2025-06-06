package com.example.camera_tflit.core

import android.graphics.Shader
import android.opengl.GLES20
import android.util.Log
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object Yuv2RgbFilter {
    var mProgram: Int? = null
    var mPositionHandle: Int? = null
    var mTexCoordHandle: Int? = null
    var mTextureHandle = IntArray(3) // sampler trong glsl
    var mTexture = IntArray(3)
    var textureCounts = 3
    var mVertexBuffer: FloatBuffer? = null
    var mTexCoorBuffer: FloatBuffer? = null

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
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture[i])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
    }

    fun uploadTexture(width: Int, height: Int, pixels: Array<Buffer?>) {
        val planes = intArrayOf(0, 1, 2)
        val widths = intArrayOf(width, width / 2, width / 2)
        val heights = intArrayOf(height, height / 2, height / 2)

        for (i in 0..2) {
            val plane = planes[i]
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture[i])
            ShaderManager.checkGlError("glBindTexture")

            if(pixels[plane] == null) {
                Log.e("YUV2RGBFilter", "pixels[plane] == null");
            }

            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_LUMINANCE,
                widths[plane],
                heights[plane],
                0,
                GLES20.GL_LUMINANCE,
                GLES20.GL_UNSIGNED_BYTE,
                pixels[plane]
            )

            ShaderManager.checkGlError("glTexImage2D")
            GLES20.glUniform1i(mTextureHandle[i], i)
            ShaderManager.checkGlError("glUniform1i")
        }

        GLES20.glVertexAttribPointer(mPositionHandle!!, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        ShaderManager.checkGlError("glVertexAttribPointer")
        GLES20.glEnableVertexAttribArray(mPositionHandle!!)

        GLES20.glVertexAttribPointer(mTexCoordHandle!!, 2, GLES20.GL_FLOAT, false, 0, mTexCoorBuffer)
        ShaderManager.checkGlError("glVertexAttribPointer");
        GLES20.glEnableVertexAttribArray(mTexCoordHandle!!);

        ShaderManager.checkGlError("glEnableVertexAttribArray");
    }


    fun disableVertexAttribArray() {
        GLES20.glDisableVertexAttribArray(mPositionHandle!!)
        GLES20.glDisableVertexAttribArray(mTexCoordHandle!!)
    }

    fun updateVertexParam() {
        mVertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
                .asFloatBuffer().apply {
                    put(vertices)
                    position(0)
                }
        }

        mTexCoorBuffer = ByteBuffer.allocateDirect(texCoord.size * 4).run {
            order(ByteOrder.nativeOrder())
                .asFloatBuffer().apply {
                    put(texCoord)
                    position(0)
                }
        }
    }

}