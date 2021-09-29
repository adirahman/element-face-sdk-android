![element](images/element.png "element")
# Element SDK Resouces

Resources in the Element SDK follow the [design](https://developer.android.com/guide/topics/resources/providing-resources) of the Android framework and are categorized into strings, colors, dimens, and drawables in each module. Many of the resources are overridable for the purposes of UI customziation and language localization in the app layer. Android resources are labeled by the resource IDs (resId). In the Element SDK, resource IDs have `element_` as the prefix are externalized for convenient accesses. The following sections list the resouces IDs and their correspoinding contents in a table. Face modules refer to `face-cam`, `face-core`, and `face-ui`, as eKYC modules refer to `card OCR & review`, and `push notifiactions`.

## Face Modules
### strings
| Resource ID | Content |
| --- | --- |
| element_authLockedOut | Too many recent false attempts - please try again in %d seconds |
| element_centerFace | Center face in frame |
| element_changeLighting | Let\'s try another position with different lighting |
| element_clearFaceView | Have a clear view of your face |
| element_gazeFailed | Please follow the dot |
| element_gazeFollowDot | Follow the dot |
| element_holdFace | Keep your eyes open\nHold face steady |
| element_holdPhoneEyeLevel | Hold phone at eye level |
| element_holdStill | Hold still |
| element_howTo | How to… |
| element_initCamera | Initializing camera… |
| element_keepCentered | Keep face centered |
| element_letsGo | Let\'s Go |
| element_lookHere | Look here |
| element_move | Move |
| element_moveCloser | Move closer |
| element_moveFurther | Move further away |
| element_next | Next |
| element_positionFaceFrame | Center your face in the frame |
| element_positionFaceHere | Position face here |
| element_positionFaceInCircle | Position your face\nin the circle |
| element_processing | Processing… |
| element_ready | I\'m ready |
| element_selfie | Selfie |
| element_selfieMessage | You\'ll be asked to frame your face then we\'ll snap a selfie. |
| element_selfieMoveMessage | We\'ll automatically take a selfie then you\'ll be prompted to move your eyes to make sure it\'s you. |
| element_selfieTutorial | Let\'s take a few selfies to verify your identity |
| element_start | Start |
| element_startCamera | Start Camera |
| element_tryAgain | Try again |
| element_watchCircles | Watch the circles |

### colors
| Resource ID | Hex Code |
| --- | --- |
| element_cameraBackground | ![#000000](https://via.placeholder.com/15/000000/000000?text=+) #000000 |
| element_cameraCurtain | ![#DDFFFFFF](https://via.placeholder.com/15/DDFFFFFF/000000?text=+) #DDFFFFFF |
| element_gazeFlowerBackground | ![#DDFFFFFF](https://via.placeholder.com/15/DDFFFFFF/000000?text=+) #DDFFFFFF |
| element_gazeFlowerCheckmark | ![#FF00A8FF](https://via.placeholder.com/15/FF00A8FF/000000?text=+) #FF00A8FF |
| element_gazeFlowerOutlineDefault | ![#FFB2B2B2](https://via.placeholder.com/15/FFB2B2B2/000000?text=+) #FFB2B2B2 |
| element_gazeFlowerOutlineDetected | ![#FF00A8FF](https://via.placeholder.com/15/FF00A8FF/000000?text=+) #FF00A8FF |
| element_gazeFlowerOutlineError | ![#FFE00C0C](https://via.placeholder.com/15/FFE00C0C/000000?text=+) #FFE00C0C |
| element_gazeFlowerPetal | ![#6000A8FF](https://via.placeholder.com/15/6000A8FF/000000?text=+) #6000A8FF |
| element_gazeFlowerRing | ![#7C7C7C](https://via.placeholder.com/15/7C7C7C/000000?text=+) #7C7C7C |
| element_gazeSelfieDotOpaqueBackground | ![#ffffff](https://via.placeholder.com/15/ffffff/000000?text=+) #ffffff |
| element_gazeSelfieDotTooltip | ![#02364A](https://via.placeholder.com/15/02364A/000000?text=+) #02364A |
| element_gazeSelfieDotTranslucentBackground | ![#CC000000](https://via.placeholder.com/15/CC000000/000000?text=+) #CC000000 |
| element_gazeWaveCorner | ![#009acd](https://via.placeholder.com/15/009acd/000000?text=+) #009acd |
| element_gazeWaveCornerText | ![#ffffff](https://via.placeholder.com/15/ffffff/000000?text=+) #ffffff |
| element_gazeWaveRing | ![#993452e1](https://via.placeholder.com/15/993452e1/000000?text=+) #993452e1 |
| element_gazeWaveRingBackground | ![#77ffffff](https://via.placeholder.com/15/77ffffff/000000?text=+) #77ffffff |
| element_negativeButton | ![#FF7017](https://via.placeholder.com/15/FF7017/000000?text=+) #FF7017 |
| element_selfieDotRing | ![#FF76E8BD](https://via.placeholder.com/15/FF76E8BD/000000?text=+) #FF76E8BD |
| element_selfieDotRingDark | ![#FF11BDC6](https://via.placeholder.com/15/FF11BDC6/000000?text=+) #FF11BDC6 |
| element_statusBar | ![#27000000](https://via.placeholder.com/15/27000000/000000?text=+) #27000000 |
| element_text | ![#02364A](https://via.placeholder.com/15/02364A/000000?text=+) #02364A |
| element_textGrey | ![#788081](https://via.placeholder.com/15/788081/000000?text=+) #788081 |
| element_titleText | ![#02364A](https://via.placeholder.com/15/02364A/000000?text=+) #02364A |

### dimensions
| Resource ID | DPI |
| --- | --- |
| element_gazeCornerTextPaddingHorizontal | 15dp |
| element_gazeCornerTextPaddingVertical | 10dp |
| element_gazeSelfieDotMessage | 26sp |
| element_gazeSelfieDotTooltipMessage | 20sp |
| element_gazeWaveCorner | 160dp |
| element_gazeWaveCornerText | 80sp |
| element_gazeWaveDot | 70dp |
| element_gazeWaveDotTopOffset | 100dp |

## eKYC Modules
### strings
| Resource ID | Content |
| --- | --- |
| element_invalidDoc | Invalid doc: %s |
| element_scanFrontSide | Scan front side |
| element_scanBackSide | Scan back side |
| element_scanSignaturePage | Scan signature page |
| element_notificationProcessing | @string/element_processing |
| element_notificationDone | Complete! |
| element_looksGood | Looks good |
| element_reviewScanSubtitle | Check image for the following: |
| element_reviewScanNoBlur | No blur |
| element_reviewScanNoGlare | No glare |
| element_reviewScanEntireDoc | Entire ID is visible |
| element_reviewProgressTitle | Verifying your information |
| element_reviewProgressMessage | You can now close the app, we’ll notify you when the review is completed. |
| element_reviewProgressUploading | Uploading information |
| element_reviewProgressWaiting | Waiting for review |
| element_reviewProgressDone | Done! |
| element_reviewProgressCheckNow | Check now |
| element_reviewProgressLastCheck | Last checked: %1$s |
| element_signatureCreate | Let\'s create your\nsignature |
| element_signatureHint | Use your finger to sign |
| element_signatureClear | Clear |
| element_signatureUseImage | Use image |
| element_signatureEmpty | Please provide your signature |

### colors
| Resource ID | Hex Code |
| --- | --- |
| element_notificationText | ![#000000](https://via.placeholder.com/15/000000/000000?text=+) #000000 | 
| element_notificationIcon | ![#12B5BF](https://via.placeholder.com/15/12B5BF/000000?text=+) #12B5BF | 

### Questions?
If you have questions, please contact devsupport@discoverelement.com.
