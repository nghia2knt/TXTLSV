package com.example.txtlserver.services;

import com.example.txtlserver.models.IssueInvoice;

import java.util.List;

public interface IssueInvoiceService {
    IssueInvoice findById(Long id);
    IssueInvoice createIssueInvoice(IssueInvoice issueInvoice);
    List<IssueInvoice> findAll(Boolean isPaid);
    List<IssueInvoice> findAllByUser(Long userId, Boolean isPaid);
    IssueInvoice findByUserId(Long id);
}
