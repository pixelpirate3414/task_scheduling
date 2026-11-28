# API Schema

## Kafka Topics

| Topic | Purpose |
|---|---|
| `request.v1` | Summarization request messages (`app.kafka.topics.rpc.summarization-request`). |
| `response.v1` | Summarization response messages (`app.kafka.topics.rpc.summarization-response`). |

Topic names are configured in `src/main/resources/application.yaml`.
