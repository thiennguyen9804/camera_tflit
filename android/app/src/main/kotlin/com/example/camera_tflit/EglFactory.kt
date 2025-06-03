package com.example.camera_tflit

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLExt
import android.opengl.EGLSurface

fun createEglContext(width: Int, height: Int): Triple<EGLDisplay, EGLContext, EGLSurface> {
    val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
    check(eglDisplay != EGL14.EGL_NO_DISPLAY) {
        "Không lấy được EGL display"
    }

    val version = IntArray(2)
    check(EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
        "Không khởi tạo EGL được"
    }

    val configAttributes = intArrayOf(
        EGL14.EGL_RED_SIZE, 8,
        EGL14.EGL_GREEN_SIZE, 8,
        EGL14.EGL_BLUE_SIZE, 8,
        EGL14.EGL_ALPHA_SIZE, 8,
        EGL14.EGL_RENDERABLE_TYPE, EGLExt.EGL_OPENGL_ES3_BIT_KHR,
        EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
        EGL14.EGL_NONE
    )

    val configs = arrayOfNulls<EGLConfig>(1)
    val numConfigs = IntArray(1)
    check(EGL14.eglChooseConfig(eglDisplay, configAttributes, 0, configs, 0, 1, numConfigs, 0)) {
        "Không chọn được EGL config"
    }

    val eglConfig = configs[0]!!

    val contextAttributes = intArrayOf(
        EGL14.EGL_CONTEXT_CLIENT_VERSION, 3,
        EGL14.EGL_NONE
    )

    val eglContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, contextAttributes, 0)
    check(eglContext != EGL14.EGL_NO_CONTEXT) {
        "Không tạo được EGLContext"
    }

    val surfaceAttributes = intArrayOf(
        EGL14.EGL_WIDTH, width,
        EGL14.EGL_HEIGHT, height,
        EGL14.EGL_NONE
    )

    val eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig, surfaceAttributes, 0)
    check(eglSurface != EGL14.EGL_NO_SURFACE) {
        "Không tạo được EGLSurface"
    }

    check(EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
        "Không makeCurrent được EGL context"

    }

    return Triple(eglDisplay, eglContext, eglSurface)

}




