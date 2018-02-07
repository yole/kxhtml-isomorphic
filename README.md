# kxhtml-isomorphic

This sample demonstrates using Kotlin to create an application with _isomorphic HTML rendering_, with the same code
used to render HTML on the server and on the client. The application is a [multiplatform project](http://kotlinlang.org/docs/reference/multiplatform.html), 
compiled to JVM bytecode on the server side and to JS for the browser. Most of the libraries used by the application are also multiplatform.

The sample uses the following technologies:

  * [ktor](http://ktor.io) as the backend framework and the HTTP client implementation on the backend;
  * [kotlinx.html](https://github.com/Kotlin/kotlinx.html) to render the HTML;
  * [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) to serialize the data on the backend
    and deserialize it on the frontend;
  * [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) to run asynchronous operations on the frontend
    (and also as underlying technology in Ktor).
  * [kotlin.test](http://kotlinlang.org/api/latest/kotlin.test/index.html) for testing multiplatform code.
    
## Running the application

To work with the project in IntelliJ IDEA, please make sure that you have the Kotlin plugin version 1.2.20 or newer,
because earlier versions do not contain some of the changes required to work correctly with multiplatform projects.
Also, running tests under Mocha requires IntelliJ IDEA Ultimate, as IntelliJ IDEA Community Edition does not provide
any support for node.

To run the application, use `./gradlew run` or open `build.gradle` as a Gradle project in the IDE and run the
`backend` module (create a run configuration of type Gradle, select "kxhtml-isomorphic:backend" module and the "run" task). 
Then open http://localhost:8080 in the browser. The application will start showing the most recent commits to the 
Kotlin project. The first five commits are rendered on the server; subsequent commits are serialized to JSON and 
rendered by the client.

To run the tests for the shared code, right-click the `DateTest` class in the IDE and select the Run option.
To run DateTest under Mocha, you need to first run `npm install` in the root directory of the project.

## Source code highlights

The [shared module](https://github.com/yole/kxhtml-isomorphic/tree/master/shared/src/main/kotlin) contains the
cross-platform part of the application, and is compiled both to the JVM and JS. It contains the following main parts:

  * The [Message class](https://github.com/yole/kxhtml-isomorphic/blob/master/shared/src/main/kotlin/Message.kt) is the
    data model for the application (just a single class in this case).
  * The [Date class](https://github.com/yole/kxhtml-isomorphic/blob/master/shared/src/main/kotlin/Date.kt) defines
    a multiplatform API for working with dates. This demonstrates using the `expect` and `actual` keywords to define
    a set of APIs with platform-specific implementations.
  * The [renderMessage function](https://github.com/yole/kxhtml-isomorphic/blob/master/shared/src/main/kotlin/Render.kt)
    renders the message to HTML. It is called from both the front-end and the back-end code.
    
The [DateTest class](https://github.com/yole/kxhtml-isomorphic/blob/master/shared/src/test/kotlin/DateTest.kt) contains
some simple tests for the date logic. The tests can run under both JUnit on the JVM and Mocha on Node.

The [kxhtml-isomorphic-jvm](https://github.com/yole/kxhtml-isomorphic/tree/master/kxhtml-isomorphic-jvm) and
[kxhtml-isomorphic-js](https://github.com/yole/kxhtml-isomorphic/tree/master/kxhtml-isomorphic-js) modules contain
platform-dependent implementations of the date logic. The JVM implementation uses the Calendar class from the JDK,
while the JS implementation delegates to the browser-provided Date class.

The [backend module](https://github.com/yole/kxhtml-isomorphic/tree/master/backend) is a simple Ktor application
that renders the initial HTML page and exposes a JSON API for retrieving updates. It is also responsible for serving
the JavaScript code of the frontend module (see the `copyBundleJs` task in its build.gradle to see how this is set up).

The [frontend module](https://github.com/yole/kxhtml-isomorphic/tree/master/frontend) contains the code which runs in
the browser (periodically calling the backend to fetch new data and delegating to the shared rendering code to display
the received data on the page).
