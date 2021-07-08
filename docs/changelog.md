![element](images/element.png "element")
## Element FM SDK - Changelog

#### Version: 1.5.1.26474
[Jul 08, 2021]
* Improved the ease of use mode

#### Version: 1.5.0.26096
[Jun 22, 2021]
* Built on Android 11 (API level 30)
* Introduced new SelfieDot UI
* Enhanced liveness detection modes
* Separated the 32-bit resources into a standalone AAR package
* Reworked behaviors when starting the face activities via `startActivityForResult`
* Provided new APIs to obtain the previous authentication records
* Enabled push notifications for the eKYC solution

#### Version: 1.4.0.22471
[Jan 20, 2021]
* The eKYC solution - released the Element Card SDK for the OCR & Review services

#### Version: 1.3.0
[Aug 8, 2020]
* Updated liveness detection UI
* New theming
* Enhanced security and code obfuscation
* User management with ElementUserUtils

#### Version: 1.2.3
[Mar 25, 2020]
* Adjusted background services sliglty

#### Version: 1.2.2
[Mar 17, 2020]
* Imrpoved UX for liveness detection

#### Version: 1.2.1
[Mar 10, 2020]
* Hotfix to post scores to the backend

#### Version: 1.2.0
[Mar 3, 2020]
* Introduced [`element-face-http`](element-face-http-guide.md) and [`element-card-sdk`](element-card-sdk-guide.md)
* Made `element-face-cam` a standalone module. It's possible to import `element-face-cam` for face detection only. Please refer to [`app-face-detection`](../app-face-detection).
* `ElementFaceCaptureActivity` can handle enrollment and authentication for server processing.
* Updated Face Capture API. Simplified the callback with only one [`Capture`](element-face-sdk-guide.md#user-face-matching-on-server) as the argument.
* Enabling face tutorials by default

#### Version: 1.1.3
[Jan 7, 2020]
* Restored EXTRA_SECONDARY_TUTORIAL

#### Version: 1.1.2
[Dec 20, 2019]
* Supported the Android 10 OS
* Rolled back EXTRA_SECONDARY_TUTORIAL

#### Version: 1.1.1
[Nov 1, 2019]
* Supported more UI customization - please refer to [element-face-ui](./element-face-ui.md)

#### Version: 1.1.0
[October 15, 2019]
* New modularized SDK architecture to achieve flexible project frameworks and to fulfill various requirements
* Introducing the redesigned AARs
  * element-face-cam
  * element-face-core
  * element-face-support
  * element-face-ui

#### Older Versions
* [Element FM SDK](https://github.com/Element1/element-android-examples/blob/master/element-fm-sdk-example/changelog.md)
* [Element Face SDK](https://github.com/Element1/element-android-examples/blob/master/element-face-sdk-example/changelog.md)
