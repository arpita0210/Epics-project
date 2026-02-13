# AI-Powered Real-Time Language Translation Platform

A comprehensive microservices-based translation platform built with Spring Boot, featuring real-time text and speech translation powered by AI models.

## 🚀 Features

- **Text Translation**: Real-time translation between multiple languages using transformer-based ML models
- **Speech-to-Text**: Convert speech to text using Whisper API integration
- **Text-to-Speech**: Generate natural-sounding speech from text using TTS models
- **User Authentication**: Secure JWT-based authentication and authorization
- **Caching**: Redis-based caching for improved performance
- **Microservices Architecture**: Scalable, independently deployable services
- **API Gateway**: Centralized routing and load balancing
- **Monitoring**: Prometheus and Grafana for metrics and observability
- **Cloud-Ready**: Docker and Kubernetes deployment configurations

## 📋 Architecture

```
┌─────────────────┐
│   API Gateway   │ :8080
└────────┬────────┘
         │
    ┌────┴────┬─────────┬──────────┐
    │         │         │          │
┌───▼────┐ ┌─▼──────┐ ┌▼────────┐ ┌▼──────────┐
│Translation│ │  User  │ │ Speech  │ │  Speech   │
│ Service   │ │Service │ │Recognition│ │Synthesis │
│   :8081   │ │ :8082  │ │  :8083  │ │  :8084   │
└───┬────┘ └─┬──────┘ └─────────┘ └───────────┘
    │        │
┌───▼────────▼───┐     ┌─────────┐
│   PostgreSQL   │     │  Redis  │
│     :5432      │     │  :6379  │
└────────────────┘     └─────────┘
```

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2.2, Java 17
- **Build Tool**: Maven
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Security**: Spring Security, JWT
- **API Documentation**: OpenAPI/Swagger
- **Monitoring**: Prometheus, Grafana
- **Containerization**: Docker, Docker Compose
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions

## 📦 Project Structure

```
ai-translation-platform/
├── common-lib/                 # Shared utilities and DTOs
├── translation-service/        # Core translation service
├── user-service/              # Authentication and user management
├── speech-recognition-service/ # Speech-to-text service
├── speech-synthesis-service/  # Text-to-speech service
├── api-gateway/               # API Gateway
├── docker/                    # Docker configurations
├── kubernetes/                # Kubernetes manifests
└── .github/workflows/         # CI/CD pipelines
```

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 15 (or use Docker)
- Redis 7 (or use Docker)

### Running with Docker Compose

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ai-translation-platform
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Start all services**
   ```bash
   docker-compose up -d
   ```

4. **Access the services**
   - API Gateway: http://localhost:8080
   - Translation Service Swagger: http://localhost:8081/swagger-ui.html
   - User Service Swagger: http://localhost:8082/swagger-ui.html
   - Prometheus: http://localhost:9090
   - Grafana: http://localhost:3000 (admin/admin)

### Running Locally (Development)

1. **Start PostgreSQL and Redis**
   ```bash
   docker-compose up postgres redis -d
   ```

2. **Run each service**
   ```bash
   # Terminal 1 - Translation Service
   cd translation-service
   mvn spring-boot:run

   # Terminal 2 - User Service
   cd user-service
   mvn spring-boot:run

   # Terminal 3 - Speech Recognition Service
   cd speech-recognition-service
   mvn spring-boot:run

   # Terminal 4 - Speech Synthesis Service
   cd speech-synthesis-service
   mvn spring-boot:run

   # Terminal 5 - API Gateway
   cd api-gateway
   mvn spring-boot:run
   ```

## 📡 API Endpoints

### Translation Service (via API Gateway :8080)

- `POST /api/translate` - Translate text
- `GET /api/languages` - List supported languages
- `GET /api/translation/history` - Get translation history

### User Service

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token
- `GET /api/auth/profile` - Get user profile

### Speech Recognition Service

- `POST /api/speech-to-text` - Convert speech to text

### Speech Synthesis Service

- `POST /api/text-to-speech` - Convert text to speech
- `GET /api/voices` - List available voices

## 🔐 Authentication

The platform uses JWT-based authentication. To access protected endpoints:

1. **Register a user**
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "username": "testuser",
       "email": "test@example.com",
       "password": "password123"
     }'
   ```

2. **Login to get JWT token**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "usernameOrEmail": "testuser",
       "password": "password123"
     }'
   ```

3. **Use the token in requests**
   ```bash
   curl -X POST http://localhost:8080/api/translate \
     -H "Authorization: Bearer <your-jwt-token>" \
     -H "Content-Type: application/json" \
     -d '{
       "text": "Hello, world!",
       "sourceLanguage": "en",
       "targetLanguage": "es"
     }'
   ```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Run Specific Service Tests
```bash
cd translation-service
mvn test
```

## 📊 Monitoring

### Prometheus Metrics
Access Prometheus at http://localhost:9090 to view metrics from all services.

### Grafana Dashboards
1. Access Grafana at http://localhost:3000
2. Login with admin/admin
3. Add Prometheus as a data source (http://prometheus:9090)
4. Import dashboards for Spring Boot applications

## 🚢 Deployment

### Kubernetes Deployment

1. **Apply Kubernetes manifests**
   ```bash
   kubectl apply -f kubernetes/postgres.yml
   kubectl apply -f kubernetes/translation-service.yml
   kubectl apply -f kubernetes/ingress.yml
   ```

2. **Check deployment status**
   ```bash
   kubectl get pods
   kubectl get services
   ```

### Environment Variables

Key environment variables for configuration:

- `SPRING_DATASOURCE_URL` - PostgreSQL connection URL
- `SPRING_DATA_REDIS_HOST` - Redis host
- `ML_TRANSLATION_ENDPOINT` - ML translation service endpoint
- `ML_WHISPER_ENDPOINT` - Whisper API endpoint
- `ML_TTS_ENDPOINT` - TTS service endpoint
- `JWT_SECRET` - JWT signing secret

## 🔧 Configuration

### ML Model Integration

The platform integrates with external ML services. Configure endpoints in `application.yml`:

```yaml
ml:
  translation:
    endpoint: http://your-ml-service:8000/translate
  whisper:
    endpoint: http://your-whisper-service:8000/speech-to-text
  tts:
    endpoint: http://your-tts-service:8000/text-to-speech
```

**Note**: Mock implementations are provided for demo purposes when ML services are unavailable.

## 📝 License

This project is licensed under the Apache License 2.0.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

For support and questions, please open an issue in the repository.

## 🎯 Roadmap

- [ ] WebSocket support for real-time conversations
- [ ] Language detection service
- [ ] Offline mode with pre-downloaded language packs
- [ ] Mobile SDK
- [ ] Voice cloning capabilities
- [ ] Multi-modal translation (image + text)
