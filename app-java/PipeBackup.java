package pipeavroapp;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;

import org.apache.avro.generic.GenericRecord;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import customer.Eligible;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Pipe {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "pipe-avro-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092,localhost:39092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericRecord.class);
        
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "localhost:8081");

        // final Serde<String> stringSerde = Serdes.String();
        // final Serde<Long> longSerde = Serdes.Long();

        // final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
        // final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
        // final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

        final StreamsBuilder builder = new StreamsBuilder();


        KStream<String, GenericRecord> source = builder
            .stream("streams-pipe-avro-input");
            
            source.print();
        
        // ObjectMapper mapper = new ObjectMapper();
            
        // KStream<String, JsonNode> jsonParse =
        //     source.map((k,v) ->
        //         new KeyValue<String, JsonNode>(k, mapper.readTree(v))).<String, Eligible>map((k,v) -> {
        //             final Eligible newValue = new Eligible(v.get("customer_id").textValue(), v.get("promo_id").textValue());
        //             return new KeyValue<String, Eligible>(k, v);
        //         });
                
        //     jsonParse.to(stringSerde, new SpecificAvroSerde(), "streams-pipe-avro-output");

        // KStream<String, JsonNode> jsonParse = 
            // source.map((k,v) ->
                // new KeyValue<String, JsonNode>(k, mapper.readTree(v)));
            // .to(stringSerde, jsonSerde, "streams-pipe-avro-output");


        




        // KStream<String, JsonNode> jsonParse =
            // source.through(Serdes.String(), jsonSerializer, "streams-pipe-avro-output");



        // Kstream<String, JsonNode> jsonParse = source.map(new KeyValueMapper<String, JsonNode, KeyValue<String, JsonNode>>() {
        //     @Override
        //     public KeyValue<String, JsonNode> apply(final String key, final Eligible value) {
        //         final Eligible newValue = new Eligible(key, value);
        //         return new KeyValue<>(key, );
        //     }
        // });


        // KStream<String, String> processed = source.map(new KeyValueMapper<String, String, KeyValue<String, String>>() {
        //     @Override
        //     public KeyValue<String, String> apply(final String key, final String value) {
        //         final Eligible newValue = new Eligible(key, value);
        //         return new KeyValue<>("boop", "Banana");
        //     }
        // });

        // processed.to("streams-pipe-avro-output");



        // KStream<String, String> source = builder.stream("streams-pipe-avro-plaintext-input");

        // KStream<String, JsonNode> jsonParse =
        //     source.map((k,v) ->
        //         new KeyValue<String, JsonNode>(k, new ))

        // KStream<String, Eligible> processed = 
        //     sourceGeneric.map((k, v) -> 
        //         new KeyValue<String, Eligible>(k, new Eligible ((String) v.get("customer_id"), (String) v.get("promo_id").toString()) ));

        // processed.to(Serdes.String(), new SpecificAvroSerde(), "streams-pipe-avro-output");



        // // map the user id as key
        // .map(new KeyValueMapper<String, WikiFeed, KeyValue<String, WikiFeed>>() {
        //     @Override
        //     public KeyValue<String, WikiFeed> apply(final String key, final WikiFeed value) {
        //         return new KeyValue<>(value.getUser(), value);
        //     }
        // })

        // KStream<String, Eligible> processed = source
        //     .map(new KeyValueMapper<String, Eligible, KeyValue<String, Eligible>>() {
        //         @Override
        //         public KeyValue<String, Eligible> apply(final String key, final Eligible value) {
        //             return new KeyValue<>(value.getCustomerId(), value.getPromoId());
        //         }
        //     });

        // KStream<String, Eligible> processed = source.map((k, v) -> KeyValue<>(stringSerde, new Eligible())).to("streams-pipe-avro-output");
        
        // KStream<String, Eligible> processed = source.through(stringSerde, new Eligible(), "streams-pipe-avro-output");



        final Topology topology = builder.build();
        // System.out.println(topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
