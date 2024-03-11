######This application is a task management system that allows users to create, view, edit and delete tasks in different project groups. It is implemented in Java technology using Spring Boot.######

**Features:

Allows the creation, viewing, editing and deletion of tasks and groups of tasks that can be assigned to specific projects.
Provides the ability to mark tasks as completed or not completed.
Provides a REST API to communicate with the application.
Provides a user interface in HTML/Thymeleaf form.
Key files and components:

**Controllers:

TaskController: Manages task requests, handles CRUD operations on tasks.
ReportController: Generates reports based on tasks and related events.

**Services:

ProjectService: Business logic for managing projects and task groups.
TaskGroupService: Service for manipulating task groups.

**Models and Repositories:

Task, TaskGroup, Project: Models representing tasks, task groups and projects.
TaskRepository, TaskGroupRepository, ProjectRepository: Repositories for interacting with the database.

**Tests:

TaskControllerE2ETest: End-to-end tests to check the performance of the API.
TaskControllerIntegrationTest: Integration tests for the task controller.
TaskControllerLightIntegrationTest: Lightweight integration tests for the task controller.
ProjectServiceTest, TaskGroupServiceTest: Unit tests for business logic.

**Configuration:

TestConfiguration: Test configuration for unit and integration tests.

**User Interface (HTML/Thymeleaf):

index.html: Home page of the application, includes navigation to different sections.
tasks.html: View for managing tasks, allows adding, viewing and marking tasks as completed.
groups.html: View for managing task groups, allows you to create, view and delete groups and tasks in groups.
projects.html: View for managing projects, allows adding projects and defining project steps.

**Technologies:

Java 11
Spring Boot
Spring Data JPA
H2 Database
Thymeleaf (for view rendering)
Maven (dependency and build management)
