cmake_minimum_required(VERSION 3.4.1)

# Tạo thư viện shared (.so) từ file native-lib.cpp
add_library(
        yuv2rgb-lib           # Tên thư viện => sẽ load bằng System.loadLibrary("native-lib")
        SHARED
        yuv2rgb-lib.cpp       # Các file nguồn native
)

# Tìm thư viện log của Android (nằm sẵn trong hệ thống)
find_library(
        log-lib
        log
)

# Liên kết thư viện native với thư viện log
target_link_libraries(
        yuv2rgb-lib
        ${log-lib}
)
