package com.example.consumer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
public class MyData implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private ZonedDateTime time;

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setTime(final ZonedDateTime time){
        this.time = time;
    }
}
