# Getting Started

## Prerequisites
- JDK 17 or later
- Maven 3.8+
- Docker (optional, for containerized deployment)

## Building the Project

```bash
mvn clean install
```

## Running the Services

### Option 1: Using Docker Compose (Recommended)
```bash
docker-compose up
```

### Option 2: Running Individually
Start each service in a separate terminal:

```bash
# Start databases first
docker-compose up postgres redis -d

# Translation Service
cd translation-service && mvn spring-boot:run

# User Service
cd user-service && mvn spring-boot:run

# Speech Recognition Service
cd speech-recognition-service && mvn spring-boot:run

# Speech Synthesis Service
cd speech-synthesis-service && mvn spring-boot:run

# API Gateway
cd api-gateway && mvn spring-boot:run
```

## Testing the API

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "email": "demo@example.com",
    "password": "demo123"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "demo",
    "password": "demo123"
  }'
```

### 3. Translate Text
```bash
curl -X POST http://localhost:8080/api/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello, how are you?",
    "sourceLanguage": "en",
    "targetLanguage": "es"
  }'
```

## Accessing Swagger UI

- Translation Service: http://localhost:8081/swagger-ui.html
- User Service: http://localhost:8082/swagger-ui.html
- Speech Recognition: http://localhost:8083/swagger-ui.html
- Speech Synthesis: http://localhost:8084/swagger-ui.html

## Monitoring

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)
