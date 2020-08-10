![element](images/element.png "element")
# Element Face HTTP SDK

The Element HTTP Tasks is a collection of HTTP-based utility APIs that communicate with endpoints in the backend server for **face enrollment**, **face authorization**, and **user inquiry**.

### `ElementHttpTask`
The ElementHttpTask is abstract and is the super class of the implementation of the Element Http task family. It handles POST HTTP calls to the backend server. Payloads in both requests and responses are JSON based. The ElementHttpTask is in the builder design pattern. It equips methods to add data to HTTP headers or payload. Invoking post() at the end initiates the process. Server responses can be received synchronously, or asynchronously if a callback is provided.

### `TaskCallback`
The super interface of the Http callbacks to receive server responses asynchronously. It is used in post() by The ElementHttpTask. It contains only a method for general error handling. Child classes of the ElementHttpTask implements its own callback for specific cases.

### `ElementFaceEnrollTask`
The ElementFaceEnrollTask initiates HTTP POST calls to the backend for server side user enrollment. The Payload in the request includes selfies and metadata captured by the ElementFaceCaptureActivity.

### `ElementFaceAuthTask`
Similar to the ElementFaceEnrollTask, the ElementFaceAuthTask initiates HTTP POST calls to the backend for server side user authentication. Commonly both ElementFaceEnrollTask and ElementFaceAuthTask are utilized in onImageCaptured() by the ElementFaceCaptureActivity if selfies and metadata are passed local liveness checks.

### `ElementUserQueryTask`
The ElementUserQueryTask communicates with the server and asks for user metadata including features which will be wrapped in a UserInfo and saved to the local database. If user features are saved, the user perform local authentication via the ElementFaceAuthActivity afterwards.
