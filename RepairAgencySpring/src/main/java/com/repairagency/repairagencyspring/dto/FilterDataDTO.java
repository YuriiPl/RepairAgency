package com.repairagency.repairagencyspring.dto;

import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class FilterDataDTO {
    private String repairerName;
    private PayStatus payStatus;
    private WorkStatus workStatus;
    public String requestString(){
         return (repairerName==null || repairerName.length()==0) && payStatus==null && workStatus==null? "" :
             String.format("&repairerName=%s&payStatus=%s&workStatus=%s",
                        repairerName != null && repairerName.length()>0? repairerName : "",
                        payStatus != null? payStatus.name() : "",
                        workStatus != null? workStatus.name() : ""
                    );
    }
}
