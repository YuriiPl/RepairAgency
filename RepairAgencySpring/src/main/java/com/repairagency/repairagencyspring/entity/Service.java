package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.ServiceDTO;
import lombok.*;

import javax.persistence.*;

@Getter
//@EqualsAndHashCode(of = {"name"})
@ToString(of = {"name"})
@NoArgsConstructor
@Entity
@Table(name = "Service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    //name varchar(128) not null unique
    @Column(name = "name", unique = true, nullable = false, length = 128)
    private String name;

    public Service(String name) {
        this.name = name;
    }

    public Service(ServiceDTO serviceDTO) {
        name = serviceDTO.getServiceName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        Service service = (Service) o;
        return name.equals(service.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
