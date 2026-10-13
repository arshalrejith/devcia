# Student Course Registration — DevOps Lab Assessment

## 1. Project Overview

This project demonstrates Git version control, Maven multi-module build management, unit testing, Jenkins Declarative Pipeline configuration, and Kubernetes deployment using Minikube.

The application manages course registration and validates course capacity and student credit limits.

## 2. Prerequisites

- Git
- Java JDK 17
- Apache Maven
- Jenkins with a suitable Windows agent, Java, and Maven
- Docker Desktop
- Minikube
- kubectl

## 3. Git Workflow

The application changes were developed using a feature branch and merged into `main`.

The work included:
- Fixing registration when a course reaches its capacity.
- Adding tests for capacity boundary conditions.
- Updating the maximum registered credit limit from 20 to 24 and testing the boundary.

To inspect the commit history and working-tree status:

```powershell
git --no-pager log --oneline --graph --decorate -10
git status
```

The final working tree should be clean after all intended changes are committed.

## 4. Maven Multi-Module Project

The project uses a parent POM and a child application module.

```text
Student-Starter/
├── pom.xml
├── Jenkinsfile
├── deployment.yaml
├── README.md
└── course-registration-app/
    ├── pom.xml
    ├── src/
    │   ├── main/java/com/university/
    │   └── test/java/com/university/
    └── target/
```

The parent POM defines the project coordinates, module, Java version, JUnit version management, and plugin versions. The child module contains the application source code and tests and produces a JAR.

Build and test from the project root:

```powershell
mvn clean test
mvn package
```

**Observed result:** Both commands completed successfully. The test run reported 12 tests, 0 failures, 0 errors, and 0 skipped tests.

The generated JAR is located at:

```text
course-registration-app/target/course-registration-app-1.0-SNAPSHOT.jar
```

## 5. Registration Validation Changes

### Course Capacity

Registration is rejected when current enrollment is greater than or equal to the course capacity. A course with exactly one seat remaining can accept another registration.

Tests cover rejection at full capacity, acceptance when one seat remains, and preservation of state when registration is rejected.

### Maximum Registered Credits

The maximum registered credit limit was changed from 20 to 24.

Tests cover registration reaching exactly 24 credits and rejection when the total would exceed 24 credits, without changing the state on rejection.

## 6. Jenkins Declarative Pipeline

The root `Jenkinsfile` defines the following stages:

1. Checkout — checks out the source configured for the Jenkins job.
2. Build and Test — executes `mvn clean test`.
3. Package — executes `mvn package`.

The pipeline uses Windows `bat` steps and is configured to publish JUnit test reports and archive the generated JAR.

Expected report path:

```text
course-registration-app/target/surefire-reports/TEST-*.xml
```

Expected artifact path:

```text
course-registration-app/target/*.jar
```

The Maven commands were verified locally. A successful local Maven build does not, by itself, establish that the pipeline was executed successfully in Jenkins. Record Jenkins execution results here if a Jenkins run is available.

## 7. Kubernetes Deployment with Minikube

The `deployment.yaml` file defines an Nginx Deployment using image `nginx:1.27` and a NodePort Service.

The initial Deployment uses two replicas. The Service exposes port 80 through NodePort 30080.

### Deploy the application

```powershell
minikube start
kubectl apply -f .\deployment.yaml
kubectl rollout status deployment/nginx-deployment
kubectl get deployments,pods,services
```

### Scale to three replicas

```powershell
kubectl scale deployment nginx-deployment --replicas=3
kubectl rollout status deployment/nginx-deployment
kubectl get deployments,pods
```

### Demonstrate self-healing

Delete one of the actual Nginx Pod names shown by `kubectl get pods`:

```powershell
kubectl delete pod <actual-nginx-pod-name>
kubectl get pods -w
kubectl get deployments,pods
```

Kubernetes creates a replacement Pod to maintain the Deployment's desired replica count.

**Observed result:** The Deployment reached 3/3 available replicas. After a Pod was deleted, a replacement Pod appeared and the Deployment continued to maintain three Running Pods.

### Access the Nginx Service

```powershell
minikube service nginx-service --url
```

Keep the terminal session open when required by the Windows Docker driver and access the returned URL in a browser.

## 8. Submission Evidence

Include screenshots of the following:

1. Git feature branch, commit history, merge, and clean working tree.
2. Maven parent and child module structure and successful `mvn clean test`.
3. Successful `mvn package` and generated JAR.
4. Registration capacity boundary tests.
5. Credit-limit boundary tests for 24 credits.
6. Jenkinsfile and Jenkins execution results, if available.
7. Kubernetes Deployment with two initial replicas.
8. Kubernetes Deployment scaled to three replicas.
9. Pod deletion and replacement demonstrating self-healing.
10. Nginx welcome page, if accessible.

Only include screenshots that were actually captured. If a step could not be executed, clearly document that limitation.

## 9. Assessment Evidence

The screenshots and practical execution evidence for Tasks 1–6 are documented in the following file:

[View DevOps Lab Assessment Evidence](./devopslab.odt)

The evidence document contains screenshots covering:
- Task 1: Git workflow and merge history
- Task 2: Maven multi-module build and tests
- Task 3: Course capacity validation
- Task 4: Maximum credit limit of 24
- Task 5: Jenkins Declarative Pipeline
- Task 6: Minikube deployment, scaling, and self-healing

## 10. Conclusion

The project demonstrates Git-based change management, Maven multi-module builds, automated unit tests, a Jenkins Declarative Pipeline configuration, and Kubernetes Deployment, scaling, and self-healing using Minikube. The application changes were verified with the Maven test and package commands.