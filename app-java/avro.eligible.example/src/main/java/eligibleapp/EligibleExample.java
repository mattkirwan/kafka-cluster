package eligibleapp;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import eligibleapp.customer.EligibleSchema;

import java.util.Properties;

public class EligibleExample {

    public static void main(String[] args) throws Exception {

        KafkaStreams streams = buildEligibleStream(
                "localhost:19092,localhost:29092,localhost:39092",
                "localhost:8081",
                "/tmp/kafka-streams"
        );

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    static KafkaStreams buildEligibleStream(
            final String bootstrapServers,
            final String schemaRegistryUrl,
            final String stateDir
    ) {
        final Properties streamsConfig = new Properties();
        streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "eligible-example");
        streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfig.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        streamsConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String());
        streamsConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        streamsConfig.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);

        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        final Topology topology = builder.build();

        final KStream<String, String> eligibleStream = builder.stream("customer_eligible_1");

        return new KafkaStreams(topology, streamsConfig);
    };

}