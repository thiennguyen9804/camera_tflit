#include <jni.h>
#include <cstring>
#include <android/log.h>

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
//            for (int x = 0; x < width; x++) {
//                nv21[destOffset + x] = y_buffer[yOffset + x * y_pixel_stride];
//            }
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
Java_com_example_camera_1tflit_presentation_YuvNv21Adapter_yuv2Nv21(JNIEnv *env,
                                                                    jobject thiz, jint width,
                                                                    jint height, jbyteArray y,
                                                                    jbyteArray u, jbyteArray v,
                                                                    jint y_row_stride, jint uv_row_stride,
                                                                    jint y_pixel_stride,
                                                                    jint uv_pixel_stride,
                                                                    jbyteArray nv21_output) {
    auto y_buffer = env->GetByteArrayElements(y, nullptr);
    auto u_buffer = env->GetByteArrayElements(u, nullptr);
    auto v_buffer = env->GetByteArrayElements(v, nullptr);
    jbyte* nv21 = env->GetByteArrayElements(nv21_output, nullptr);
    if(y_buffer == nullptr || u_buffer == nullptr || v_buffer == nullptr || nv21 == nullptr) {
        return false;
    }

    yuv2Nv21(width, height, y_buffer, u_buffer,
             v_buffer, y_row_stride, uv_row_stride,
             y_pixel_stride, uv_pixel_stride, nv21);
    env->ReleaseByteArrayElements(nv21_output, nv21, 0);
    env->ReleaseByteArrayElements(y, y_buffer, JNI_ABORT);
    env->ReleaseByteArrayElements(u, u_buffer, JNI_ABORT);
    env->ReleaseByteArrayElements(v, v_buffer, JNI_ABORT);
    return true;
}
