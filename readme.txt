Hello, Spring

why use DTOS?

-> Keep the internal domain models(your database entties) hidden from the client. This protects your app internal strcutre.

-> Allow you to send or receive only the fields relevant to the client, rather than exposing all field in your domain model.

-> Request DTOs allow you to validate client input (eg. checking if fields are null or meet certain criteria)


#api-request folder :
here we will be creating different sub-folders for testing the different endpoints of a service....
-> eg. patient-service : here we will list all the api-endpoints, provided by patient-service microservice


### just curious how error is handled in spring boot

1️⃣ Exception is thrown inside Service
2️⃣ It travels up (bubbles) to Controller
3️⃣ Controller does not catch it(because there is not try-catch block)
4️⃣ Spring Framework catches it in internal layer (DispatcherServlet)
5️⃣ It tries to resolve exception using configured resolvers
6️⃣ It sees @ControllerAdvice handlers available
7️⃣ Finds method matching the exception type
8️⃣ Calls your handler to convert it → JSON Response


@Validation : It checks for each field and if its blank it will throw proper message and response with error code 400

Whenever we want that based on request type in controller, like get, put, post, patch, we want to validate the fields with incoming request
then in that case we can use the validators

When you use @Validated(Default.class) we simply means that Default.class only(which includes @NotBlank, @NotNULL...etc)
When you use @Validated(Default.class, CreatePatientValidation.class) it means validated it for both default class and alos for CreatePatientValidation.class)

So basically you have to assign groups to each field, and then use in the controller for grouping simple.
Note : you also need to remove the validation from both the getter setter of that variable



Adding Swagger : (bit complext to add but just add dependecy proper veresion which matches the compatibilty with spring boot)
Once Added and build successfully you need to visit the URL : http:://localhost:4000/v3/api-docs
// make hit to url copy the whole json
// open the swigger editor
//


#Docker :

Simplify building and deployment of apps using lightweight containers
It resolves the dependency issue  and compatibility issues
It consist of a single thing(artifact) needed to run the app on any machine

How to push particular microservice to docker container and use it?

#Step1 :
    Create a file name DockerFile
    Whatever image that we stored inside the container goes inside the folder structure is var/lib/docker
    ok got it, basically means there overrall extra memory is used by docker as space consiting of all the enviorment requried to run the application
    ->A Dockerfile is a script that tells Docker: “How to build this application into a container image”



Absolutely — let's deeply break down each line of your Dockerfile and understand what exactly is happening inside Docker 👇

🔷 Stage 1: Builder Stage
This stage compiles the Spring Boot application into a .jar file.

dockerfile
Copy code
FROM maven:3.9.9-eclipse-temurin-21 AS builder
✔ What this does:
Selects a base image with Maven + Java 21 installed.

Names this stage builder so it can be referred later.

This environment is heavy because it contains:

Linux OS

JDK 21

Maven + all build tools

Purpose: Build the app but not included in final container.

dockerfile
Copy code
WORKDIR /app
✔ What this does:
Creates & switches into a folder inside container /app

All next operations happen inside /app

Think of it as cd /app.

dockerfile
Copy code
COPY pom.xml .
✔ What this does:
Copies only pom.xml into container

Why only pom.xml first?
→ To cache dependency downloads in next step
→ So that future builds may skip downloading Maven dependencies if only source code changed

dockerfile
Copy code
RUN mvn dependency:go-offline -B
✔ What this does:
Downloads all dependencies defined in pom.xml

-B means batch mode: no user interaction required

⚡ Benefit: Faster builds later
Docker caching will reuse this layer unless dependencies change.

dockerfile
Copy code
COPY src ./src
✔ What this does:
Copy your Java code into the container

Now Maven can compile it

dockerfile
Copy code
RUN mvn clean package
✔ What this does:
Runs full Maven build

Compiles code and creates .jar under:

📌 /app/target/patient-service-0.0.1-SNAPSHOT.jar

Also cleans old builds

After this step → your backend is fully compiled inside container

🧠 Recap Stage 1
Purpose	Output
Build Java app	.jar file ready inside container
Heavy tools included (Maven/JDK)	❌ Will NOT be in final image

🟦 Stage 2: Runner Stage
This stage runs the already-built JAR.

dockerfile
Copy code
FROM openjdk:21-jdk AS runner
✔ What this does:
Starts a fresh lightweight JDK 21 image

Much smaller than Maven image

Will run the compiled .jar

➡ This is what becomes your production image.

dockerfile
Copy code
WORKDIR /app
Same purpose: working directory = /app

dockerfile
Copy code
COPY --from=builder ./app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar
✔ What this does:
Copies JAR from builder stage → into /app directory of final image

Only the final .jar is copied — no source code, no Maven, nothing extra

This is key to security & small size 👌

dockerfile
Copy code
EXPOSE 4000
✔ What this does:
Documents that application listens on port 4000

Does NOT open host port automatically

You will still need when running:

sh
Copy code
docker run -p 4000:4000 <image-name>
dockerfile
Copy code
ENTRYPOINT ["java", "-jar", "app.jar"]
✔ What this does:
Defines command that starts your Spring Boot app when container runs

ENTRYPOINT makes sure Java always runs, even if someone appends arguments

🎯 Final Outcome
Stage	Contains	Used in Final Image?
builder	Maven + source + compiled JAR	❌ No
runner	Only runtime JDK + app.jar	✔ Yes

