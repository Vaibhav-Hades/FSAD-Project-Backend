# PostgreSQL Migration - Code Reference Guide

## 📋 Updated pom.xml Dependencies Section

### ✅ AFTER Migration (Current - PostgreSQL)

```xml
<dependencies>

    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Spring Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT API -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>

    <!-- JWT Implementation -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT Jackson -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Spring Security Test -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

### ❌ BEFORE Migration (Old - MySQL)

```xml
<dependencies>
    <!-- ... other dependencies ... -->
    
    <!-- MySQL Driver (REMOVED) -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- ... rest of dependencies ... -->
</dependencies>
```

---

## 🔧 Updated application.properties Configuration

### File Location
`src/main/resources/application.properties`

### ✅ AFTER Migration (PostgreSQL with Environment Variables)

```properties
# ============================================
# Server Port
# ============================================
server.port=8080

# ============================================
# PostgreSQL (Neon DB) Configuration
# ============================================
# Use environment variables for database credentials
# For local development, set these in application-dev.properties
# For production (Render), set these in deployment environment variables

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/agrivalueconnect}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:password}
spring.datasource.driver-class-name=org.postgresql.Driver

# ============================================
# JPA / Hibernate Configuration
# ============================================

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# ============================================
# JWT Configuration
# ============================================

app.jwt.secret=AgriValueConnectSecretKey2024VeryLongSecretKeyForJWTTokenGeneration
app.jwt.expiration=86400000

# ============================================
# Application Name
# ============================================

spring.application.name=agrivalueconnect
```

### ❌ BEFORE Migration (MySQL with Hardcoded Credentials)

```properties
# OLD: MySQL Configuration (NOT USED ANYMORE)
spring.datasource.url=jdbc:mysql://localhost:3306/agrivalue_connect
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# OLD: MySQL Dialect (CHANGED)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

---

## 📁 Development Profile (application-dev.properties)

### File Location
`src/main/resources/application-dev.properties`

```properties
# ============================================
# AgriValue Connect - Development Profile
# ============================================
# Use this profile for local PostgreSQL development

# Server Port
server.port=8080

# ============================================
# PostgreSQL Configuration (Local)
# ============================================

spring.datasource.url=jdbc:postgresql://localhost:5432/agrivalueconnect
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# ============================================
# JPA / Hibernate Configuration
# ============================================

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# ============================================
# JWT Configuration
# ============================================

app.jwt.secret=AgriValueConnectSecretKey2024VeryLongSecretKeyForJWTTokenGeneration
app.jwt.expiration=86400000

# ============================================
# Application Name
# ============================================

spring.application.name=agrivalueconnect
```

---

## 🚀 Production Profile (application-prod.properties)

### File Location
`src/main/resources/application-prod.properties`

```properties
# ============================================
# AgriValue Connect - Production Profile (Render)
# ============================================
# Use environment variables for all sensitive data

# Server Port
server.port=${PORT:8080}

# ============================================
# PostgreSQL Configuration (Neon)
# ============================================
# Environment variables should be set in Render dashboard:
# - DB_URL: PostgreSQL connection URL
# - DB_USERNAME: Database username
# - DB_PASSWORD: Database password

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Connection Pool Settings for Production
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# ============================================
# JPA / Hibernate Configuration
# ============================================

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# ============================================
# JWT Configuration
# ============================================

app.jwt.secret=${JWT_SECRET:AgriValueConnectSecretKey2024VeryLongSecretKeyForJWTTokenGeneration}
app.jwt.expiration=86400000

# ============================================
# Application Name
# ============================================

spring.application.name=agrivalueconnect

# ============================================
# Logging
# ============================================

logging.level.root=INFO
logging.level.com.agrivalueconnect=DEBUG
```

---

## 🐳 Docker Compose Configuration

### File Location
`docker-compose.yml` (root level)

