const Kafka = require('node-rdkafka');

const producer = new Kafka.Producer({
    'metadata.broker.list': 'kafka-host1:9092,kafka-host2:9092'
});

producer.connect();

producer.on('ready', function() {
    try {
        producer.produce(
            // Topic to send the message to
            'matts_test_topic',
            // optionally we can manually specify a partition for the message
            // this defaults to -1 - which will use librdkafka's default partitioner (consistent random for keyed messages, random for unkeyed messages)
            null,
            // Message to send. Must be a buffer
            new Buffer('Awesome message'),
            // for keyed messages, we also specify the key - note that this field is optional
            'Stormwind',
            // you can send a timestamp here. If your broker version supports it,
            // it will get added. Otherwise, we default to 0
            Date.now(),
            // you can send an opaque token here, which gets passed along
            // to your delivery reports
        );
    } catch (err) {
        console.error('A problem occurred when sending our message');
        console.error(err);
    }
});

producer.on('event.error', function(err) {
    console.error('Error from producer');
    console.error(err);
})