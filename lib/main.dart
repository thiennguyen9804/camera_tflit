import 'package:camera_tflit/camera_image_x.dart';
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

  var skipFrameCounter = 0;

  void processImage(CameraImage image) async {
    // image format: yuv 420 888
    if (skipFrameCounter % 60 == 0) {
      String landmark = '';
      try {
        landmark =
            await platform.invokeMethod<String>('getLandmark', image.toMap()) ??
            'Unknown landmark';
      } on PlatformException catch (e) {
        debugPrint("PlatformException + ${e.message}");
        landmark = "Failed to get landmark";
      }

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
        .then((_) {
          if (!mounted) {
            return;
          }
          setState(() {});
          controller.startImageStream(processImage);
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
                children: [
                  CameraPreview(controller),
                  SizedBox(height: 10),
                  Text(_landmark),
                ],
              ),
    );
  }
}