```yaml
version: '3.9'

services:
  # PostgreSQL Database (Neon Compatible)
  postgres:
    image: postgres:15-alpine
    container_name: agrivalue-postgres
    environment:
      POSTGRES_DB: agrivalueconnect
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - agrivalue-network

  # Backend API
  backend:
    build:
      context: ./backend/agrivalueconnect
      dockerfile: Dockerfile
    container_name: agrivalue-backend
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/agrivalueconnect
      DB_USERNAME: postgres
      DB_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_JPA_SHOW_SQL: "false"
      SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.hibernate.dialect.PostgreSQLDialect
      SERVER_PORT: 8080
      SERVER_SERVLET_CONTEXT_PATH: /api
      SPRING_PROFILES_ACTIVE: dev
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - agrivalue-network

  # Frontend (unchanged)
  frontend:
    build:
      context: ./agrivalue-connect
      dockerfile: Dockerfile
    container_name: agrivalue-frontend
    ports:
      - "5173:5173"
    environment:
      VITE_API_URL: http://backend:8080/api
    depends_on:
      - backend
    networks:
      - agrivalue-network

volumes:
  postgres_data:
    driver: local

networks:
  agrivalue-network:
    driver: bridge
```

---

## 📋 Environment Variables Template

### File Location
`.env.example`

```env
# ============================================
# PostgreSQL Configuration (Neon)
# ============================================
# Format: postgresql://user:password@host/database?sslmode=require
DB_URL=postgresql://neondb_owner:your_neon_password@ep-your-endpoint.us-east-1.aws.neon.tech/neondb?sslmode=require
DB_USERNAME=neondb_owner
DB_PASSWORD=your_neon_password

# ============================================
# JWT Configuration
# ============================================
# Use a strong, random secret key
JWT_SECRET=AgriValueConnectSecretKey2024VeryLongSecretKeyForJWTTokenGeneration
JWT_EXPIRATION=86400000

# ============================================
# Server Configuration
# ============================================
PORT=8080
SPRING_PROFILES_ACTIVE=prod

# ============================================
# Database Connection Pool
# ============================================
# Optimal for Neon free tier (max 20 connections)
DB_POOL_SIZE=10
DB_MIN_IDLE=2
DB_MAX_LIFETIME=1800000
DB_IDLE_TIMEOUT=600000

# ============================================
# Application Configuration
# ============================================
APP_NAME=agrivalueconnect
JAVA_VERSION=17
```

---

## 🔍 Quick Verification Commands

### Check for MySQL References
```bash
# Should return nothing
grep -r "mysql-connector" backend/agrivalueconnect/pom.xml
grep -r "com.mysql" backend/agrivalueconnect/src/
grep -r "MySQL8Dialect" backend/agrivalueconnect/src/
```

### Verify PostgreSQL Configuration
```bash
# Should show PostgreSQL driver
grep "postgresql" backend/agrivalueconnect/pom.xml
grep "PostgreSQLDialect" backend/agrivalueconnect/src/main/resources/application.properties
```

### Build Project
```bash
# Navigate to backend
cd backend/agrivalueconnect

# Build (no need for Maven if using IDE)
mvn clean compile -DskipTests
```

---

## ✅ Migration Checklist

- [x] Removed MySQL driver from pom.xml
- [x] Added PostgreSQL driver to pom.xml
- [x] Updated application.properties with environment variables
- [x] Changed dialect to PostgreSQLDialect
- [x] Created development profile (application-dev.properties)
- [x] Created production profile (application-prod.properties)
- [x] Created .env.example template
- [x] Updated docker-compose.yml to use PostgreSQL
- [x] Created verification scripts (bash & batch)
- [x] Created comprehensive documentation

---

## 🚀 Build & Run Commands

### Development
```bash
cd backend/agrivalueconnect
mvn clean install -DskipTests
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Production
```bash
cd backend/agrivalueconnect
mvn clean package -DskipTests -Dspring.profiles.active=prod
java -jar target/agrivalueconnect-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker
```bash
# From root directory
docker-compose up --build

# Verify services
docker-compose ps
docker-compose logs -f backend
```

---

**Last Updated:** May 1, 2026
**Status:** ✅ Migration Complete
**Ready for:** Development & Production Deployment
