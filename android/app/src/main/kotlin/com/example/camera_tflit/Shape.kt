package com.example.camera_tflit

import android.opengl.GLES30

class Shape {
    private var mProgram: Int = -999
    fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader ->
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }

    init {
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, ShaderCode.vertexShaderCode)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, ShaderCode.fragmentShaderCode)
        mProgram = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)
            GLES30.glAttachShader(it, fragmentShader)
            GLES30.glLinkProgram(it)
        }
    }

    fun draw() {
        GLES30.glUseProgram(mProgram)

    }
}