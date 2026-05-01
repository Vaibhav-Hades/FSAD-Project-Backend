#!/bin/bash
# PostgreSQL Migration Verification Script

echo "╔════════════════════════════════════════════════════╗"
echo "║  PostgreSQL Migration Verification Script         ║"
echo "║  AgriValue Connect Backend                        ║"
echo "╚════════════════════════════════════════════════════╝"
echo ""

ERRORS=0
WARNINGS=0

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "[1/10] Checking for MySQL references..."
if grep -r "mysql-connector" . --exclude-dir=.git --exclude-dir=target --exclude-dir=.m2 2>/dev/null | grep -v ".md" | grep -v ".example"; then
    echo -e "${RED}✗ FAIL${NC}: MySQL connector dependency found"
    ((ERRORS++))
else
    echo -e "${GREEN}✓ PASS${NC}: No MySQL connector dependency"
fi
echo ""

echo "[2/10] Checking for PostgreSQL driver..."
if grep -q "org.postgresql" pom.xml; then
    echo -e "${GREEN}✓ PASS${NC}: PostgreSQL driver configured"
else
    echo -e "${RED}✗ FAIL${NC}: PostgreSQL driver not found in pom.xml"
    ((ERRORS++))
fi
echo ""

echo "[3/10] Checking application.properties for environment variables..."
if grep -q 'spring.datasource.url=${DB_URL' src/main/resources/application.properties; then
    echo -e "${GREEN}✓ PASS${NC}: Environment variable configuration found"
else
    echo -e "${RED}✗ FAIL${NC}: Environment variable configuration missing"
    ((ERRORS++))
fi
echo ""

echo "[4/10] Checking PostgreSQL dialect configuration..."
if grep -q "org.hibernate.dialect.PostgreSQLDialect" src/main/resources/application.properties; then
    echo -e "${GREEN}✓ PASS${NC}: PostgreSQL dialect configured"
else
    echo -e "${RED}✗ FAIL${NC}: PostgreSQL dialect not configured"
    ((ERRORS++))
fi
echo ""

echo "[5/10] Checking for MySQL8Dialect references..."
if grep -r "MySQL8Dialect" . --exclude-dir=.git --exclude-dir=target 2>/dev/null; then
    echo -e "${RED}✗ FAIL${NC}: MySQL8Dialect reference found"
    ((ERRORS++))
else
    echo -e "${GREEN}✓ PASS${NC}: No MySQL8Dialect references"
fi
echo ""

echo "[6/10] Checking development profile..."
if [ -f "src/main/resources/application-dev.properties" ]; then
    echo -e "${GREEN}✓ PASS${NC}: Development profile exists"
else
    echo -e "${YELLOW}⚠ WARN${NC}: Development profile not found"
    ((WARNINGS++))
fi
echo ""

echo "[7/10] Checking production profile..."
if [ -f "src/main/resources/application-prod.properties" ]; then
    echo -e "${GREEN}✓ PASS${NC}: Production profile exists"
else
    echo -e "${YELLOW}⚠ WARN${NC}: Production profile not found"
    ((WARNINGS++))
fi
echo ""

echo "[8/10] Checking .env.example..."
if [ -f ".env.example" ]; then
    echo -e "${GREEN}✓ PASS${NC}: Environment variable template exists"
else
    echo -e "${YELLOW}⚠ WARN${NC}: .env.example not found"
    ((WARNINGS++))
fi
echo ""

echo "[9/10] Checking for docker-compose.yml..."
if grep -q "postgres:" docker-compose.yml 2>/dev/null || grep -q "postgres:" ../docker-compose.yml 2>/dev/null; then
    echo -e "${GREEN}✓ PASS${NC}: Docker Compose PostgreSQL configured"
else
    echo -e "${YELLOW}⚠ WARN${NC}: Docker Compose PostgreSQL not configured"
    ((WARNINGS++))
fi
echo ""

echo "[10/10] Checking PostgreSQL driver class name..."
if grep -q "org.postgresql.Driver" src/main/resources/application.properties; then
    echo -e "${GREEN}✓ PASS${NC}: PostgreSQL driver class name correct"
else
    echo -e "${RED}✗ FAIL${NC}: PostgreSQL driver class name incorrect"
    ((ERRORS++))
fi
echo ""

echo "╔════════════════════════════════════════════════════╗"
echo "║  Verification Results                             ║"
echo "╚════════════════════════════════════════════════════╝"
echo -e "${GREEN}✓ Passed: $((10 - ERRORS - WARNINGS))${NC}"
echo -e "${YELLOW}⚠ Warnings: $WARNINGS${NC}"
echo -e "${RED}✗ Failed: $ERRORS${NC}"
echo ""

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✓ Migration verification PASSED!${NC}"
    echo ""
    echo "Next steps:"
    echo "1. mvn clean install -DskipTests"
    echo "2. Configure environment variables"
    echo "3. Run with: mvn spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=dev'"
    echo ""
    exit 0
else
    echo -e "${RED}✗ Migration verification FAILED!${NC}"
    echo ""
    echo "Issues to fix:"
    echo "1. Remove MySQL dependencies from pom.xml"
    echo "2. Ensure PostgreSQL driver is in pom.xml"
    echo "3. Update application.properties with environment variables"
    echo "4. Replace MySQL8Dialect with PostgreSQLDialect"
    echo ""
    exit 1
fi
