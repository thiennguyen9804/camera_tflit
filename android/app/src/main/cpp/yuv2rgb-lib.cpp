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



        int idUV = width * height;
        int uv_width = width / 2;
        int uv_height = height / 2;

        for(int y = 0; y < uv_height; y++) {
            int uv_offset = (y * uv_row_stride);
            for(int x = 0; x < uv_width; x++) {
                int buffer_index = uv_offset + (x * uv_pixel_stride);
                nv21[idUV++] = v_buffer[buffer_index];
                nv21[idUV++] = u_buffer[buffer_index];
            }
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
    auto y_buffer = static_cast<uint8_t*>
}