![element](images/element.png "element")
# Element Face SDK
The Element Face SDK (the SDK) is an API library for creating biometric models that can be used to authenticate users. This document contains information for integrating the SDK into an Android application by using Android Studio.

The SDK was created on a modularized architecture to achieve flexible project designs and to fulfill various requirements. The main components of the SDK are:
- `element-face-cam`: the essential face detection camera module
- `element-face-core`: the core module with most of the features
- `element-face-support`: the additional support module enabling local device enrollment for offline usage
- `element-face-ui`: the add-on package for rich UI / UX and animations

It is straightforward to use `element-face-cam` and `element-face-core` for face authentication processing on the server, and `element-face-support` and `element-face-ui` are optional according to usage. Simply declare the modules in the dependency section and the SDK will configure automatically. No extra settings are needed.

## Version Support
### Android device & development environments
- The minimum Android device requirement is Android 5.0 or API 21 (Android OS Lollipop and up). A minimum of 2GB of RAM is recommended for optimal performance.
- Android Studio 4.0.1 with Gradle Wrapper 6.1.1
- Android Target SDK Version 29, Build Tool Version 29.0.3, and AndroidX

### Prerequisites
Please refer to [prerequisites](prerequisites.md) for more information.

### Dependencies for `element-face-cam`
- AndroidX WorkManager: 2.3.4
- Google Guava for Android: 27.0.1-android
- Google Gson for Android: 2.2.4 (can be omitted if Amazon AWS Mobile SDK is included with `element-face-core`)

### Dependencies for `element-face-core`
- Google Play Service Location: 17.0.0
- Google Material Design: 1.1.0
- Amazon AWS Mobile SDK: 2.8.5

### Dependencies for `element-face-ui`
- AndroidX Kotlin extensions: 1.3.0
- Airbnb Lottie Library: 3.0.7

References of dependencies can be found in the sample projects at [app-face](../app-face/build.gradle).

