package com.example.camera_tflit.core

import android.content.Context
import android.opengl.GLES20
import android.util.Log

object ShaderManager {
    var mVertexShader: String? = null
    var mFragShader: String? = null
    var mProgram: Int? = null
    fun loadFromFile(context: Context) {
        mVertexShader = loadFromAssetFile(context, "yuv2rgb.vert")
        mFragShader = loadFromAssetFile(context, "yuv2rgb.frag")
    }

    fun compileShader() {
        mProgram = createProgram(mVertexShader!!, mFragShader!!)
    }

    fun loadShader(shaderType: Int, source: String): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if(shader != 0) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if(compiled[0] == 0) {
                Log.e("ES20_ERROR", "Could not compile shader $shaderType:");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }

        return shader
    }

    fun createProgram(vertexSource: String, fragSource: String): Int {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0;
        }
        val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragSource)
        if (pixelShader == 0) {
            return 0;
        }

        var program = GLES20.glCreateProgram()
        if(program != 0) {
            GLES20.glAttachShader(program, vertexShader)
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");

            GLES20.glLinkProgram(program)
            var linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if(linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    fun checkGlError(op: String?) {
        val error: Int
        while ((GLES20.glGetError().also { error = it }) != GLES20.GL_NO_ERROR) {
            Log.e("ES20_ERROR", "$op: glError $error")
            throw RuntimeException("$op: glError $error")
        }
    }

    fun loadFromAssetFile(context: Context, fname: String): String {
        return context.assets.open(fname).bufferedReader().use {
            it.readText().replace("\r\n", "\n")
        }
    }
}