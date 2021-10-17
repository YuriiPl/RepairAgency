package com.repairagency.repairagencyspring.dto;

import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterDataDTO {
    private String repairerName;
    private PayStatus payStatus;
    private WorkStatus workStatus;
    public String requestString(){
        return String.format("repairerName=%s&payStatus=%s&workStatus=%s",
                    repairerName != null && repairerName.length()>0? repairerName : "",
                    payStatus != null? payStatus.name() : "",
                    workStatus != null? workStatus.name() : ""
                );
    }
}
