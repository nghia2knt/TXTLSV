package com.example.txtlserver.models;

import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class IssueInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private StatusInvoiceType statusType;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime createAt;

    private String customerName;
    private String customerIDCard;
    private String customerPhone;
    private String customerEmail;

    private Boolean isPaid;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "issueInvoice")
    private List<Issue> issues;



}
