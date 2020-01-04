# grpc-java-microservice ###WIP###

# Dependency 
- Gradle
- Java 8 ish

# How to run
Easiest way run navigate to root dir and run `gradle build` (if you have gradle installed else used provided `gradlew`) NOTE I added some ssl security to grpc calls please see the `ssl/instructions.sh` to generate your certs.
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
Starting a Gradle Daemon, 2 busy and 1 stopped Daemons could not be reused, use --status for details

> Task :execute
Hello this is the gRPC Client
Sending: Travis
Sending: Martin
Sending: Marky mark
Sending: Jonny
Response from server: Hello, Travis
Response from server: Hello, Martin
Response from server: Hello, Marky mark
Response from server: Hello, Jonny
Shutting down channel
```

## Calculator client server stuff

### Client
I commented out most functions uncomment them to run.
`./gradlew -PmainClass=com.github.travis.grpc.calculator.client.CalculatorClient execute`

### Server
`./gradlew -PmainClass=com.github.travis.grpc.calculator.server.CalculatorServer execute`

### Output
```
Sending Nunber: 1
Sending Nunber: 2
Got a new maximum value: 1
Got a new maximum value: 2
Sending Nunber: 5
Got a new maximum value: 5
Sending Nunber: 1
Sending Nunber: 10
Got a new maximum value: 10
Sending Nunber: 12
Got a new maximum value: 12
Sending Nunber: 9
Sending Nunber: 7
Sending Nunber: 6
Got a new maximum value: 12
Server Finished sending data
```
- SBT