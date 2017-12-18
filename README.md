# Kafka Cluster

## Java App

A service for defining a java app that would like to use the kafka cluster.

### Get Started

`docker-compose up -d app-java`

To inspect and manipulate the kafka cluster use the kafka cli tools within the `app-java` service:

`docker exec -it kafkacluster_app-java_1 bash`

```
kafka-topics --zookeeper localhost:22181,localhost:32181,localhost:42181 --create --topic streams-plaintext-input --if-not-exists --replication-factor 3 --partitions 3
kafka-topics --zookeeper localhost:22181,localhost:32181,localhost:42181 --create --topic streams-pipe-output --if-not-exists --replication-factor 3 --partitions 3
```

To build and run the pipe application:

`docker exec -it kafkacluster_app-java_1 bash`

`cd app-java/APPNAME`

`mvn clean package`

`mvn exec:java -Dexec.mainClass=APPNAME.CLASSNAME`

Also setup a producer to the source topic and consumer to listen to the sink topic:

`kafka-console-producer --broker-list localhost:19092,localhost:29092,localhost:39092 --topic streams-plaintext-input`
`kafka-console-consumer --bootstrap-server localhost:19092,localhost:29092,localhost:39092 --topic streams-pipe-output --from-beginning --formatter kafka.tools.DefaultMessageFormatter  --property print.value=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer`

