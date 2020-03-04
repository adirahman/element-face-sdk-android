![element](images/element.png "element")
# Element Card SDK

The Element Card Capture SDK is an API library for creating biometrics models that can be used to authenticate a user with a proper identification card (e.g. passport, driver's license). It allows capturing and sending of both selfie photos and ID card photos for further processing on the server side. This document contains information about the process of capturing faces from selfies or cards and sending them to the server for matching.

## Card Capture SDK
Element Card Capture SDK utilizes the **ElementCardCaptureActivity** class to prepare card photos for face matching. It allows capturing card photos for further processing on the server side.

Create a child class of the ElementCardCaptureActivity class and override the **onCardPhotoTaken** (String userId, Capture capture, int totalCount) callback method to receive the card photos. The **Capture** object from this callback has a list of images, which is public. And the Image class has the bytes of the card photos that are taken. The **userId** parameter specifies the id of the user whose account the sent card photo is matched against. The **totalCount** parameter specifies the number of card photos that a user would like to take consecutively. An example implementation of the method onCardPhotoTaken in a child class of the ElementCardCaptureActivity is listed as follows:
<pre><code>
@Override
protected void onCardPhotoTaken(String userId, Capture capture, int totalCount) {
     /* use the boolean variable <b>success</b> of the <b>Capture</b> class
           to check whether the image is successfully captured */
    if (capture.success) {
        //show some message indicating the status of processing
        toastMessage(R.string.processing);

        //in the processing status, disable the capture button
        getCaptureButton().setEnabled(false);

        //Create an instance of ElementfaceEnrollTask with card match endpoint, user id, capture data and a callback
        new ElementCardMatchTask(getString(R.string.card_match_url), userId, capture)
                    .exec(callback);
        } else {
            showResult(getString(R.string.capture_failed), R.drawable.icon_focus);
        }
    }
</code></pre>

## Card Match HTTP Task
Note that by overriding the ElementCardCaptureActivity class, you can use the **ElementCardMatchTask** class to communicate with the endpoint on the backend server for card matching. To use this ElementCardMatchTask class, you will need to supply it with a **callback** instance of **ElementCardMatchTask.Callback**. An example of this instance is as follows:
<pre><code>
    private ElementCardMatchTask.Callback callback = new ElementCardMatchTask.Callback() {

        @Override
        public void onComplete(String message, Map&lt;String, Object&gt; details) {
            showResult(message, R.drawable.icon_check);
        }

        @Override
        public void onError(int code, @NonNull String message, @NonNull Map&lt;String, Object&gt; map) {
            showResult(message, R.drawable.icon_locker_white);
        }
    };
</code></pre>
