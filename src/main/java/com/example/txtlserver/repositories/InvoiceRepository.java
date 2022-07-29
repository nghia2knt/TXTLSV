package com.example.txtlserver.repositories;

import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.example.txtlserver.models.Invoice;
import com.example.txtlserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Optional<Invoice> findByUserAndId(User user, Long id);

    @Query(value = "SELECT i.* FROM car c inner join invoice i on c.id = i.car_id WHERE " +
            "(c.id = :car_id) AND (i.status_type != 'REFUNDED') AND" +
            "((:from_date BETWEEN i.start_time AND i.end_time) OR "+
            "(:to_date BETWEEN i.start_time AND i.end_time)" +
            "OR (:from_date <= i.start_time AND :to_date >=i.end_time))",nativeQuery = true)
    List<Invoice> findInvoiceWithCarHaveCanlendar(
            @Param("car_id") String car_id,
            @Param("from_date") String from_date,
            @Param("to_date") String to_date
    );

    @Query(value = "SELECT i.* FROM invoice i WHERE " +
            "(:id is null or i.id = :id) AND " +
            "(:user_id is null or i.user_id = :user_id) AND " +
            "(:car_id is null or i.car_id = :car_id) AND " +
            "(:status is null or i.status_type = :status) AND " +
            "(:customer_name is null or i.customer_name LIKE %:customer_name%) AND " +
            "(:customer_email is null or i.customer_email LIKE %:customer_email%) AND " +
            "(:customer_phone is null or i.customer_phone LIKE %:customer_phone%) AND " +
            "(:customeridcard is null or i.customeridcard LIKE %:customeridcard%) AND " +
            "(:car_name is null or i.car_name LIKE %:car_name%) AND " +
            "(:car_brand is null or i.car_brand LIKE %:car_brand%) AND " +
            "(:car_license_plate is null or i.car_license_plate LIKE %:car_license_plate%) AND " +
            "(:start_time is null or i.start_time = :start_time) AND "+
            "(:end_time is null or i.end_time = :end_time) AND "+
            "(:from_date is null or i.create_at >= :from_date) AND "+
            "(:to_date is null or i.create_at <= :to_date) "+
            "ORDER BY i.create_at DESC "+
            "LIMIT :limit OFFSET :offset",nativeQuery = true)
    List<Invoice> findInvoicesAdmin(
            @Param("id") String id,
            @Param("user_id") String user_id,
            @Param("car_id") String car_id,
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("customer_name") String customer_name,
            @Param("customer_email") String customer_email,
            @Param("customer_phone") String customer_phone,
            @Param("customeridcard") String customeridcard,
            @Param("from_date") String from_date,
            @Param("to_date") String to_date,
            @Param("start_time") String start_time,
            @Param("end_time") String end_time,
            @Param("car_name") String car_name,
            @Param("car_brand") String car_brand,
            @Param("car_license_plate") String car_license_plate,
            @Param("status") String status
    );

    int countAllByStatusType(StatusInvoiceType statusInvoiceType);
}
