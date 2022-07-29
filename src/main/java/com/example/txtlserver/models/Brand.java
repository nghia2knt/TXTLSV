package com.example.txtlserver.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
public class Brand{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime createAt;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="brand")
    private Collection<Car> cars;
    @JsonIgnore
    public Collection<Car> getCars() {
        return cars;
    }
}