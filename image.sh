echo Building Dockerfile described image
docker build -t message .
echo Launching docker compose services for upstanding message:latest image
docker-compose up