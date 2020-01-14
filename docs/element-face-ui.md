![element](images/element.png "element")
# Element Face UI
The Element Face UI is a library for customization of face capture screens. It requires [Element Face SDK](element-face-sdk-guide.md) to work.

## Installation
1. Import the AAR (`element-face-ui-[VERSION].aar`) in Android studio
1. Declare `element-face-ui` in `settings.gradle` in the project root
    ```
    include ':element-face-ui'
    ```
1. Add `element-face-ui` as a dependency `build.gradle` in the app module
    ```
    dependencies {
        implementation project(path: ':element-face-ui')
    }
    ```

## Face UI Style Guide
Element Face Detection UI components can be themed/styled based on business requirements. This document discusses some of the classes involved in configuring them.

### Notable Overrides
#### Timings

Create new value resource in `res/values` and add

```
    <integer name="key">value</integer>
```

Keys:

| Key | Default Value (ms) | Notes |
|---|---|---|
| element_gazeFlowerAnimDurMs | 1000 | Animation speed for first, _spinning flower_, part of gaze animation. |
| element_gazeLoaderAnimDurMs | 2000 | Animation speed for second, _checkmark loader_, part of gaze animation. |
| element_outlineAnimDurMs | 1800 | Animation speed for Face Detection progress outline. |

#### Colors

Create new value resource in `res/values` and add  

```
    <color name="key">#color</color>
```

Keys:

| Key | Default Value | Notes |
|---|---|---|
| element_checkmarkButton | #FF00A8FF | Checkmark button, not the checkmark itself which is always white. |
| element_flowerPetal | #80FFFFFF | Spinning circles in first half of `CheckmarkLoaderView` animation. |
| element_ringColor | #FFFFFFFF | Starting color of "ring" in `CheckmarkLoaderView`, end color is `elementFaceUiCheckmarkButton`. |
| element_outlineNormalColor | #FFB2B2B2 | Default color of Face Detection outline. |
| element_outlineDetectedColor | #FF00A8FF | Active color of Face Detection outline. |
| element_outlineErrorColor | #FFE00C0C | Error color of Face Detection outline. |
| element_stencilBgColor | #AA000000 | Color used as background of Face Detection screen. |

#### Drawables

Create new value resource in `res/values` and add  

```
    <drawable name="key">@drawable/filename</drawable>
```

Keys:

| Key | Default Value | Notes |
|---|---|---|
| element_outlineCompletionMarker | @drawable/ic_check | The icon that appears when Face Detection is complete. |

#### Strings
Go to `src/main/res/values/strings.xml` and add
```
    <string name="string_key" tools:override="true">YOUR TEXT</string>
```
String Keys:

| Key | Default Text |
|---|---|
| element_centerFace | Center face in frame |
| element_keepCentered | Keep face centered  |
| element_watchCircles | Watch the circles  |
| element_clearFaceView | Make sure we have a clear view of your face |
| element_holdPhoneEyeLevel | Hold phone at eye level |
| | |
| element_selfieMoveMessage | We\'ll automatically take a selfie then you\'ll be prompted to move your eyes to make sure it\'s you. |
| element_selfieMessage | You\'ll be asked to frame your face then we\'ll snap a selfie. |
| | |
| element_tryAgain | Please try again |
| element_matchFace | Match Face to outline |
| element_gazeFollowDot | Follow the dot |
| element_moveCamera | Move camera away from your face. Match Face to outline |
| element_holdFace | Keep your eyes open\nHold face steady |
| element_onNoFace | Please position your face inside the outline |
| element_gazeFailed | Please follow the dot |
| element_processing | Processing… |
| | |

#### Animations
We're using LottieFiles json animations. Just put the exact file name in the right directory below to override it.

https://lottiefiles.com/featured

![Animation](images/animations.gif "Animation")

From left to right:

| File Name | File Location |
|---|---|
| ic_selfie.json | src/main/res/raw/ic_selfie.json |
| ic_scan.json | src/main/res/raw/ic_scan.json |
| ic_move.json | src/main/res/raw/ic_move.json |
| ic_eye_level.json | src/main/res/raw/ic_eye_level.json |
| ic_clear_face.json | src/main/res/raw/ic_clear_face.json |
