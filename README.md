# Product CRUD Spring Boot Application

This is a Spring Boot application that provides basic CRUD operations for a product entity, complete with authentication, rate-limiting, and a Swagger API interface. The application is secured with basic authentication and leverages **Bucket4j** for rate limiting based on the user's IP address. The rate-limiting configuration is customizable through application properties.

## Features

- **Basic Authentication**:
    - **Register**: Users must first register by calling `/api/v1/auth/register` and providing credentials in subsequent requests.
    - **Public Endpoints**: `GET /api/v1/products` and `GET /api/v1/auth` endpoints are open to all users without authentication.
    - **Protected Endpoints**: Other endpoints, including CRUD operations for products, require authentication.

- **Rate Limiting**:
    - Configurable rate-limiting is implemented using **Bucket4j**.
    - Rate limiting is based on the user's IP address.
    - Rate limits can be customized via `application.properties`.

- **Swagger API**:
    - The application provides a Swagger UI to list all available APIs and interact with them. The Swagger documentation is automatically generated for all endpoints.

## Endpoints

- `POST /api/v1/auth/register`: Register a new user.
- `GET /api/v1/products`: Retrieve all products (No authentication required).
- `GET /api/v1/products/{id}`: Retrieve a product by ID (Authentication required).
- `POST /api/v1/products`: Create a new product (Authentication required).
- `PUT /api/v1/products/{id}`: Update an existing product (Authentication required).
- `DELETE /api/v1/products/{id}`: Delete a product (Authentication required).

## Deployment

### Cloud Deployment (Google Cloud Platform)

To deploy the app on GCP Cloud Run, follow these steps:

1. **Deploy PostgreSQL on GCP**:
    - Set up a PostgreSQL instance on Google Cloud SQL.

2. **Install Google Cloud CLI**:
    - Install the [Google Cloud CLI](https://cloud.google.com/sdk/docs/install).

3. **Setup Google Cloud CLI**:
    - Run `gcloud init` and configure your project and authentication.
    - Ensure that the `gcloud` CLI is configured to use your project and the correct credentials.

4. **Configure Application Properties**:
    - In `application-prod.properties`, set the URL and credentials for the Cloud SQL instance.

5. **Modify Build Script**:
    - Modify the `deploy.sh` script with the necessary details for your GCP project.
    - This script will
      - Build project using mvn
      - Build Docker image
      - Push Docker image to Google Container Registry (GCR) using `gcloud` CLI

6. **Deploy to Cloud Run**:
    - Deploy the image to **Cloud Run** by running the following command or using the Google Cloud Console:
      ```bash
      gcloud run deploy --image gcr.io/[your-project-id]/[image-name] --platform managed
      ```
    - In the Cloud Run environment variables, set `SPRING_PROFILES_ACTIVE=prod`.

7. **Optional**: **Deploy API Gateway**:
    - If required, deploy a new gateway using gcp_gateway_config.yaml
      - Be sure to fill in the `backendUrl` and `host` with the Cloud Run service URL.

### Local Deployment (Development)

To run the application locally in a development environment, follow these steps:

1. **Set up PostgreSQL Instance**:
    - Ensure you have a PostgreSQL instance running locally or remotely and configure its URL and credentials.

2. **Configure Application Properties**:
    - Provide the PostgreSQL instance URL and credentials in `application-dev.properties`.

3. **Build and Run the Application**:
    - Build the project:
      ```bash
      mvn clean install
      ```
    - Run the application in the `dev` profile:
      ```bash
      mvn spring-boot:run -Dspring-boot.run.profiles=dev
      ```

## Future Enhancements

- **JWT Authentication and Authorization**: Implement JWT for more secure and stateless authentication.
- **Rate Limits with Tiering**: Replace the current ConcurrentHashMap with **Redis** or **Guava** for more efficient caching, and implement tiered rate limiting based on API keys.
- **Move Auth and Rate Limiting to GCP**: Offload authentication and rate limiting to fully managed GCP services for improved scalability and reliability.
- **Logging and Monitoring**: Add centralized logging and monitoring using tools like ELK Stack or Prometheus.
- **Custom Exception Handling**: Implement a global exception handler to manage exceptions centrally, providing custom responses for different error scenarios.
- **Terraform for Deploying Infrastructure**: Use **Terraform** for provisioning and managing the application's infrastructure, enabling automated and consistent deployments across environments.
- **Automated Tests**: Improve test coverage with unit and integration tests.
- **CI/CD Pipeline**: Set up a **CI/CD pipeline** using tools like **Jenkins**, **GitLab CI**, or **GitHub Actions** to automate the process of building, testing, and deploying the application. This ensures faster and more reliable deployments with continuous integration and delivery.