➡ This means:

Smaller image

Faster deploy

Cleaner & more secure (no build tools exposed)

📌 What to Run
sh
Copy code
docker build -t patient-service .
docker run -p 4000:4000 patient-service
Then backend available at:
👉 http://localhost:4000

🌟 Why This Is Best Practice?
✔ Multi-stage build = optimal image size
✔ Caches Maven dependencies
✔ Builds in a clean environment
✔ Final image has only what is needed to run

If you want, I can also explain:

🔹 How Docker layers work
🔹 How to reduce image size further (e.g., JRE instead of JDK)
🔹 How to add environment variables (DB credentials)
🔹 Docker Compose to run backend + Postgres together


# gRPC :
gRPC Internal Working — Full Deep Dive
Below is the exact flow for a Unary RPC Call, from client function call → server function execution → response.

🔷 0️⃣ Before any request — Setup (One time)
A. Developer defines API in .proto
Compiler (protoc) generates code:

Client Stub: Looks like a local function call

Server Skeleton: Dispatches to real implementation

B. TCP + HTTP/2 connection established
ALPN negotiation (TLS handshake if secure)

One connection can handle many parallel RPCs (HTTP/2 streams)

▶️ 1️⃣ Client Calls a gRPC Method
User calls like normal code:

java
Copy code
UserResponse res = stub.getUser(UserRequest.newBuilder().setId(10).build());
Internally:

Stub encodes the request message object → Protocol Buffers (binary)

Assigns a Stream ID (HTTP/2 feature)

Creates metadata (headers) including:

:method = POST

content-type = application/grpc+proto

te = trailers

grpc-timeout, etc.

All headers compressed via HPACK (HTTP/2 header compression).

🔶 2️⃣ Serialization — Data Conversion
How Protobuf serialization works in detail:
Message fields get converted into:

Component	Meaning
Field Number	Identifies the field (defined in .proto)
Wire Type	Specifies encoding (varint, fixed32, string, etc.)
Value	Binary encoded data

Format:

ini
Copy code
Key = (field_number << 3) | wire_type
Value = encoded field value
Example:

proto
Copy code
message UserRequest {
  int32 id = 1;
}
Becomes (binary):

Copy code
08 0A
08 = Key for field 1 (varint)
0A = value (decimal 10 encoded)

✔ Tiny payload
✔ Very fast encode/decode
✔ Schema makes structure known on both sides

📦 3️⃣ Message Framing (gRPC-specific)
Each message wrapped in a gRPC Data Frame:

Byte	Meaning
1 byte	Compression flag
4 bytes (uint32)	Message length
N bytes	Protobuf serialized message

Example:

Copy code
0x00 00 00 00 02 08 0A
Now ready for network transfer.

🌐 4️⃣ HTTP/2 Transmission
Data written into the TCP socket:

Multiplexed streams → no blocking

Flow control window ensures optimal packet flow

Server receives:

Parses HTTP/2 headers

Passes data frames to gRPC library

🏢 5️⃣ Server Decoding + Dispatch
Flow inside the server:

HTTP/2 layer extracts message bytes

gRPC runtime removes framing

Protobuf deserializer reconstructs objects

Routed to correct service using:

Method name from header: "/UserService/GetUser"

Stream ID mapping

Finally calls your function:

java
Copy code
public UserResponse getUser(UserRequest req) {
    // business logic
}
✔ From binary → actual argument object
✔ Type-safe call using generated code

🧮 6️⃣ Server Response
Server performs reverse path:

Step	What happens
Business logic	Executes and creates response object
Serialize	Convert object → Protobuf binary
gRPC framing	Add compression flag + length
HTTP/2	Send over same Stream ID
TCP	Over network to client

Server sends trailers after data:

Final status → grpc-status: 0 (OK)

Optional grpc-message for errors

🔁 7️⃣ Client Decoding & Returning Result
Client:

Reads frames for that Stream ID

Remove gRPC framing

Deserialize Protobuf → Original object

Stub returns it like a local function return

java
Copy code
return deserializedUserResponse;
✔ No parsing JSON
✔ No reflection-based dynamic fields
✔ No multi-call overhead

🥇 Why This Whole Flow is Fast
Optimization	Benefit
Binary serialization (protobuf)	Minimal CPU + network cost
Predefined schemas	No runtime field discovery
HTTP/2 multiplexing	Avoid connection creation per request
HPACK compression	Smaller metadata → lower latency
Single TCP connection	Faster round-trips
Streaming support	No polling + real-time efficiency

Latency can improve 5×–10× vs REST JSON
Bandwidth usage can drop by 50%–90%

🔄 Visual Summary
pgsql
Copy code
Client Stub
   ↓ (Protobuf serialization)
gRPC Framing
   ↓
HTTP/2 Stream
   ↓ (TCP packets)
Server HTTP/2
   ↓
gRPC Runtime
   ↓ (Protobuf deserialization)
Server Handler (Function invoked)
   ↓ (Response generated)
Reverse path back to Client



##when you should use kafka and when you should use grpc##########

-> gRPC : 1-1 microservice communication. when you need to an immediate response (synchronous)

-> Kafka : 1 : m (1 to many) microservice communication , do not need an immediate response (asynchronous)

#### Introduce Authentication and Authorization ###########











































