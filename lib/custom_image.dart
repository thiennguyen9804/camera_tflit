import 'package:camera/camera.dart';
import 'package:camera_tflit/camera_image_x.dart';
import 'package:camera_tflit/my_image.dart';

class CustomImage implements MyImage{
  final CameraImage _image;
  final int rotation;

  CustomImage(this._image, this.rotation);

  @override
  Map<String, dynamic> toMap() {
    var map = _image.toMap();
    map['rotation'] = rotation;
    return map;
  }
}
