package com.repairagency.repairagencyspring.entity;

public enum PayStatus {
    FREE("paystatus.pay.free"),
    WAIT("paystatus.pay.wait"),
    PAID("paystatus.pay.paid"),
    DONE("paystatus.pay.done"),
    CANCELED("paystatus.pay.canceled");

    private final String messageId;

    PayStatus(String messageId){
        this.messageId=messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
