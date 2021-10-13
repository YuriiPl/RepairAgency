package com.repairagency.repairagencyspring.entity;

public enum PayStatus {
    WAIT("paystatus.pay.wait"),DONE("paystatus.pay.done"),CANCELED("paystatus.pay.canceled"),FREE("paystatus.pay.free");

    private final String messageId;

    PayStatus(String messageId){
        this.messageId=messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
