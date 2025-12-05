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