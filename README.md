# Distributed Job Scheduling System

## Overview
The Distributed Job Scheduling System is a robust and scalable backend solution for managing and executing various types of tasks, also known as jobs. It supports one-time jobs, recurring jobs, and immediate execution, providing comprehensive job management functionality. The system is designed to handle backend functionalities while offering APIs for interaction with a potential frontend.

### Key Features
1. **Job Submission**:
   - Users can submit jobs to be executed immediately or at a specific future time.
   - Jobs can vary in type and complexity.

2. **Recurring Jobs**:
   - Supports jobs that recur at specified intervals, including hourly, daily, weekly, or monthly tasks.

3. **Job Management**:
   - RESTful APIs allow users to manage their jobs, view statuses, cancel jobs, and reschedule jobs.

4. **Failure Handling**:
   - Automatic retry mechanism for job failures.
   - Notifications for consistently failing jobs.

5. **Logging and Monitoring**:
   - Detailed logs of all job executions.
   - Monitoring system tracks job statuses and overall system health.

### Technical Highlights
- **Backend Framework**: Java Spring Boot
- **Database**: PostgreSQL
  - **Tables**:
    - `job`: Stores job details such as type, schedule, and status.
    - `job_execution`: Tracks execution details for each job.
    - `job_logs`: Maintains logs for job executions.
- **Task Scheduling**: Quartz Scheduler
- **Authentication**: Environment-based credentials for secure email notifications.
- **APIs**: RESTful APIs for job submission, management, and monitoring.
- **Postman Collection**: A Postman collection is provided in the `/chronos` directory for testing APIs.

## Database Structure
1. **Job Table**:
   - Stores job metadata such as type, schedule, and status.
   - Fields include `id`, `name`, `type`, `schedule`, `status`, etc.

2. **Job Execution Table**:
   - Tracks execution details for each job.
   - Fields include `id`, `job_id`, `start_time`, `end_time`, `status`, etc.

3. **Job Logs Table**:
   - Maintains logs for job executions.
   - Fields include `id`, `job_execution_id`, `log_message`, `timestamp`, etc.

## Running the System
### Prerequisites
- **Java**: Ensure Java 11 or higher is installed.
- **Database**: PostgreSQL setup with the required schema.
- **Environment Variables**:
  - Set `EMAIL_USERNAME` and `EMAIL_PASSWORD` for email notifications.

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd chronos
   ```

4. Configure :
   - Set `EMAIL_USERNAME` and `EMAIL_PASSWORD` in application properties.

5. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

6. Test APIs using the provided Postman collection:
   - Import the collection from `/chronos` into Postman.

### Suggestions for Users
- Ensure the database is properly configured and accessible.
- Use the Postman collection for testing and exploring API functionalities.
- Monitor logs and job statuses using the provided APIs.

## Monitoring and Logging
- Logs are stored in the `job_logs` table for each job execution.
- Use monitoring APIs to track system health and job statuses.

## Scalability
The system is designed to handle an increasing number of tasks efficiently. It uses Quartz Scheduler for task scheduling and PostgreSQL for reliable data management.

## Documentation
For detailed API documentation, refer to the Postman collection and the `/HELP.md` file in the repository.

---

Feel free to reach out for any issues or suggestions regarding the system.
