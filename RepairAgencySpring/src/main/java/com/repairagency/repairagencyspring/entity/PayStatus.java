package com.repairagency.repairagencyspring.entity;

public enum PayStatus {
    DONE("paystatus.pay.done"),
    PAID("paystatus.pay.paid"),
    WAIT("paystatus.pay.wait"),
    CANCELED("paystatus.pay.canceled"),
    FREE("paystatus.pay.free");

    private final String messageId;

    PayStatus(String messageId){
        this.messageId=messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
