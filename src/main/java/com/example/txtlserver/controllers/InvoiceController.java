package com.example.txtlserver.controllers;

import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.forms.CreateInvoiceRequest;
import com.example.txtlserver.forms.EditStatusRequest;
import com.example.txtlserver.forms.FilterInvoicesAdmin;
import com.example.txtlserver.models.Car;
import com.example.txtlserver.models.Invoice;
import com.example.txtlserver.models.ResponseObject;
import com.example.txtlserver.models.User;
import com.example.txtlserver.services.CarService;
import com.example.txtlserver.services.EmailService;
import com.example.txtlserver.services.InvoiceService;
import com.example.txtlserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;

    @Autowired
    public EmailService emailService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> findInvoiceById(@PathVariable("id") Long id){
        Invoice invoice = invoiceService.findById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Find invoice by id success", invoice)
        );
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> findInvoiceFilter(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "id", required = false, defaultValue = "") String id,
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "carId", required = false, defaultValue = "") String carId,
            @RequestParam(name = "customerName", required = false, defaultValue = "") String customerName,
            @RequestParam(name = "customerEmail", required = false, defaultValue = "") String customerEmail,
            @RequestParam(name = "customerPhone", required = false, defaultValue = "") String customerPhone,
            @RequestParam(name = "customeridcard", required = false, defaultValue = "") String customeridcard,
            @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
            @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate,
            @RequestParam(name = "startTime", required = false, defaultValue = "") String startTime,
            @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
            @RequestParam(name = "carName", required = false, defaultValue = "") String carName,
            @RequestParam(name = "carBrand", required = false, defaultValue = "") String carBrand,
            @RequestParam(name = "carLicensePlate", required = false, defaultValue = "") String carLicensePlate,
            @RequestParam(name = "status", required = false, defaultValue = "") String status
    ){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("c.id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("c.id").descending();
        }
        FilterInvoicesAdmin filter = FilterInvoicesAdmin.builder()
                .page(page-1)
                .size(size)
                .sort(sortable)
                .id(id)
                .userId(userId)
                .carId(carId)
                .customerName(customerName)
                .customerEmail(customerEmail)
                .customerPhone(customerPhone)
                .customeridcard(customeridcard)
                .fromDate(fromDate)
                .toDate(toDate)
                .startTime(startTime)
                .endTime(endTime)
                .carName(carName)
                .carBrand(carBrand)
                .carLicensePlate(carLicensePlate)
                .status(status)
                .build();
        List<Invoice> invoices = invoiceService.findInvoiceFilter(filter);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Find invoice success", invoices)
        );
    }
    @GetMapping("/count/wait")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> countInvoiceWait(
    ){
        int invoices = invoiceService.countInvoiceWait();
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Count invoice wait success", invoices)
        );
    }

    @GetMapping("/self")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> findInvoiceSelf(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "id", required = false, defaultValue = "") String id,
            @RequestParam(name = "carId", required = false, defaultValue = "") String carId,
            @RequestParam(name = "customerName", required = false, defaultValue = "") String customerName,
            @RequestParam(name = "customerEmail", required = false, defaultValue = "") String customerEmail,
            @RequestParam(name = "customerPhone", required = false, defaultValue = "") String customerPhone,
            @RequestParam(name = "customeridcard", required = false, defaultValue = "") String customeridcard,
            @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
            @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate,
            @RequestParam(name = "startTime", required = false, defaultValue = "") String startTime,
            @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
            @RequestParam(name = "carName", required = false, defaultValue = "") String carName,
            @RequestParam(name = "carBrand", required = false, defaultValue = "") String carBrand,
            @RequestParam(name = "carLicensePlate", required = false, defaultValue = "") String carLicensePlate
    ){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("c.id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("c.id").descending();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);

        FilterInvoicesAdmin filter = FilterInvoicesAdmin.builder()
                .page(page-1)
                .size(size)
                .sort(sortable)
                .id(id)
                .userId(foundUser.getId().toString())
                .carId(carId)
                .customerName(customerName)
                .customerEmail(customerEmail)
                .customerPhone(customerPhone)
                .customeridcard(customeridcard)
                .fromDate(fromDate)
                .toDate(toDate)
                .startTime(startTime)
                .endTime(endTime)
                .carName(carName)
                .carBrand(carBrand)
                .carLicensePlate(carLicensePlate)
                .status("")
                .build();
        List<Invoice> invoices = invoiceService.findInvoiceFilter(filter);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Find invoice by id success", invoices)
        );
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> editInvoiceStatusById(@PathVariable("id") Long id, @RequestBody EditStatusRequest statusRequest) throws MessagingException{
        Invoice invoice = invoiceService.findById(id);
        Invoice invoiceResponse = Invoice.builder().build();
        if (statusRequest.getStatus() == StatusInvoiceType.CONFIRMED) {
            if (invoice.getStatusType() == StatusInvoiceType.WAIT) {
                invoiceResponse = invoiceService.setStatus(invoice,statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(),"HÓA ĐƠN TXTL: "+invoice.getId(),"Hóa đơn của bạn đã được chấp nhận, kiểm tra lại lịch đặt để xem chi tiết.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
          }
        if (statusRequest.getStatus() == StatusInvoiceType.CANCEL) {
            invoiceResponse = invoiceService.setStatus(invoice,statusRequest.getStatus());
            emailService.sendOtpMessage(invoice.getCustomerEmail(),"HÓA ĐƠN TXTL: "+invoice.getId(),"Hóa đơn của bạn đã bị hủy và xóa khỏi hệ thống.");
        }
        if (statusRequest.getStatus() == StatusInvoiceType.DONE) {
            if (invoice.getStatusType() == StatusInvoiceType.CONFIRMED) {
                invoiceResponse = invoiceService.setStatus(invoice, statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(), "HÓA ĐƠN TXTL: " + invoice.getId(), "Bạn đã hoàn thành lịch thuê cho hóa đơn.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
        }
        if (statusRequest.getStatus() == StatusInvoiceType.LATE) {
            if (invoice.getStatusType() == StatusInvoiceType.CONFIRMED) {
                invoiceResponse = invoiceService.setStatus(invoice, statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(), "HÓA ĐƠN TXTL: " + invoice.getId(), "Bạn đã bị trễ lịch trả xe theo quy định.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
        }
        if (statusRequest.getStatus() == StatusInvoiceType.REQ_REFUND) {
            if (invoice.getStatusType() == StatusInvoiceType.CONFIRMED) {
                invoiceResponse = invoiceService.setStatus(invoice,statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(), "HÓA ĐƠN TXTL: " + invoice.getId(), "Bạn đã gửi yêu cầu hoàn tiền cho hóa đơn.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
        }
        if (statusRequest.getStatus() == StatusInvoiceType.REFUNDED) {
            if (invoice.getStatusType() == StatusInvoiceType.REQ_REFUND) {
                invoiceResponse = invoiceService.setStatus(invoice,statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(), "HÓA ĐƠN TXTL: " + invoice.getId(), "Bạn đã xác nhận đã được hoàn tiền của hóa đơn.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
        }
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change status invoice by id success", invoiceResponse)
        );
    }

    @PutMapping("/refund/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> editInvoiceStatusByIdUser(@PathVariable("id") Long id, @RequestBody EditStatusRequest statusRequest) throws MessagingException{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        Invoice invoice = invoiceService.findByUserAndId(foundUser,id);
        Invoice invoiceResponse = Invoice.builder().build();
        if (statusRequest.getStatus() == StatusInvoiceType.REQ_REFUND) {
            if (invoice.getStatusType() == StatusInvoiceType.CONFIRMED) {
                invoiceResponse = invoiceService.setStatus(invoice,statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(), "HÓA ĐƠN TXTL: " + invoice.getId(), "Bạn đã gửi yêu cầu hoàn tiền cho hóa đơn.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
        }
        if (statusRequest.getStatus() == StatusInvoiceType.REFUNDED) {
            if (invoice.getStatusType() == StatusInvoiceType.REQ_REFUND) {
                invoiceResponse = invoiceService.setStatus(invoice,statusRequest.getStatus());
                emailService.sendOtpMessage(invoice.getCustomerEmail(), "HÓA ĐƠN TXTL: " + invoice.getId(), "Bạn đã xác nhận đã được hoàn tiền của hóa đơn.");
            }else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error");
            }
        }
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change status invoice by id success", invoiceResponse)
        );
    }

    @GetMapping("/self/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> findInvoiceByUserAndId(@PathVariable("id") Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User findUser = userService.getUserByEmail(email);
        Invoice invoice = invoiceService.findByUserAndId(findUser,id);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Find invoice by id success", invoice)
        );
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createInvoice(@Valid @RequestBody CreateInvoiceRequest createInvoiceRequest) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        Car foundCar = carService.findById(createInvoiceRequest.getCarId());
        Invoice invoice = Invoice.builder()
                .user(foundUser)
                .car(foundCar)
                .startTime(createInvoiceRequest.getStartTime())
                .endTime(createInvoiceRequest.getEndTime())
                .createAt(LocalDateTime.now())
                .statusType(StatusInvoiceType.WAIT)
                .customerEmail(user.getEmail())
                .customerIDCard(user.getIdCard())
                .customerName(user.getName())
                .customerPhone(user.getPhoneNumber())
                .carName(foundCar.getName())
                .carBrand(foundCar.getBrand().getName())
                .carLicensePlate(foundCar.getLicensePlate())
                .build();
        invoice = invoiceService.createInvoice(invoice);
        emailService.sendOtpMessage(invoice.getCustomerEmail(),"HÓA ĐƠN TXTL: "+invoice.getId(),"Hóa đơn đã được tạo thành công, kiểm tra lại lịch đặt - hóa đơn để cập nhật tình hình.");
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Create invoice success", invoice)
        );
    }
}
