## QueueWriter

### Description
This is a simple implementation of a SpringBoot based RESTful API to publish messages to a message publisher.

I have used the following components in the API:
1. SpringBoot to create a simple application that listens to an HTTP port and provides request handlers for different APIs.
2. Guice for dependency injection (See package `com.ameya.queuewriter.guice`)
3. AWS Java SDK
4. Lombok for generating boilerplate code
5. Jackson-mapper for JSON operations
6. ApacheCommons packages (lang3, collections, etc.)

Alternatives considered:
1. Vert.X instead of SpringBoot for implementing the RESTful API.  
I have been wanting to try it out just to get an idea and wrote the same API using Vert.x but didn't intend to spend too
much time on it, just picked one for this task.

2. Dagger for DI instead of Guice.  
They're not much different syntactically and I just picked one.

3. Json-org-java / Gson instead of Jackson-* for JSON.  
Again, could have used either of those but Jackson is most recently used by me so just went with it.

### Structure
The code is all under `com.ameya` package prefix with 

Start point: `com.ameya.queuewriter.Main` having the `main` method that loads the SpringBoot application.
 
API router: The class that provides API route mapping is `com.ameya.queuewriter.controllers.RequestRouter`.

Activity classes: The API handlers are all implemented as 'activities' and are found under `com.ameya.queuewriter.activities`

Message publishers: The message publishers are under `com.ameya.queuewriter.publishers`.

There is no configuration file that has all the configurable values (Queue properties, credentials, HTTP port number, etc.)
but can be created. 

### Use
Note: All APIs require `Content-Type` to be set to `application/json`.

1. Run the application locally
2. Hit endpoint on your host at port 8080, (or change the port in code as needed)
3. The root path `'/'` is bound to `list-apis` API which returns a list of APIs available. All other APIs have a
root at `/api`
4. The `describe-api` API describes the request formats of available APIs.
5. The `send-message` API will send a message to an SQS queue as configured.

#### SQS Queue configuration
You need to replace the Guice modules with appropriate modules that provide the necessary AWS JDK object plumbing. Take 
a look at `com.ameya.queuewriter.guice.AwsModule` to see the configuration. 

### Testing
I have not spent any time writing unit tests as I didn't think that was necessary for this task, but can gladly discuss
how the code can be tested.