## SDK Integration
### Initialize the Element Face SDK
1. Create a class which extends [android.app.Application](https://developer.android.com/reference/android/app/Application), and initialize the `ElementFaceSDK` in `onCreate()` method:
    ```
    public class MainApplication extends Application {
      @Override
      public void onCreate() {
        super.onCreate();
        ElementFaceSDK.initSDK(this);
      }
    }
    ```
1. Declare the `MainApplication` class in AndroidManifest.xml:
    ```
    <manifest>
      .....
      <application android:name=".MainApplication">
        .....
      </application>
    </manifest>
    ```

### Ask for the permissions
1. The SDK requires the following permissions. These permissions are declared in the Element Face SDK AAR, and your app will inherit them. No need to declare them again in your app:
  * `android.Manifest.permission.CAMERA`
  * `android.Manifest.permission.ACCESS_FINE_LOCATION`
  * `android.Manifest.permission.ACCESS_COARSE_LOCATION`
1. Use `PermissionUtils.verifyPermissions(Activity activity, String... permissionsToVerify)` provided by the SDK to ask for user permissions:
    ```
    PermissionUtils.verifyPermissions(
      MainActivity.this,
      Manifest.permission.CAMERA,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION);
    ```
1. For Android 6.0 (Marshmallow, API 23) and up, make sure permissions are granted before starting any Activity provided by the Element Face SDK.

### SDK Debug Mode
Debug Mode can be enabled with the Element FM SDK. It is disabled by default. Once Debug Mode is ON, all image captures will be sent to Element for investigation. Please use this feature if you need help from Element.
    ```
    ElementFaceSDK.enableDebugMode(getBaseContext(), true);
    ```

### Element Activities
The SDK provides various Activities: `ElementFaceAuthActivity`, `ElementFaceCaptureActivity`, and `ElementFaceEnrollActivity`. Use of each Activity will be covered in the next section.

### Activity declaration
Declare the Activity in the manifest:
    ```
    <manifest>
      .....
      <application android:name=".MainApplication">
        .....
        <activity android:name="com.element.camera.ElementFaceEnrollActivity"
        android:theme="@style/CamTheme.Blue"
            android:clearTaskOnLaunch="true" />
        .....
      </application>
    </manifest>
    ```

### Start Activities
There are a few options to start the Element Activities. The Element Activities can be configured by passing the extras in the intent.
- `EXTRA_ELEMENT_USER_ID` - A `UserInfo` needs to be created first before starting the Element Activities. Pass `userId` with the intent extra.
- `EXTRA_TUTORIAL` - Displays tutorials to guide the user step-by-step. It's also a feature from `element-face-ui`. The default value is `true`.
- `EXTRA_SECONDARY_TUTORIAL` - Displays small instructional popups during/after face detection. The default value is `true`.

## Usage of the Element Activities
### Local user enrollment on an Android device
The `ElementFaceEnrollActivity` handles the user enrollment flow. When starting the `ElementFaceEnrollActivity`, it expects a `UserInfo` existing in the database. The `userId` is the main identifier for a `UserInfo`, and it is stored as a string. The `UserInfoBuilder` can be used to create a `UserInfo`. It writes metadata into a `UserInfo` per the builder methods called. If `setId()` is not called, a random userId will be assigned to the `UserInfo`. If `setName()` is not called, the userId will be used as the name for the `UserInfo`.
    ```
    UserInfo userInfo = UserInfo.builder()
              .setId("userID")
              .setName("name")
              .setName2("name2")
              .enroll(context);
    ```

In order to obtain the enrollment results, utilizing [`startActivityForResult()`](https://developer.android.com/reference/android/app/Activity#onActivityResult(int,%20int,%20android.content.Intent)) provided by the Android OS.
1. Declare a enroll request code:
    ```
    public static final int ENROLL_REQ_CODE = 12800;
    ```
1. Start the `ElementFaceEnrollActivity` with an 'Intent' with extras along with the request code:
    ```
    Intent intent = new Intent(this, ElementFaceEnrollActivity.class);
    intent.putExtra(ElementFaceEnrollActivity.EXTRA_ELEMENT_USER_ID, userInfo.userId);
    startActivityForResult(intent, ENROLL_REQ_CODE);
    ```
1. Override [`onActivityResult()`](https://developer.android.com/reference/android/app/Activity#onActivityResult(int,%20int,%20android.content.Intent)) for the results:
    ```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == ENROLL_REQ_CODE) {
        if (resultCode == Activity.RESULT_OK) {
          // User enrolled successfully
        } else {
          // Enrollment cancelled
        }
      }
    }
    ```
1. Optionally, it's possible to subclass the `ElementFaceEnrollActivity` and override `onAccountCreated()` to display alternative UI when the user enrollment has completed. Finishing the Activity is the default behavior.

### Local user authentication on an Android device
User authentication is similar to [`startActivityForResult()`](https://developer.android.com/reference/android/app/Activity#onActivityResult(int,%20int,%20android.content.Intent)) as in the `ElementFaceEnrollActivity`, and it's handled by the `ElementFaceAuthActivity`. The `ElementFaceAuthActivity` handles the user authentication flow. A `UserInfo` should exist in the database first, and user enrollment should be completed for this `UserInfo`.
1. Declare an auth request code:
    ```
    public static final int AUTH_REQ_CODE = 12801;
    ```
1. Start the `ElementFaceAuthActivity`:
    ```
    Intent intent = new Intent(this, ElementFaceAuthActivity.class);
    intent.putExtra(ElementFaceAuthActivity.EXTRA_ELEMENT_USER_ID, userInfo.userId);
    startActivityForResult(intent, AUTH_REQ_CODE);
    ```
1. Override [`onActivityResult()`](https://developer.android.com/reference/android/app/Activity#onActivityResult(int,%20int,%20android.content.Intent)) for the results:
    ```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == AUTH_REQ_CODE) {
        if (resultCode == Activity.RESULT_OK) {
          String results = data.getStringExtra(ElementFaceAuthActivity.EXTRA_RESULTS);
          if (ElementFaceAuthActivity.USER_VERIFIED.equals(results)) {
              // The user is verified
          } else if (ElementFaceAuthActivity.USER_FAKE.equals(results)) {
              // the user was spoofing
          } else {
              // The user is not verified
          }
        } else {
          // Verification cancelled
        }
      }
    }
    ```
1. If extending the `ElementFaceAuthActivity`, it's possible to override `onVerify()` and `onNotVerify()` for various purposes.

### Server side processing
The `ElementFaceCaptureActivity` captures of selfies, and posts them for user enrollment or authentication on the server side.
1. Create a class that extends the `ElementFaceCaptureActivity`.
1. Authentication is default in the `ElementFaceCaptureActivity`. Passing [`EXTRA_CAPTURE_MODE`](../app-server-processing/src/main/java/com/element/spex/SpEnrollActivity.java#L30) in the Intent with `"enroll"` for enrollment.
    ```
    Intent intent = new Intent(this, FmActivity.class);
    intent.putExtra(ElementFaceAuthActivity.EXTRA_ELEMENT_USER_ID, userId);
    intent.putExtra(EXTRA_CAPTURE_MODE, "enroll");
    startActivity(intent);
    ```
1. Override `onImageCaptured(Capture capture)` to obtain the selfie images, and examine the `Capture` from the callback which contains the JPEG image data in bytes.
1. Send the captured image data to the server if `capture.success` is true.
1. Use the [`ElementFaceEnrollTask`](https://github.com/Element1/element-face-sdk-android-internal/blob/feat/fsdk120-maker/app-server-processing/src/main/java/com/element/spex/SpEnrollActivity.java#L60) or the [`ElementFaceAuthTask`](../app-server-processing/src/main/java/com/element/spex/SpAuthActivity.java#L53) from `element-face-http` to enroll or authenticate a user. More details can be found in [element-face-http-guide](element-face-http-guide.md).
    ```
    public class FmActivity extends ElementFaceCaptureActivity {

      @Override
      public void onImageCaptured(Capture capture) {
          if (capture.success) {
              // Capturing selfie succeed. Let's proceed.
              .....
              // Use tasks from `element-face-http` to post images to server.
              new ElementFaceEnrollTask(host, userInfo).exec(callback, capture);
              .....
          } else {
              // Error handling. Capture.code specifies the actual error.
              .....
          }
      }
    }
    ```

### Questions?
If you have questions, please contact devsupport@discoverelement.com.
