The "adb" tool also offers you commands to copy files into and from the connected Android emulator or devices. 
The "adb push <local> <remote>" copies a file or folder from the local system to the remote emulator or device. 
The "adb pull <remote> <local>" copies a file or folder from the remote emulator or device to the local system. 

Example 1 - Copying "Silk-Road.jpg" to the /sdcard/Picture folder in the emulator: 

C:\local\android-sdk-windows\platform-tools>adb push \ 
   \herong\Pictures\Silk-Road.jpg /sdcard/Picture
258 KB/s (111019 bytes in 0.420s)

Example 2 - Copying "init.rc" from the / folder in the emulator: 

C:\local\android-sdk-windows\platform-tools>adb pull \
   /init.rc \herong
139 KB/s (17040 bytes in 0.119s)

Example 3 - Copying all files from the /data/data folder in the emulator: 

C:\local\android-sdk-windows\platform-tools>adb pull 
   /data/app \herong\app
pull: building file list...
pull: /data/app/GestureBuilder.apk -> \herong\app/GestureBuilder.apk
pull: /data/app/SoftKeyboard.apk -> \herong\app/SoftKeyboard.apk
pull: /data/app/ApiDemos.apk -> \herong\app/ApiDemos.apk
pull: /data/app/SoftKeyboard.odex -> \herong\app/SoftKeyboard.odex
pull: /data/app/CubeLiveWallpapers.apk -> \herong\app/CubeLiveWallp...
pull: /data/app/ApiDemos.odex -> \herong\app/ApiDemos.odex
pull: /data/app/GestureBuilder.odex -> \herong\app/GestureBuilder.odex
pull: /data/app/WidgetPreview.odex -> \herong\app/WidgetPreview.odex
pull: /data/app/CubeLiveWallpapers.odex -> \herong\app/CubeLiveWall...
pull: /data/app/WidgetPreview.apk -> \herong\app/WidgetPreview.apk
pull: /data/app/com.herongyang-1.apk -> \herong\app/com.herongyang-...
pull: /data/app/net.sf.andpdf.pdfviewer-1.apk -> \herong\app/net.sf...
pull: /data/app/com.adobe.reader-1.apk -> \herong\app/com.adobe.rea...
13 files pulled. 0 files skipped.
148 KB/s (7022410 bytes in 46.298s)
