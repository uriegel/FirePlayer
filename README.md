# FirePlayer
Mediaplayer for Amazon Firestick and HomeServer (github.com/uriegel/HomeServer) as HTTP Range Streamer
## Power saving when using Sony TV
TV url:
```http://<tv url>/sony```

PSK:

enter PSK in Sony TV settings and FireMusic settings

## Debug on Firestick
```/daten/Android/Sdk/platform-tools/adb connect 192.168.178.81```

## Install on Firestick

```adb devices``` to list connected devices

install with 

```adb install app-debug.apk```

For an update of an already-installed version:

```adb install -r app-debug.apk```
