package com.company;

public class CausalLink {
    int consumer;
    int producer;
    String literal;

    public CausalLink(int consumer, int producer, String literal){
        this.consumer=consumer;
        this.producer=producer;
        this.literal=literal;
    }
    public int getConsumer() {
        return consumer;
    }

    public int getProducer() {
        return producer;
    }

    public void setProducer(int producer) {
        this.producer = producer;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public void setConsumer(int consumer) {
        this.consumer = consumer;
    }


}
