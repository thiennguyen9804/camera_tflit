import 'dart:typed_data';

import 'package:camera/camera.dart';
import 'package:flutter/foundation.dart';

extension CameraImageX on CameraImage {
  

  Map<String, dynamic> toMap() {
    final map = {
      'width': width,
      'height': height,
      'y': planes[0].bytes,
      'u': planes[1].bytes,
      'v': planes[2].bytes,
      'yRowStride': planes[0].bytesPerRow,
      'uvRowStride': planes[1].bytesPerRow,
      'yPixelStride': planes[0].bytesPerPixel,
      'uvPixelStride': planes[1].bytesPerPixel
    };

    return map;
  }
}
