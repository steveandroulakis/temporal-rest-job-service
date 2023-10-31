# Temporal REST Job Service

The primary purpose of this PoC is to demonstrate the ability of  [Temporal](https://temporal.io) to integrate with legacy orchestration systems.

### TODO
- [X] Legacy Job Service
- [ ] Temporal Workflow

## Legacy Workflow Orchestrator

#### Run the legacy Job Service Orchestrator Server
```bash
./gradlew -q execute -PmainClass=com.example.job.service.web.WebServer
````

Navigate to `http://localhost:7070/`
Submitting the form will return an ID you can use: `http://localhost:7070/job/<ID>`

#### Run a Legacy Job directly (without a Temporal Workflow or jobs WebServer)
```bash
./gradlew -q execute -PmainClass=com.example.job.service.Main
````

## Temporal

### Configuration

The sample is configured by default to connect to a [local Temporal Server](https://docs.temporal.io/cli#starting-the-temporal-server) running on localhost:7233.

To instead connect to Temporal Cloud, set the following environment variables, replacing them with your own Temporal Cloud credentials:

```bash
TEMPORAL_ADDRESS=testnamespace.sdvdw.tmprl.cloud:7233
TEMPORAL_NAMESPACE=testnamespace.sdvdw
TEMPORAL_CERT_PATH="/path/to/file.pem"
TEMPORAL_KEY_PATH="/path/to/file.key"
````

(optional) set a task queue name
```bash
export TEMPORAL_REST_JOB_SERVICE_TASKQUEUE="RestJobService"
```

### Run a Workflow

Note: Use a Java 18 SDK.

Start a worker: TODO
