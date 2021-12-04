package com.example.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


public interface Stream {

	String IN_CHANNEL = "my-channel";

	@Input(IN_CHANNEL)
	SubscribableChannel rcvStream();

}
