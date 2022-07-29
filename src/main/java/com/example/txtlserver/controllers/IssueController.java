package com.example.txtlserver.controllers;

import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.forms.CreateInvoiceRequest;
import com.example.txtlserver.forms.CreateIssueInvoiceRequest;
import com.example.txtlserver.models.*;
import com.example.txtlserver.services.IssueInvoiceService;
import com.example.txtlserver.services.IssueService;
import com.example.txtlserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    private IssueInvoiceService issueInvoiceService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createIssueInvoice(@Valid @RequestBody CreateIssueInvoiceRequest request){
        List<Issue> listIssue = new ArrayList<Issue>();
        User newUser = userService.getUserByID(request.getUserId());
        IssueInvoice invoices = IssueInvoice.builder()
                .createAt(LocalDateTime.now())
                .user(newUser)
                .customerEmail(newUser.getEmail())
                .customerIDCard(newUser.getIdCard())
                .customerName(newUser.getName())
                .customerPhone(newUser.getPhoneNumber())
                .totalPrice(0.0)
                .isPaid(false)
                .build();
        IssueInvoice saveInvoice = issueInvoiceService.createIssueInvoice(invoices);
        Double price = 0.0;
        for (Issue issue: request.getIssues()){
            issue.setIssueInvoice(saveInvoice);
            Issue saveIssue = issueService.createIssue(issue);
            listIssue.add(saveIssue);
            price = price + saveIssue.getPrice();
        }
        saveInvoice.setTotalPrice(price);
        IssueInvoice result = issueInvoiceService.createIssueInvoice(saveInvoice);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "create success",result)
        );
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getIssueInvoice(@RequestParam(name = "isPaid", required = false, defaultValue = "") String paid){
        Boolean isPaid = null;
        if (paid.equalsIgnoreCase("true")) {
            isPaid = true;
        }
        if (paid.equalsIgnoreCase("false")) {
            isPaid = false;
        }
        List<IssueInvoice> invoices = issueInvoiceService.findAll(isPaid);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get all success",invoices)
        );
    }

    @GetMapping("/self")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getSelfIssueInvoice(@RequestParam(name = "isPaid", required = false, defaultValue = "") String paid){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        Boolean isPaid = null;
        if (paid.equalsIgnoreCase("true")) {
            isPaid = true;
        }
        if (paid.equalsIgnoreCase("false")) {
            isPaid = false;
        }
        List<IssueInvoice> invoices = issueInvoiceService.findAllByUser(foundUser.getId(),isPaid);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get all success",invoices)
        );
    }

    @GetMapping("/self/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getSelfIssueInvoiceId(@PathVariable("id") Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        IssueInvoice foundIssue = issueInvoiceService.findById(id);
        if (foundUser.getId() != foundIssue.getUser().getId()) {
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found");
        }
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get invoices success",foundIssue)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getIssueInvoiceId(@PathVariable("id") Long id){
        IssueInvoice foundIssue = issueInvoiceService.findById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get invoices success",foundIssue)
        );
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> setTruePaid(@PathVariable("id") Long id){
        IssueInvoice foundIssue = issueInvoiceService.findById(id);
        foundIssue.setIsPaid(true);
        IssueInvoice saveIssue = issueInvoiceService.createIssueInvoice(foundIssue);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "set paid success",saveIssue)
        );
    }
}
