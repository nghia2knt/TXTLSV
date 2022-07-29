package com.example.txtlserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime createAt;

    @ManyToOne()
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne()
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

}
