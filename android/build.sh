#!/usr/bin/env bash
# Builds ShooterArena.apk (WebView wrapper with the game bundled offline).
# Needs: JDK, Android SDK with platforms;android-34 and build-tools;35.0.0.
# Signing key: android/keystore.jks (NOT in git — keep your own copy safe).
set -e
cd "$(dirname "$0")"
SDK=${ANDROID_HOME:-/opt/android-sdk}; BT=$SDK/build-tools/35.0.0; JAR=$SDK/platforms/android-34/android.jar
KS=${KEYSTORE:-keystore.jks}; KS_PASS=${KS_PASS:?set KS_PASS}; KS_ALIAS=${KS_ALIAS:-shooterarena}
rm -rf build && mkdir -p build/classes build/dex build/assets/www build/res/mipmap-xxxhdpi
cp ../index.html ../sw.js ../manifest.json ../peerjs.min.js ../icon-192.png ../icon-512.png build/assets/www/
cp -r res/* build/res/ && cp ../icon-512.png build/res/mipmap-xxxhdpi/ic_launcher.png
$BT/aapt2 compile --dir build/res -o build/res.zip
$BT/aapt2 link -o build/unsigned.apk -I $JAR --manifest AndroidManifest.xml -A build/assets build/res.zip \
  --min-sdk-version 24 --target-sdk-version 34 --java build/gen
javac --release 8 -nowarn -Xlint:-options -cp $JAR -d build/classes $(find src build/gen -name '*.java')
$BT/d8 --release --min-api 24 --lib $JAR --output build/dex $(find build/classes -name '*.class')
(cd build/dex && zip -q ../unsigned.apk classes.dex)
$BT/zipalign -f -p 4 build/unsigned.apk build/aligned.apk
$BT/apksigner sign --ks "$KS" --ks-pass pass:$KS_PASS --ks-key-alias $KS_ALIAS --out ShooterArena.apk build/aligned.apk
echo "OK -> android/ShooterArena.apk"
