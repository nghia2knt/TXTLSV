package com.example.txtlserver.services;

import com.example.txtlserver.models.IssueInvoice;
import com.example.txtlserver.repositories.IssueInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueInvoiceServiceImpl implements IssueInvoiceService{

    @Autowired
    private IssueInvoiceRepository issueInvoiceRepository;

    @Override
    public IssueInvoice findById(Long id) {
        IssueInvoice result = issueInvoiceRepository.findById(id).get();
        return result;
    }

    @Override
    public IssueInvoice createIssueInvoice(IssueInvoice issueInvoice) {
        IssueInvoice result = issueInvoiceRepository.save(issueInvoice);
        return result;
    }

    @Override
    public List<IssueInvoice> findAll(Boolean isPaid) {
        if (isPaid !=null) {
            List<IssueInvoice> result = issueInvoiceRepository.findAllByIsPaidOrderByCreateAtDesc(isPaid);
            return result;
        }else {
            List<IssueInvoice> result = issueInvoiceRepository.findAllByOrderByCreateAtDesc();
            return result;
        }
    }

    @Override
    public List<IssueInvoice> findAllByUser(Long userId, Boolean isPaid) {
        if (isPaid !=null) {
            List<IssueInvoice> result = issueInvoiceRepository.findAllByUserIdAndIsPaidOrderByCreateAtDesc(userId, isPaid);
            return result;
        }else {
            List<IssueInvoice> result = issueInvoiceRepository.findAllByUserIdOrderByCreateAtDesc(userId);
            return result;
        }
    }

    @Override
    public IssueInvoice findByUserId(Long id) {
        return null;
    }
}
