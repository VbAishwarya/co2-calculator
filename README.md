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

3. Build the Project
   ```bash   
   mvn clean package

✅ If successful, the JAR file will be in the target/ directory.

## Running the CLI Tool

- Run the tool using:
   ```bash  
   java -jar target/co2-calculator-1.0-SNAPSHOT.jar --start Hamburg 
   --end Berlin --transportation-method diesel-car-medium

- Example Output
   ```bash 
   Distance between Hamburg and Berlin: 289.0 km
   Your trip caused 49.2 kg of CO2-equivalent.

- Alternative CLI Syntax
   ```bash 
  Supports both --key value and --key=value formats: 
  java -jar target/co2-calculator-1.0-SNAPSHOT.jar --start=Hamburg 
  --end Berlin --transportation-method=diesel-car-medium

## Running with Docker (Optional)

Instead of installing Java and Maven, you can run the tool inside a Docker container.

