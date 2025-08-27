package org.demo.model;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

public class Coffee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotNull
    private String name;

    @NotNull
    private String price;

    private long timestamp;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public Coffee(){
        name = "unknown";
        price = "$ 0.00";
        timestamp = System.currentTimeMillis();
    }

    public Coffee(long coffeeId, String coffeName, String coffeePrice) {
        id = coffeeId;
        name = coffeName;
        price = coffeePrice;
        timestamp = System.currentTimeMillis();
    }

    public void setTimestamp(long time) {
        timestamp = time;
    }

}
