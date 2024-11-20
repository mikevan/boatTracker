Build and Run Workflow
Compile the Code:
mvn compile

Run Tests:
mvn test

Package the Application:
mvn verify

deploy the artifact:
mvn deploy



Generates:
target/KMLProject-1.0.0.jar (with tests for debugging).
target/KMLProject-1.0.0-final.jar (clean production version).
Run the Final Application:

bash
Copy code
java -jar target/KMLProject-1.0.0-final.jar <input-file-or-directory>