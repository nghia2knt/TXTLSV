package com.example.txtlserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String info;
    private Double price;

    @ManyToOne()
    @JoinColumn(name = "issue_invoice_id", nullable = false)
    private IssueInvoice issueInvoice;

    @JsonIgnore
    public IssueInvoice getIssueInvoice() {
        return  null;
    }
}
