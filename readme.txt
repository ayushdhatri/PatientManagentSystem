Hello, Spring

why use DTOS?

-> Keep the internal domain models(your database entties) hidden from the client. This protects your app internal strcutre.

-> Allow you to send or receive only the fields relevant to the client, rather than exposing all field in your domain model.

-> Request DTOs allow you to validate client inpuot (eg. checking if fields are null or meet certain criteria)


#api-request folder :
here we will be creating different sub-folders for testing the different endpoints of a service....
-> eg. patient-service : here we will list all the api-endpoints, provided by patient-service microservice
