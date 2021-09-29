![element](images/element.png "element")
# Element SelfieDot
The Element Face SDK (the SDK) is an API library for creating biometric models that can be used to authenticate users. This document contains information for enabling the latest Face Gaze functionality - SelfieDot. 

## Dependencies
- Requires Element Face SDK
Please refer to [Element Face SDK doc](element-face-sdk-guide.md) for details about set up and usage.

### Activity declaration
SelfieDot supports transparent Authentication so the Auth Activity should be using a Transparent theme, set in the manifest like so.
```
    <manifest>
      .....
      <application android:name=".MainApplication">
        .....
        <activity android:name="com.element.camera.LocalAuthTransparentActivity"
        android:theme="@style/Theme.Transparent"/>
        .....
      </application>
    </manifest>
```

Here is an exampe of a working theme:
```
<style name="Theme.Transparent" parent="AppTheme.NoActionBar">
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:colorBackgroundCacheHint">@null</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowAnimationStyle">@null</item>
    </style>
```

## Usage of the Element Activities
When starting an Enrollment or Auth Activity just add this Extra: 
 ```
 intent.putExtra(ElementFaceAuthActivity.EXTRA_UI_DELEGATE, "selfieDot")
 ```

Otherwise, this feature functions exactly the same as normal Face SDK activities. Again, please refer to [Element Face SDK doc](element-face-sdk-guide.md) for further instructions.

### Questions?
If you have questions, please contact devsupport@discoverelement.com.
