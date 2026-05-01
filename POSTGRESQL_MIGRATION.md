# PostgreSQL (Neon) Migration Guide

## Migration Completed ✅

Your AgriValue Connect backend has been successfully migrated from MySQL to PostgreSQL.

## Changes Made

### 1. pom.xml Updates

**Removed:**
```xml
<!-- MySQL Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Added:**
```xml
<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Application Properties Configuration

**Default Configuration** (`application.properties`):
- Uses environment variables with fallback defaults
- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- Driver: `org.postgresql.Driver`
- Dialect: `org.hibernate.dialect.PostgreSQLDialect`

### 3. Profile-Specific Configurations

#### Development Profile (`application-dev.properties`)
- Local PostgreSQL on `localhost:5432`
- Database: `agrivalueconnect`
- Default credentials: `postgres/password`
- SQL logging enabled for debugging
- To use: `-Dspring.profiles.active=dev`

#### Production Profile (`application-prod.properties`)
- Optimized for Render deployment
- Connection pooling (HikariCP)
- Environment variable-based configuration
- SQL logging disabled for performance
- To use: `-Dspring.profiles.active=prod`

## Environment Variables

### For Neon PostgreSQL (Render)

Set these in your Render environment variables:

```
DB_URL=postgresql://user:password@your-neon-host/dbname?sslmode=require
DB_USERNAME=neondb_owner
DB_PASSWORD=your_neon_password
JWT_SECRET=your_secure_jwt_secret
```

### For Local Development

Create `.env` file in the project root:

```env
DB_URL=jdbc:postgresql://localhost:5432/agrivalueconnect
DB_USERNAME=postgres
DB_PASSWORD=password
JWT_SECRET=AgriValueConnectSecretKey2024VeryLongSecretKeyForJWTTokenGeneration
```

## Setup Instructions

### Local Development Setup

#### Prerequisites
- PostgreSQL 12+ installed and running
- Java 17+
- Maven 3.9+

#### Step 1: Create Local Database

```sql
CREATE DATABASE agrivalueconnect;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE agrivalueconnect TO postgres;
```

Or using psql:
```bash
psql -U postgres -c "CREATE DATABASE agrivalueconnect;"
```

#### Step 2: Build Project

```bash
cd backend/agrivalueconnect
mvn clean install -DskipTests
```

#### Step 3: Run Application

```bash
# Using development profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or using environment variables
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Render Deployment Setup

#### Step 1: Create Neon PostgreSQL Database

1. Go to [Neon Console](https://console.neon.tech)
2. Create a new project
3. Copy the connection string (it will look like: `postgresql://user:password@ep-xxxx.us-east-1.aws.neon.tech/dbname?sslmode=require`)

#### Step 2: Configure Render Environment Variables

In your Render service environment tab, add:

```
DB_URL=postgresql://your_user:your_password@your_neon_host/your_db?sslmode=require
DB_USERNAME=your_neon_user
DB_PASSWORD=your_neon_password
JWT_SECRET=your_secure_jwt_secret
JAVA_VERSION=17
```

#### Step 3: Update Render Build Command

```bash
mvn clean install -DskipTests -Dspring.profiles.active=prod
```

#### Step 4: Update Render Start Command

```bash
java -jar target/agrivalueconnect-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Dockerfile Updates

Update your Docker configurations to use PostgreSQL:

### docker-compose.yml (Local Development)

```yaml
services:
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

  backend:
    build:
      context: ./backend/agrivalueconnect
      dockerfile: Dockerfile
    container_name: agrivalue-backend
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/agrivalueconnect
      DB_USERNAME: postgres
      DB_PASSWORD: password
      JWT_SECRET: AgriValueConnectSecretKey2024
      SPRING_PROFILES_ACTIVE: dev
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy

volumes:
  postgres_data:
    driver: local
```

## Verification Checklist

- ✅ PostgreSQL driver added to pom.xml
- ✅ MySQL driver removed from pom.xml
- ✅ application.properties updated with environment variables
- ✅ PostgreSQL dialect configured (PostgreSQLDialect)
- ✅ Development profile created (application-dev.properties)
- ✅ Production profile created (application-prod.properties)
- ✅ Connection pooling configured for production
- ✅ SSL/TLS support enabled for Neon

## Testing

### Test Local Connection

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Expected output:
```
Hibernate dialect: org.hibernate.dialect.PostgreSQLDialect
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

### Verify No MySQL References

```bash
# Check for any remaining MySQL references
grep -r "mysql" src/main/java/
grep -r "com.mysql" .
```

Should return no results (except in documentation/comments).

## Common Issues and Solutions

### Issue 1: Cannot load driver class: org.postgresql.Driver

**Solution:**
- Ensure `org.postgresql:postgresql` dependency is in pom.xml
- Run `mvn clean install` to download driver
- Check Maven Central network connectivity

### Issue 2: Connection refused

**Solution:**
- Verify PostgreSQL is running: `pg_isready -U postgres`
- Check DB_URL environment variable format
- For Neon, ensure `?sslmode=require` is in URL

### Issue 3: No suitable database driver found

**Solution:**
- Verify `spring.datasource.driver-class-name=org.postgresql.Driver`
- Check that PostgreSQL dependency is not marked as `<optional>true</optional>`
- Ensure driver scope is `runtime` not `test`

### Issue 4: SSL/TLS certificate validation error

**Solution:**
- Add `?sslmode=require` to the connection URL (already done)
- For Neon, this is required by default
- Ensure Java has updated CA certificates

## Rollback to MySQL (if needed)

If you need to rollback to MySQL:

1. Restore old pom.xml with MySQL driver
2. Update application.properties with MySQL configuration
3. Run: `mvn clean install -DskipTests`

However, be aware that:
- Table structures may differ between MySQL and PostgreSQL
- Data migration may be required
- SQL dialects differ

## Performance Tips

### For Production

1. **Connection Pooling** - Already configured with HikariCP
2. **Batch Operations** - Enabled in prod profile
3. **Query Optimization** - Monitor slow queries
4. **Indexes** - Add indexes on frequently queried columns
5. **Monitoring** - Use Render's database monitoring

### PostgreSQL Tuning

```properties
# Add to application-prod.properties for better performance
spring.datasource.hikari.maximum-pool-size=20
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

## References

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Neon Documentation](https://neon.tech/docs)
- [Spring Boot PostgreSQL Guide](https://spring.io/guides/gs/accessing-data-postgresql/)
- [Render Deployment Guide](https://render.com/docs)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP/wiki/Configuration)

## Support

For issues or questions:
1. Check the logs: `docker-compose logs -f backend`
2. Verify environment variables: `echo $DB_URL`
3. Test database connection: `psql -c "SELECT 1"`
4. Review Spring Boot documentation for datasource configuration
