package com.example.txtlserver.services;

import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.example.txtlserver.forms.FilterInvoicesAdmin;
import com.example.txtlserver.models.Invoice;
import com.example.txtlserver.models.User;

import java.util.List;

public interface InvoiceService {
    Invoice findById(Long id);
    Invoice createInvoice(Invoice invoice);
    Invoice findByUserAndId(User user, Long id);
    Invoice setStatus(Invoice invoice, StatusInvoiceType statusType);
    List<Invoice> findInvoiceFilter(FilterInvoicesAdmin filter);
    int countInvoiceWait();
}
