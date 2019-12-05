# grpc-java-microservice ###WIP###

# Dependency 
- Gradle
- Java 8 ish

# How to run
Easiest way run navigate to root dir and run `gradle build` (if you have gradle installed else used provided `gradlew`)
1) Navigate to cloned project root dir.
2) `./gradlew build`
3) Start the server
    - `./gradlew -PmainClass=com.github.travis.grpc.greeting.server.GreetingServer execute`
4) Start the Client
    - `./gradlew -PmainClass=com.github.travis.grpc.greeting.client.GreetingClient execute`
5) Don't forget to shutdown server dont wanna leave them ports open ;)
# Output

Simple project, learning gRPC
```
Starting a Gradle Daemon, 1 busy and 1 stopped Daemons could not be reused, use --status for details

> Task :execute
Hello this is the gRPC Client
Creating stub
Good Morning T-rav
Shutting down channel

```

- SBT