package com.repairagency.repairagencyspring.entity;

public enum WorkStatus {
    FREE("workstatus.work.free"),
    PROCESS("workstatus.work.process"),
    DONE("workstatus.work.done");

    final private String messageId;

    WorkStatus(String messageId){
        this.messageId=messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
