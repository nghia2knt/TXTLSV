package com.example.txtlserver.services;

import com.example.txtlserver.models.Issue;

public interface IssueService {
    Issue findById(Long id);
    Issue createIssue(Issue issue);
}
