package com.example.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Producer {

final KafkaTemplate<String, String> kafkaTemplate;

    public Producer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);



        this.kafkaTemplate = new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory<>(configProps));
    }

    public static void main(String[] args) {
        new Producer().send("my-test-topic", "100");
    }

    public void send(String destTopic, String numberMessagesStr) {
        Integer numberMessages = Integer.valueOf(numberMessagesStr);
        for (int i=0; i<numberMessages; i++) {
            final String rciEvent = "seq: " + i + " time: " + ZonedDateTime.now();
            final Message<?> msg = MessageBuilder.withPayload(rciEvent)
                    .setHeader(KafkaHeaders.TOPIC, destTopic)
                    .build();
            this.sendMsg(msg);
        }
    }

    /**
     * send  Message
     *
     * @param msg
     * @return
     */
    private void sendMsg(final Message<?> msg) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(msg);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + msg +
                                   "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                                   + msg + "] due to : " + ex.getMessage());
            }
        });

        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException executionEx) {
        }
    }
}
