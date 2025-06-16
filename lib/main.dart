import 'package:camera_tflit/camera_image_x.dart';
import 'package:camera_tflit/custom_image.dart';
import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:flutter/services.dart';

late List<CameraDescription> _cameras;

const platform = MethodChannel('com.example.camera_tflit/classify');
Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  _cameras = await availableCameras();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  late CameraController controller;
  String _landmark = '';
  double _maxZoom = 1.0;
  double _minZoom = 1.0;

  var skipFrameCounter = 0;

  Future<String> _processImageNative(CameraImage image) async {
    String landmark = '';

    try {
      final customImage = CustomImage(
        image,
        controller.description.sensorOrientation,
      );
      landmark =
          await platform.invokeMethod<String>(
            'getLandmark',
            customImage.toMap(),
          ) ??
          'Unknown landmark';
    } on PlatformException catch (e) {
      debugPrint("PlatformException + ${e.message}");
      debugPrint(e.stacktrace);
      landmark = "Failed to get landmark";
    }

    return landmark;
  }

  void _processImage(CameraImage image) async {
    // image format: yuv 420 888
    if (skipFrameCounter % 60 == 0) {
      final landmark = await _processImageNative(image);
      if (_landmark != landmark) {
        setState(() {
          _landmark = landmark;
        });
      }
    }

    skipFrameCounter++;
  }

  @override
  void initState() {
    super.initState();
    controller = CameraController(_cameras[0], ResolutionPreset.max);
    controller
        .initialize()
        .then((_) async {
          if (!mounted) {
            return;
          }
          _maxZoom = await controller.getMaxZoomLevel(); 
          _minZoom = await controller.getMinZoomLevel();
          setState(() {});
          // controller.startImageStream(_processImage);
        })
        .catchError((Object e) {
          if (e is CameraException) {
            switch (e.code) {
              case 'CameraAccessDenied':
                // Handle access errors here.
                break;
              default:
                // Handle other errors here.
                break;
            }
          }
        });
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  void _cameraScaleHandler(ScaleUpdateDetails details) async {
    final zoom = (details.scale).clamp(_minZoom, _maxZoom);
    await controller.setZoomLevel(zoom);
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
      body:
          !controller.value.isInitialized
              ? Container()
              : Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onScaleUpdate: _cameraScaleHandler,
                    child: CameraPreview(controller),
                  ),
                  SizedBox(height: 10),
                  Text(_landmark),
                  SizedBox(height: 20),
                  // ElevatedButton(onPressed: () async {
                  //   final ximage = await controller.takePicture();

                  // }, child: Text('Capture')),
                ],
              ),
    );
  }
}

class _MediaSizeClipper extends CustomClipper<Rect> {
  final Size mediaSize;
  const _MediaSizeClipper(this.mediaSize);
  @override
  Rect getClip(Size size) {
    return Rect.fromLTWH(0, 0, mediaSize.width, mediaSize.height);
  }

  @override
  bool shouldReclip(CustomClipper<Rect> oldClipper) {
    return true;
  }
}
