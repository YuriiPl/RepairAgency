package com.repairagency.repairagencyspring.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ServiceDTO {

    @NotBlank(message = "wrongName")
    @Pattern(regexp = "^(?! )(?!.* $)[\\da-zA-Zа-щьюяіїєґА-ЩЮЯІЇЄҐ ')(№#%]{2,128}$", message = "wrongName")
    private String serviceName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().getName().equals(this.getClass().getName()) )) return false;
        ServiceDTO service = (ServiceDTO) o;
        return serviceName.equals(service.serviceName);
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }
}
