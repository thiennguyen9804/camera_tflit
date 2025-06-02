#include <jni.h>
#include <cstring>

//
// Created by hayashing on 6/2/25.
//
namespace {
    void yuv2Nv21(
        int width, int height,
        const int8_t* y_buffer, const int8_t* u_buffer,
        const int8_t* v_buffer,
        int y_row_stride, int uv_row_stride,
        int y_pixel_stride, int uv_pixel_stride,
        int8_t* nv21
    ) {
        for(int y = 0; y < height; y++) {
            int destOffset = y * width;
            int yOffset = y * y_row_stride;
            memcpy(nv21 + destOffset, y_buffer + yOffset, width);
        }

        if(v_buffer - u_buffer == sizeof(uint8_t) || u_buffer - v_buffer == sizeof(uint8_t)) {

        }


    }
}



extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_camera_1tflit_Yuv420888ImageAdapter_yuv2Nv21(JNIEnv *env, jobject thiz, jint width,
                                                              jint height, jbyteArray y,
                                                              jbyteArray u, jbyteArray v,
                                                              jint y_row_stride, jint uv_row_stride,
                                                              jint y_pixel_stride,
                                                              jint uv_pixel_stride,
                                                              jbyteArray nv21_output) {

}