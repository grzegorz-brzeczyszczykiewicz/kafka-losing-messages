package com.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.ZonedDateTime;
import java.util.Random;

@Configuration
@Component
@EnableTransactionManagement
@Slf4j
public class Consumer {

    @Autowired
    private MyDataRepository repository;
    private int methodCounter = 0;
    private int defectCounter = 0;

    //@Transactional
    @StreamListener(Stream.IN_CHANNEL)
    public void handleMessage(final Message<String> msg) throws Exception {
        //msg.getHeaders().forEach((key, value) -> System.out.println(key + ": " + value));
        final Acknowledgment acknowledgment = msg.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        System.out.println("payload: " + msg.getPayload());
        methodCounter++;
        System.out.println("method counter: " + methodCounter);
        if(!new Random().nextBoolean()){
            defectCounter++;
            System.out.println("defect counter: " + defectCounter);
            throw new Exception("kaputt");
        }

        System.out.println("");
        MyData data = new MyData();
        String[] split = msg.getPayload().split(" time: ");
        data.setMessage(split[0] + "_" + methodCounter + "_" + defectCounter);
        data.setTime(ZonedDateTime.parse(split[1]));
        repository.save(data);
//        if (acknowledgment!=null) {
//            System.out.println("ack nonnull");
            acknowledgment.acknowledge();
//        } else {
//            System.out.println("ack null");
//        }
    }
}
