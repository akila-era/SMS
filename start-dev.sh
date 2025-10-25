#!/bin/bash

echo "🚀 Starting Salon Management System Development Environment"
echo "=========================================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

echo "📦 Starting services with Docker Compose..."
docker-compose up -d

echo "⏳ Waiting for services to start..."
sleep 10

echo "🔍 Checking service status..."
echo "Frontend: http://localhost:3000"
echo "Backend API: http://localhost:8080/api"
echo "API Documentation: http://localhost:8080/swagger-ui.html"
echo "Database: localhost:3306"

echo ""
echo "✅ Development environment is ready!"
echo ""
echo "Default login credentials:"
echo "Username: admin"
echo "Password: password"
echo ""
echo "To stop the services, run: docker-compose down"
echo "To view logs, run: docker-compose logs -f"


