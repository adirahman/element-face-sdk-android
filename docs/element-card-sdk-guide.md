![element](images/element.png "element")
# Element Card SDK

The Element Card SDK is an API library that enables ID card / document scanning in order to obtain user information from optical character recognition (OCR). The Element Card SDK is an add-on module to the Element Face SDK. It is required to [configure the Face SDK](element-face-sdk-guide.md) before starting with the Card SDK.

## Version Support
### Android device, development environments, and Element Dashboard
- The minimum Android device requirement is Android 5.0 or API 21 (Android OS Lollipop and up). A minimum of 2GB of RAM is recommended for optimal performance.
- Android Target SDK Version 29, Build Tool Version 29.0.3, and AndroidX.
- Please refer to [prerequisites](prerequisites.md) to configure the development environments.
- The EAK for the Card SDK requires extra settings. This step has not automated yet. Please contact Element with your EAK if you would like to try the Card SDK.

### Dependencies
- Kotlin Coroutines: 1.4.0
- AndroidX Kotlin Coroutines: 1.4.0

References for dependencies can be found in the sample project at [app-card](../app-card/build.gradle).

## SDK Integration
### Initialize the Element Card SDK
Create a class which extends [android.app.Application](https://developer.android.com/reference/android/app/Application), and initialize the `ElementCardSDK` in `onCreate()` method:
```
open class CardApplication : Application() {
  override fun onCreate() {
      super.onCreate()
      ElementCardSDK.initSDK(this)
  }
}
```

### DocTypeId & CardTokens
Each doc scan requires a docTypeId. The docTypeId instructs the OCR services for the process and returns. Before starting card scan or loading from photo gallery, it's required to specify a docTypeId. EAK maintains a list of docTypeId. There are a few methods in the `ElementCardSDK` to access the docTypeId list.

The card tokens contain specific infomraion about each card scan. Card tokens can be obtained after completing card scan. Alternatively, it's also possible to obtain card tokens by passing JPEG byte arrays to `ElementCardSDK.applyForOcrCardTokens`.

The docTypeId and cardTokens are interchangeable. The `ElementCardSDK` is the portal for the transformation.

### ID card / document scanning
The `ElementCardScanActivity` handles ID card and document scanning. It's required to create a UserInfo object before starting the activity. Use [startActivityForResult()](https://developer.android.com/reference/android/app/Activity#onActivityResult(int,%20int,%20android.content.Intent)) provided by the Android OS to obtain the scar scan results.
1. Declare a card request code:
    ```
    val CARD_REQ_CODE = 12802
    ```
1. Start the `ElementCardScanActivity` with an 'Intent' with extras along with the request code:
    ```
    val intent = Intent(this, ElementCardScanActivity::class.java).apply {
        putExtra(EXTRA_ELEMENT_USER_ID, user?.userId)
        putExtra(EXTRA_DOC_TYPE_ID, selectedDocument.first)
    }
    startActivityForResult(intent, CARD_REQ_CODE)
    ```
1. Override [`onActivityResult()`](https://developer.android.com/reference/android/app/Activity#onActivityResult(int,%20int,%20android.content.Intent)) for the results. The card scan results are called cardTokens. Retrieve it from the result intent with `EXTRA_CARD_TOKENS`. CardTokens are in a key-value map.
    ```
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ...
        if (requestCode == CARD_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Card scan successfully.
                val cardTokens = data?.getSerializableExtra(EXTRA_CARD_TOKENS)
            } else {
                // Unable to scan cards
            }
        }
    }
    ```
1. Optionally, it's possible to subclass the `ElementCardScanActivity` and override `onCardPhotoTaken(userId: String, capture: Capture)` to display alternative UI when card scanning has completed. Finishing the Activity is the default behavior.

1. CardTokens are needed to request the OCR & Review services. Or they can be assigned to a user. When starting the `ElementCardScanActivity`, if a valid userId is specified in the `Intent` with `EXTRA_ELEMENT_USER_ID`, the tokens will be assigned to the userId automatically at the end of success card scanning. Otherwise cardTokens can be linked to any user by calling,
    ```
    ElementCardSDK.assignCardTokens(baseContext, user?.userId, loadCardTokens())
    ```

### Starting OCR & Review services
The `ElementOcrService` is an Android [IntentService](https://developer.android.com/reference/android/app/IntentService) processing the network traffic in the background. It posts images and user metadata to the Element dashboard for OCR & Review services. Calling the `ElementOcrService` after card scanning completed to start the service.
```
    fun startOcrService(context: Context,
                        userId: String,
                        receiver: ElementServiceResultReceiver,
                        extraHeader: Map<String, String>? = null,
                        extraPayload: Map<String, Any>? = null)
```
  * `context`: The Context
  * `userId`: The UserInfo object associated with the userId
  * `receiver`: Callback receiver. An instance of `ElementServiceResultReceiver`
  * `extraHeader`: Optional HTTP headers for customization
  * `extraPayload`: Optional HTTP params for customization

### Checking OCR & Review status
The `ElementOcrReviewResultTask` is one of the [Element HTTP Tasks](element-face-http-guide.md) in order to retrieve OCR & Review results from the Element server. The results are returned as in a Bundle. Call `getResult()` to access it.
```
    ElementOcrReviewResultTask(this, userId).getResult {
        showMessage(it.toString())
    }
```

### Receive the OCR & Review results
The `ElementServiceResultReceiver` implements the Android [ResultReceiver](https://developer.android.com/reference/android/os/ResultReceiver) interface for receiving a callback from The `ElementOcrService`. Check the `resultCode` first. Server responses are properly parsed into a [Bundle](https://developer.android.com/reference/android/os/Bundle) with key-value pairs.
```
    ElementServiceResultReceiver { resultCode, resultData ->
      when (resultCode) {
        OCR_RESULT_OK -> {
          // You code
        }
        OCR_RESULT_ERROR -> {
          // You code
        }
      }
    }
```

### Request OCR services with genuine images
The SDK allows to load JPG images from other resources for OCR requests, including photo gallery. Pass the images in bytes with `applyForOcrCardTokens`.
```
    fun applyForOcrCardTokens(context: Context,
                              userId: String?,
                              docTypeId: String,
                              cardPhotos: Map<String, ByteArray>): Map<String, String>
```
  * `context`: The Context
  * `userId`: The UserInfo object associated with the userId
  * `cardPhotos`: The map of the card photos. Card ares are the keys associated wit the image bytes.
The method returns card tokens. If userId is provided, the tokens will be assigned automatically.

### Questions?
If you have questions, please contact devsupport@discoverelement.com.
