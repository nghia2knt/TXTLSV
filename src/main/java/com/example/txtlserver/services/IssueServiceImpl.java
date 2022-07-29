package com.example.txtlserver.services;

import com.example.txtlserver.models.Issue;
import com.example.txtlserver.repositories.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;


    @Override
    public Issue findById(Long id) {
        return null;
    }

    @Override
    public Issue createIssue(Issue issue) {
        Issue result = issueRepository.save(issue);
        return result;
    }
}
