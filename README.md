# Kafka Cluster

## Java App

A service for defining a java app that would like to use the kafka cluster.

### Get Started

`docker-compose up -d app-java`

`docker exec -it kafkacluster_app-java_1 bash`

`kafka-topics --zookeeper localhost:22181,localhost:32181,localhost:42181 --create --topic YOUR_TOPIC --if-not-exists --replication-factor 3 --partitions 3`

