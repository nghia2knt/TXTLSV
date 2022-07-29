package com.example.txtlserver.services;

import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.forms.FilterInvoicesAdmin;
import com.example.txtlserver.models.Invoice;
import com.example.txtlserver.models.User;
import com.example.txtlserver.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    private InvoiceRepository invoiceRepository;


    @Override
    public Invoice findById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (!invoice.isPresent()) {
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found invoice with id "+id);
        }
        return invoice.get();
    }

    @Override
    public Invoice findByUserAndId(User user, Long id) {
        Optional<Invoice> invoice = invoiceRepository.findByUserAndId(user,id);
        if (!invoice.isPresent()) {
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found invoice with id "+id);
        }
        return invoice.get();
    }

    @Override
    public Invoice setStatus(Invoice invoice, StatusInvoiceType statusType) {
        if (statusType == StatusInvoiceType.CANCEL) {
            invoiceRepository.delete(invoice);
            return Invoice.builder().build();
        }
        invoice.setStatusType(statusType);
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public List<Invoice> findInvoiceFilter(FilterInvoicesAdmin filter) {

        if (filter.getId().equalsIgnoreCase("")) {
            filter.setId(null);
        }
        if (filter.getUserId().equalsIgnoreCase("")) {
            filter.setUserId(null);
        }
        if (filter.getCarId().equalsIgnoreCase("")) {
            filter.setCarId(null);
        }
        if (filter.getCustomerName().equalsIgnoreCase("")) {
            filter.setCustomerName(null);
        }
        if (filter.getCustomerPhone().equalsIgnoreCase("")) {
            filter.setCustomerPhone(null);
        }
        if (filter.getCustomerEmail().equalsIgnoreCase("")) {
            filter.setCustomerEmail(null);
        }
        if (filter.getCustomeridcard().equalsIgnoreCase("")) {
            filter.setCustomeridcard(null);
        }
        if (filter.getFromDate().equalsIgnoreCase("")){
            filter.setFromDate(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime fromDate = LocalDateTime.parse(filter.getFromDate() , formatter);
        }
        if (filter.getToDate().equalsIgnoreCase("")){
            filter.setToDate(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime toDate = LocalDateTime.parse(filter.getToDate(), formatter);
        }
        if (filter.getStartTime().equalsIgnoreCase("")){
            filter.setStartTime(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime StartTime = LocalDateTime.parse(filter.getStartTime(), formatter);
        }
        if (filter.getEndTime().equalsIgnoreCase("")){
            filter.setEndTime(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime EndTime = LocalDateTime.parse(filter.getEndTime(), formatter);
        }
        if (filter.getCarName().equalsIgnoreCase("")){
            filter.setCarName(null);
        }
        if (filter.getCarBrand().equalsIgnoreCase("")){
            filter.setCarBrand(null);
        }
        if (filter.getCarLicensePlate().equalsIgnoreCase("")){
            filter.setCarLicensePlate(null);
        }
        if (filter.getStatus().equalsIgnoreCase("")){
            filter.setStatus(null);
        }
        List<Invoice> invoices = invoiceRepository.findInvoicesAdmin(filter.getId(),
                filter.getUserId(),
                filter.getCarId(),
                filter.getSize(),
                filter.getPage(),
                filter.getCustomerName(),
                filter.getCustomerEmail(),
                filter.getCustomerPhone(),
                filter.getCustomeridcard(),
                filter.getFromDate(),
                filter.getToDate(),
                filter.getStartTime(),
                filter.getEndTime(),
                filter.getCarName(),
                filter.getCarBrand(),
                filter.getCarLicensePlate(),
                filter.getStatus()
                );
        return invoices;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        double duration = Duration.between(invoice.getStartTime(),  invoice.getEndTime()).toHours();
        if (duration<=0) {
           throw new CustomException(HttpStatus.BAD_REQUEST,"Start time must < end time, > 1 hour");
        }
        int count = (invoiceRepository.findInvoiceWithCarHaveCanlendar(invoice.getCar().getId().toString(),invoice.getStartTime().toString(),invoice.getEndTime().toString()).size());
        if (count > 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"This car have another calendar");
        }
        double totalPrice = duration * invoice.getCar().getPrice();
        invoice.setTotalPrice(totalPrice);
        invoice = invoiceRepository.save(invoice);
        return invoice;
    }


    @Override
    public int countInvoiceWait(){
       return invoiceRepository.countAllByStatusType(StatusInvoiceType.WAIT);
    }
}
