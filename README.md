# CO₂ Emission Calculator

A **Java-based CLI tool** that calculates the **CO₂ emissions** of a journey between two cities.  
It fetches the **distance from OpenRouteService (ORS) API** and computes emissions using predefined **CO₂ factors** for different transportation methods.

This tool supports **Maven for dependency management** and can be **Dockerized for easy execution**.

---

##  Features
- ✅ Fetches distances between cities using **OpenRouteService API**.
- ✅ Supports **multiple transport methods** (diesel car, electric car, bus, train).
- ✅ Computes **CO₂ emissions in kilograms**.
- ✅ Allows flexible CLI arguments (`--key value` and `--key=value` formats).
- ✅ Uses **Maven for dependency management**.
- ✅ Includes **unit tests** for all components.
- ✅ Supports **Windows Batch & Shell scripts** for easier execution.
- ✅ Can be **Dockerized for easier execution**.

---

##  Prerequisites

Before running the tool, ensure you have:

- **Java 21+** installed
  ```bash
  java -version
  
- **Apache Maven 3.9+** installed
  ```bash 
  mvn -version

- **Docker (Optional, for containerized execution)** 
  ```bash 
  docker --version

- An OpenRouteService API key (Sign up https://openrouteservice.org/)


## Installation & Setup

1. **Clone the Repository** installed
   ```bash
   git clone https://github.com/VbAishwarya/co2-calculator.git
   cd co2-calculator

2. Set Up Environment Variables (For OpenRouteService API Key)
   
   ```bash
   export ORS_TOKEN="your_api_key_here"   # Linux/macOS
   set ORS_TOKEN="your_api_key_here"      # Windows (CMD)
   $env:ORS_TOKEN="your_api_key_here"     # Windows (PowerShell)

3. Build the Project with Maven
   ```bash   
   mvn clean package

- If successful, the JAR file will be in the target/ directory.
   ```bash
   target/co2-calculator-1.0-SNAPSHOT.jar

## Running the CLI Tool

- Option 1: Run Directly Using Java
   ```bash 
  java -jar target/co2-calculator-1.0-SNAPSHOT.jar --start "Los Angeles" --end "New York" --transportation-method diesel-car-medium

- Option 2: Run Using Windows Batch File (co2-calculator.bat)
   ```bash 
  ./co2-calculator --end "New York" --start "Los Angeles" --transportation-method=electric-car-large

- Option 3: Run Using Linux/macOS Shell Script (co2-calculator.sh)
   ```bash 
    ./co2-calculator.sh --end "New York" --start "Los Angeles" --transportation-method petrol-car-medium
  
## Running with Docker (Optional)
    
Instead of installing Java and Maven, you can run the tool inside a Docker container.
 
- Build the Docker Image
   ```bash 
  docker build -t co2-calculator .

- Run the Tool in Docker
   ```bash 
    docker run --rm -e ORS_TOKEN="your_api_key_here" co2-calculator --start "Los Angeles" --end "New York" --transportation-method diesel-car-medium

## Running Tests

- To verify correctness, run:
   ```bash 
    mvn test

- For test coverage, run:
   ```bash 
    mvn jacoco:report

The coverage report will be generated at:
   ```bash 
    target/site/jacoco/index.html
