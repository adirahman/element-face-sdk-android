![element](images/element.png "element")
# Element Face HTTP SDK

The Element Face HTTP SDK is an API library that contains HTTP-based task classes that communicate with endpoints in the face matching server for two face-related tasks: **face enroll** and **face authorization**. Both classes are in the pipeline SDK and extend from the class ElementHTTPTask. Each individual class will be explained in the following sections.

## Element Face Enroll Task
The **ElementFaceEnrollTask** class is to communicate with the face matching server for enrollment using a selfie. Below is an example of how to implement it in an **onImageCaptured** method in an Activity class that should **extend the ElementFaceCaptureActivity class**.

<pre><code>
import com.element.camera.ElementFaceEnrollTask;
...
     @Override
    public void onImageCaptured(Capture capture) {
        /* use the boolean variable <b>success</b> of the **Capture** class
           to check whether the image is successfully captured */
        if (capture.success) {
            //show some message indicating the status of processing
            toastMessage(R.string.processing);

            //construct a <b>UserInfo</b> object that will passed to ElementFaceEnrollTask
            String userId = getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID);
            UserInfo userInfo = ProviderUtil.getUser(getBaseContext(), getPackageName(), userId);

            //Create an instance of ElementfaceEnrollTask with the face enroll endpoint, user information, capture data and callback
            ElementFaceEnrollTask task = new ElementFaceEnrollTask(getBaseContext(), getString(R.string.face_enroll_url), userInfo, capture);
            task.exec(callback);
        } else {
                showResult(getString(R.string.capture_failed), R.drawable.icon_focus);
        }
    }
</code></pre>
Notice that when creating the instance of ElementFaceEnrollTask, an **ElementFaceEnrollTask.Callback** needs to be supplied, an example of which is listed below:
<pre><code>
    private ElementFaceEnrollTask.Callback callback = new ElementFaceEnrollTask.Callback() {
        @Override
        public void onError(int code, @NonNull String message, @NonNull Map&lt;String, Object&gt; map) {
            //show some error result
        }

        @Override
        public void onComplete(String message, UserInfo userInfo, Map&lt;String, Object&gt; details) {
           //show some success result
        }
    };
</code></pre>
## Element Face Auth Task
The **ElementFaceAuthTask** class is to communicate with the face matching server for authorization using selfies. Below is an example of how to implement it in an **onImageCaptured** method in an Activity class that should **extend the ElementFaceCaptureActivity class**.
<pre><code>
import com.element.camera.ElementFaceAuthTask;
...
    @Override
    public void onImageCaptured(Capture capture) {
        /*use the boolean variable **success** of the **Capture** class
           to check whether the image is successfully captured*/
        if (capture.success) {
            //show some message indicating the status of processing
            toastMessage(R.string.processing);

            /*supply <b>ElementFaceAuthTask</b> with
               url, userid and clientid*/
            String url = getString(R.string.face_auth_url);
            String userId = getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID);

            //Create an instance of ElementFaceAuthTask with
            //the face auth endpoint, user id, capture and a callback
            new ElementFaceAuthTask(url, userId, capture)
                    .exec(callback);
            } else {
                showResult(getString(R.string.capture_failed), R.drawable.icon_focus);
            }
    }
</code></pre>
Notice that when creating the instance of ElementFaceAuthTask, an **ElementFaceAuthTask.Callback** needs to be supplied, an example of which is listed below:
<pre><code>
private ElementFaceAuthTask.Callback callback = new ElementFaceAuthTask.Callback() {

        @Override
        public void onError(int code, @NonNull String message, @NonNull Map&lt;String, Object&gt; map) {
            //Show some error result
        }

        @Override
        public void onComplete(boolean verified, String message, Map&lt;String, Object&gt; details) {
            //Show some success result
        }
    };
</code></pre>
## Element User Query Task
The **ElementUserQueryTask** class is to communicate with the face matching server to retrieve user features. It can be used in scenarios such as when a **user previously enrolls on the face matching server and then tries to authenticates on the mobile device**, or if **the user switches to another device after enrollment**. Below is an example of how to implement this task:
<pre><code>
fun startFetch(userId: String) {
        //provide the user query endpoint
        val url = getString(R.string.query_api_url)

        //create callback
        val callback = object : ElementUserQueryTask.Callback {
            override fun onComplete(p0: UserInfo?, p1: Boolean) {
                showMessage(getString(R.string.msg_fetching_succeed))
            }

            override fun onError(p0: Int, p1: String, p2: MutableMap&lt;String, Any&gt;) {
                showMessage(getString(R.string.msg_fetching_failed))
            }
        }

        //Create an instance of ElementUserQueryTask with
        //the user query endpoint, user id, and a callback
        ElementUserQueryTask(baseContext, url, userId)
                .exec(callback)
    }
</code></pre>
