#!/bin/bash

# Fail the script if any command fails
set -e

# Set your project details
PROJECT_ID=agile-planet-453217-n6
REGION="us-central1"
IMAGE_NAME="product-listing-app"
IMAGE_TAG="latest"  # You can change this to any tag you prefer, like commit hash or version

# Set the path to your application source
APP_PATH="."

# Change directory to your Spring Boot project directory
cd $APP_PATH

# Build app with Maven
echo "Building the Spring Boot app with Maven..."
mvn clean package -DskipTests

# Build docker image
echo "Building Docker image..."
docker build -t gcr.io/$PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG .

# Authenticate with Google Cloud so we can push the image to Google Container Registry
echo "Authenticating with Google Cloud..."
gcloud auth configure-docker

# Push the image to Google Container Registry
echo "Pushing Docker image to Google Container Registry..."
docker push gcr.io/$PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG

echo "Build and Push Complete!"
