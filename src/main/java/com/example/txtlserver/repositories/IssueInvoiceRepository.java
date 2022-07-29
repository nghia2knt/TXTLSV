package com.example.txtlserver.repositories;

import com.example.txtlserver.models.Issue;
import com.example.txtlserver.models.IssueInvoice;
import com.example.txtlserver.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueInvoiceRepository extends JpaRepository<IssueInvoice,Long> {
    List<IssueInvoice> findAllByOrderByCreateAtDesc();
    List<IssueInvoice> findAllByUserIdOrderByCreateAtDesc(Long userId);
    List<IssueInvoice> findAllByUserIdAndIsPaidOrderByCreateAtDesc(Long userId, Boolean isPaid);
    List<IssueInvoice> findAllByIsPaidOrderByCreateAtDesc(Boolean isPaid);
}
