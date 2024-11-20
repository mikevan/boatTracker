# Application Documentation

This repository contains multiple implementations of a program that processes textual input to generate KML (Keyhole Markup Language) files. The implementations include:

1. Perl version
2. Python version
3. Java-based standalone application
4. Java-based servlet
5. Java-based microservice

This README provides instructions on installing and using each version, along with details about Maven targets for the Java applications.

---

## Table of Contents

1. [Installation Requirements](#installation-requirements)
2. [Perl Version](#perl-version)
3. [Python Version](#python-version)
4. [Java-Based Application](#java-based-application)
5. [Java-Based Servlet](#java-based-servlet)
6. [Java-Based Microservice](#java-based-microservice)
7. [Maven Targets for Java Applications](#maven-targets-for-java-applications)
8. [Additional Resources](#additional-resources)

---

## Installation Requirements

To use the application, ensure you have the following installed:

- **Perl**: [Installation Instructions](https://www.perl.org/get.html)
- **Python (version 3.6 or higher)**: [Installation Instructions](https://www.python.org/downloads)
- **Java Development Kit (JDK 11 or higher)**: [Installation Instructions](https://openjdk.org/install/)
- **Apache Maven** (for Java applications): [Installation Instructions](https://maven.apache.org/install.html)

---

## Perl Version

1. Open a terminal and navigate to the directory containing the Perl script.

2. Run the script using the following command:
   ```bash
   perl script_name.pl <input-file-or-directory>
    ```
	  
Replace <input-file-or-directory> with:

The path to a specific file containing input text.
A directory path to process all .txt files within the directory.

3. Input Format: The input file(s) must contain text in the following format:

 ```text
HN127/24 SOUTH CHINA SEA MILITARY EXERCISES IN AREA BOUNDED BY THE LINES JOINING 20-34.32N 110-54.27E、20-35.73N 110-59.82E、20-05.98N 111-10.75E AND 20-04.33N 111-05.35E FROM 311300 UTC OCT TO 010700 UTC NOV. ENTERING PROHIBITED. HAINAN MSA CHINA.
 ```

4. Output:

The script processes the input and generates a KML file named combined_military_exercise_areas.kml in the same directory.
 
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
    <name>Ship Tracker</name>
    <description>A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
    <Style id="thinBlackLine">
      <LineStyle>
        <color>87000000</color>
        <width>1</width>
      </LineStyle>
    </Style>
    <Style id="transparentPolyStyle">
      <PolyStyle>
        <color>80ADD8E6</color>
        <outline>1</outline>
      </PolyStyle>
    </Style>
    <Placemark>
      <name>HN127/24 SOUTH CHINA SEA MILITARY EXERCISES</name>
      <description>ENTERING PROHIBITED. HAINAN MSA CHINA.</description>
      <styleUrl>#thinBlackLine</styleUrl>
      <styleUrl>#transparentPolyStyle</styleUrl>
      <TimeSpan>
        <begin>2024-10-31T13:00:00Z</begin>
        <end>2024-11-01T07:00:00Z</end>
      </TimeSpan>
      <Polygon>
        <outerBoundaryIs>
          <LinearRing>
            <coordinates>
              110.904500,20.572000,0
              110.997000,20.595500,0
              111.179167,20.099667,0
              111.089167,20.072167,0
            </coordinates>
          </LinearRing>
        </outerBoundaryIs>
      </Polygon>
    </Placemark>
  </Document>
</kml>
```
	
5. Notes:

Ensure the input file(s) are formatted correctly for the script to work properly.
View the generated KML file using Google Earth or any KML viewer.

## Python Version

### Installation
1. Install Python (version 3.6 or higher).
   - Check if Python is installed by running:
     ```bash
     python3 --version
     ```
   - If not installed, download and install Python from the [official Python website](https://www.python.org/downloads).

2. Ensure the required dependencies are installed. If the script has dependencies (e.g., for XML or KML processing), install them using `pip`:
   ```bash
   pip install -r requirements.txt
    ```

(If a requirements.txt file is not provided, no additional dependencies are required.)

Copy the provided Python script (script_name.py) to your working directory.
Usage
Open a terminal and navigate to the directory containing the script.

Run the script using the following command:

```bash
python3 script_name.py <input-file-or-directory>
```
Replace <input-file-or-directory> with:

The path to a specific file containing input text.
A directory path to process all .txt files within the directory.
Input Format: The input file(s) must contain text in the following format:

```text
HN127/24 SOUTH CHINA SEA MILITARY EXERCISES IN AREA BOUNDED BY THE LINES JOINING 20-34.32N 110-54.27E、20-35.73N 110-59.82E、20-05.98N 111-10.75E AND 20-04.33N 111-05.35E FROM 311300 UTC OCT TO 010700 UTC NOV. ENTERING PROHIBITED. HAINAN MSA CHINA.
 ```

4. Output:

    - The script processes the input and generates a KML file named combined_military_exercise_areas.kml in the same directory.
    - Example KML output:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
    <name>Ship Tracker</name>
    <description>A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
    <Style id="thinBlackLine">
      <LineStyle>
        <color>87000000</color>
        <width>1</width>
      </LineStyle>
    </Style>
    <Style id="transparentPolyStyle">
      <PolyStyle>
        <color>80ADD8E6</color>
        <outline>1</outline>
      </PolyStyle>
    </Style>
    <Placemark>
      <name>HN127/24 SOUTH CHINA SEA MILITARY EXERCISES</name>
      <description>ENTERING PROHIBITED. HAINAN MSA CHINA.</description>
      <styleUrl>#thinBlackLine</styleUrl>
      <styleUrl>#transparentPolyStyle</styleUrl>
      <TimeSpan>
        <begin>2024-10-31T13:00:00Z</begin>
        <end>2024-11-01T07:00:00Z</end>
      </TimeSpan>
      <Polygon>
        <outerBoundaryIs>
          <LinearRing>
            <coordinates>
              110.904500,20.572000,0
              110.997000,20.595500,0
              111.179167,20.099667,0
              111.089167,20.072167,0
            </coordinates>
          </LinearRing>
        </outerBoundaryIs>
      </Polygon>
    </Placemark>
  </Document>
</kml>
```

5. Notes:

The script supports both individual files and directories containing multiple .txt files.
Ensure the input text files are correctly formatted as shown above for accurate KML generation.
View the generated KML file using Google Earth or any KML viewer.

## Java-Based Application

### Installation
1. Install the following:
   - **Java Development Kit (JDK 11 or higher)**:
     - Verify installation:
       ```bash
       java -version
       ```
     - If not installed, download and install from the [official OpenJDK website](https://openjdk.org/install/).
   - **Apache Maven**:
     - Verify installation:
       ```bash
       mvn -version
       ```
     - If not installed, follow the [official Maven installation instructions](https://maven.apache.org/install.html).

2. Clone this repository or download the Java application directory to your system.

### Usage
1. **Compile the Application**:
   - Navigate to the application directory:
     ```bash
     cd <path-to-java-application-directory>
     ```
   - Compile the application using Maven:
     ```bash
     mvn compile
     ```

2. **Run the Application**:
   - Use the following Maven command to execute the Java application:
     ```bash
     mvn exec:java -Dexec.mainClass="main.KMLGenerator" -Dexec.args="<input-file-or-directory>"
     ```
   - Replace `<input-file-or-directory>` with:
     - The path to a specific file containing input text.
     - A directory path to process all `.txt` files within the directory.

3. **Input Format**:
   The input file(s) must contain text in the following format:

```text
HN127/24 SOUTH CHINA SEA MILITARY EXERCISES IN AREA BOUNDED BY THE LINES JOINING 20-34.32N 110-54.27E、20-35.73N 110-59.82E、20-05.98N 111-10.75E AND 20-04.33N 111-05.35E FROM 311300 UTC OCT TO 010700 UTC NOV. ENTERING PROHIBITED. HAINAN MSA CHINA.
```
4. **Output**:
- The application generates a KML file named `combined_military_exercise_areas.kml` in the `target` directory.
- Example KML output:
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
      <name>Ship Tracker</name>
      <description>A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
      <Style id="thinBlackLine">
        <LineStyle>
          <color>87000000</color>
          <width>1</width>
        </LineStyle>
      </Style>
      <Style id="transparentPolyStyle">
        <PolyStyle>
          <color>80ADD8E6</color>
          <outline>1</outline>
        </PolyStyle>
      </Style>
      <Placemark>
        <name>HN127/24 SOUTH CHINA SEA MILITARY EXERCISES</name>
        <description>ENTERING PROHIBITED. HAINAN MSA CHINA.</description>
        <styleUrl>#thinBlackLine</styleUrl>
        <styleUrl>#transparentPolyStyle</styleUrl>
        <TimeSpan>
          <begin>2024-10-31T13:00:00Z</begin>
          <end>2024-11-01T07:00:00Z</end>
        </TimeSpan>
        <Polygon>
          <outerBoundaryIs>
            <LinearRing>
              <coordinates>
                110.904500,20.572000,0
                110.997000,20.595500,0
                111.179167,20.099667,0
                111.089167,20.072167,0
              </coordinates>
            </LinearRing>
          </outerBoundaryIs>
        </Polygon>
      </Placemark>
    </Document>
  </kml>
  ```

5. **Notes**:
- Ensure that input text files are correctly formatted as shown above.
- The application supports both single files and directories containing multiple `.txt` files.
- View the generated KML file using Google Earth or any KML viewer.

## Java-Based Servlet

### Installation
1. Install the following:
   - **Java Development Kit (JDK 11 or higher)**:
     - Verify installation:
       ```bash
       java -version
       ```
     - If not installed, download and install from the [official OpenJDK website](https://openjdk.org/install/).
   - **Apache Maven**:
     - Verify installation:
       ```bash
       mvn -version
       ```
     - If not installed, follow the [official Maven installation instructions](https://maven.apache.org/install.html).
   - **Servlet Container**:
     - Install a servlet container like [Apache Tomcat](https://tomcat.apache.org/).

2. Clone this repository or download the Java servlet directory to your system.

### Usage
1. **Package the Servlet**:
   - Navigate to the servlet project directory:
     ```bash
     cd <path-to-java-servlet-directory>
     ```
   - Package the application as a WAR file:
     ```bash
     mvn package
     ```
   - The generated WAR file will be located in the `target/` directory with a name like:
     ```
     KMLProject-1.0.0.war
     ```

2. **Deploy the WAR File**:
   - Copy the WAR file to your servlet container's `webapps` directory (e.g., for Apache Tomcat):
     ```bash
     cp target/KMLProject-1.0.0.war /path/to/tomcat/webapps/
     ```
   - Start or restart the servlet container.

3. **Access the Servlet**:
   - Open a browser and navigate to the servlet endpoint:
     ```
     http://localhost:8080/KMLProject-1.0.0/generateKML
     ```
   - Use an API testing tool like `curl` or Postman to send a POST request to the servlet with the input text.

4. **Input Format**:
   The input must be sent as plain text in the following format:

```text
HN127/24 SOUTH CHINA SEA MILITARY EXERCISES IN AREA BOUNDED BY THE LINES JOINING 20-34.32N 110-54.27E、20-35.73N 110-59.82E、20-05.98N 111-10.75E AND 20-04.33N 111-05.35E FROM 311300 UTC OCT TO 010700 UTC NOV. ENTERING PROHIBITED. HAINAN MSA CHINA.
```

5. **Output**:
- The servlet responds with a dynamically generated KML file.
- Example KML output:
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
      <name>Ship Tracker</name>
      <description>A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
      <Style id="thinBlackLine">
        <LineStyle>
          <color>87000000</color>
          <width>1</width>
        </LineStyle>
      </Style>
      <Style id="transparentPolyStyle">
        <PolyStyle>
          <color>80ADD8E6</color>
          <outline>1</outline>
        </PolyStyle>
      </Style>
      <Placemark>
        <name>HN127/24 SOUTH CHINA SEA MILITARY EXERCISES</name>
        <description>ENTERING PROHIBITED. HAINAN MSA CHINA.</description>
        <styleUrl>#thinBlackLine</styleUrl>
        <styleUrl>#transparentPolyStyle</styleUrl>
        <TimeSpan>
          <begin>2024-10-31T13:00:00Z</begin>
          <end>2024-11-01T07:00:00Z</end>
        </TimeSpan>
        <Polygon>
          <outerBoundaryIs>
            <LinearRing>
              <coordinates>
                110.904500,20.572000,0
                110.997000,20.595500,0
                111.179167,20.099667,0
                111.089167,20.072167,0
              </coordinates>
            </LinearRing>
          </outerBoundaryIs>
        </Polygon>
      </Placemark>
    </Document>
  </kml>
  ```

6. **Notes**:
- Ensure the servlet container is running on the correct port (default: 8080 for Tomcat).
- Use the input format provided to ensure accurate KML generation.
- View the generated KML file using Google Earth or any KML viewer.

## Java-Based Microservice

### Installation
1. Install the following:
   - **Java Development Kit (JDK 11 or higher)**:
     - Verify installation:
       ```bash
       java -version
       ```
     - If not installed, download and install from the [official OpenJDK website](https://openjdk.org/install/).
   - **Apache Maven**:
     - Verify installation:
       ```bash
       mvn -version
       ```
     - If not installed, follow the [official Maven installation instructions](https://maven.apache.org/install.html).

2. Clone this repository or download the Java microservice directory to your system.

### Usage
1. **Compile and Package the Microservice**:
   - Navigate to the microservice project directory:
     ```bash
     cd <path-to-java-microservice-directory>
     ```
   - Compile and package the application using Maven:
     ```bash
     mvn package
     ```
   - The generated JAR file will be located in the `target/` directory with a name like:
     ```
     KMLService-1.0.0.jar
     ```

2. **Run the Microservice**:
   - Start the microservice using the following command:
     ```bash
     java -jar target/KMLService-1.0.0.jar
     ```
   - The service will start on `http://localhost:8080` by default.

3. **Access the Service**:
   - Use an API testing tool like `curl` or Postman to send a POST request to the microservice endpoint:
     ```
     POST http://localhost:8080/kml/generate
     ```
   - Set the `Content-Type` header to `text/plain` and include the input text in the body.

4. **Input Format**:
   The input must be sent as plain text in the following format:

```text
HN127/24 SOUTH CHINA SEA MILITARY EXERCISES IN AREA BOUNDED BY THE LINES JOINING 20-34.32N 110-54.27E、20-35.73N 110-59.82E、20-05.98N 111-10.75E AND 20-04.33N 111-05.35E FROM 311300 UTC OCT TO 010700 UTC NOV. ENTERING PROHIBITED. HAINAN MSA CHINA.
```


5. **Output**:
- The microservice responds with a dynamically generated KML file.
- Example KML output:
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
      <name>Ship Tracker</name>
      <description>A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
      <Style id="thinBlackLine">
        <LineStyle>
          <color>87000000</color>
          <width>1</width>
        </LineStyle>
      </Style>
      <Style id="transparentPolyStyle">
        <PolyStyle>
          <color>80ADD8E6</color>
          <outline>1</outline>
        </PolyStyle>
      </Style>
      <Placemark>
        <name>HN127/24 SOUTH CHINA SEA MILITARY EXERCISES</name>
        <description>ENTERING PROHIBITED. HAINAN MSA CHINA.</description>
        <styleUrl>#thinBlackLine</styleUrl>
        <styleUrl>#transparentPolyStyle</styleUrl>
        <TimeSpan>
          <begin>2024-10-31T13:00:00Z</begin>
          <end>2024-11-01T07:00:00Z</end>
        </TimeSpan>
        <Polygon>
          <outerBoundaryIs>
            <LinearRing>
              <coordinates>
                110.904500,20.572000,0
                110.997000,20.595500,0
                111.179167,20.099667,0
                111.089167,20.072167,0
              </coordinates>
            </LinearRing>
          </outerBoundaryIs>
        </Polygon>
      </Placemark>
    </Document>
  </kml>
  ```

6. **Notes**:
- Ensure the service is running on the correct port (`8080` by default).
- Use the provided input format to ensure accurate KML generation.
- View the generated KML file using Google Earth or any KML viewer.
- 
## Maven Targets for Java Applications

The Java-based implementations (standalone application, servlet, and microservice) use Maven to handle building, packaging, and running the application. Below are the commonly used Maven targets for these projects.

1. Compile the Code
To compile the source code:
```bash
mvn compile
```
This command compiles the Java source files and ensures all dependencies are downloaded and properly set up.

2. Run the Application
For the standalone Java application, use the following command to run the program:
```bash
mvn exec:java -Dexec.mainClass="main.KMLGenerator" -Dexec.args="<input-file-or-directory>"
```

3. Replace <input-file-or-directory> with:

The path to a file containing input text.
A directory containing multiple .txt files.

4. Package the Application
To package the project into a JAR or WAR file:
```bash
mvn package
```

For the standalone application and microservice, this creates a JAR file (e.g., target/KMLService-1.0.0.jar).
For the servlet, this creates a WAR file (e.g., target/KMLProject-1.0.0.war).

5. Run Unit Tests
To run the unit tests for any of the Java-based implementations:

```bash
mvn test
```

This command runs all unit tests and generates a test report. It ensures the application logic is functioning as expected before deployment.

6. Clean Up
To remove previously built files and reset the build environment:

```bash
mvn clean
```

This deletes the target/ directory and all compiled files.

## Notes
Ensure Maven is installed and configured on your system.
Use mvn clean before running mvn package to ensure a fresh build.
Always run mvn test to validate the application logic before packaging and deployment.

## Additional Resources

### Installing Required Software
If you need to install any of the required software mentioned in this documentation, follow these official guides:

- **Perl**: [Installation Guide](https://www.perl.org/get.html)
  - Includes instructions for Windows, macOS, and Linux.
  
- **Python**: [Python Downloads](https://www.python.org/downloads)
  - Choose a version compatible with your system (3.6 or higher is required).
  - Installation includes `pip`, the Python package manager.

- **Java Development Kit (JDK)**: [OpenJDK Installation](https://openjdk.org/install/)
  - Follow the guide for your operating system to install JDK 11 or higher.
  
- **Apache Maven**: [Maven Installation](https://maven.apache.org/install.html)
  - Includes setup instructions for Windows, macOS, and Linux.

- **Apache Tomcat** (for the servlet implementation): [Tomcat Downloads](https://tomcat.apache.org/)
  - Download and install a version compatible with your Java installation.

---

### KML File Viewers
The generated KML files can be opened with the following tools:
- **Google Earth**: [Download Here](https://www.google.com/earth/)
- **ArcGIS Earth**: [Download Here](https://www.esri.com/en-us/arcgis/products/arcgis-earth/overview)
- Any KML-compatible GIS application.

---

### Troubleshooting
- If you encounter issues with any implementation:
  1. Check the input file format for correctness as outlined in the respective sections.
  2. Ensure all required dependencies are installed and accessible.
  3. For Java implementations, verify your `JAVA_HOME` environment variable is set correctly.
  4. Run `mvn clean` to reset the build environment for Maven projects.

- For further assistance, refer to the official documentation for each tool or technology.

---

### Contact
If you encounter further issues or have suggestions for improvement, feel free to reach out via the repository's issue tracker or contribute a pull request.