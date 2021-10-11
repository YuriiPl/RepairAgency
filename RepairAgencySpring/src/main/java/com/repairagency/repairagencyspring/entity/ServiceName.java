package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.ServiceDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
//@EqualsAndHashCode(of = {"name"})
@ToString(of = {"name"})
@NoArgsConstructor
@Entity
@Table
public class ServiceName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    //name varchar(128) not null unique
    @Column( unique = true, nullable = false, length = 128)
    private String name;


//    @OneToMany (mappedBy="serviceName", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
//    private List<Service> services;

    public ServiceName(String name) {
        this.name = name;
    }

    public ServiceName(ServiceDTO serviceDTO) {
        name = serviceDTO.getServiceName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceName)) return false;
        ServiceName service = (ServiceName) o;
        return name.equals(service.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
