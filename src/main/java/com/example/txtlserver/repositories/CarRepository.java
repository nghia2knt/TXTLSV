package com.example.txtlserver.repositories;

import com.example.txtlserver.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends  JpaRepository<Car,Long> {
    @Query(value = "SELECT ca.* FROM car ca WHERE " +
            "(ca.delete_at is null) AND " +
            "(:brand_id is null or ca.brand_id = :brand_id) AND " +
            "(:transmission is null or ca.transmission = :transmission) AND " +
            "(:seats is null or ca.seats = :seats) AND " +
            "(:name is null or ca.name LIKE %:name%) AND "+
            "(:engine is null or ca.engine_type = :engine) AND "+
            "(:model is null or ca.model = :model) AND "+
            "(ca.is_lock != true) AND "+
            "NOT EXISTS ("+
            "SELECT c.* FROM car c inner join invoice i on c.id = i.car_id WHERE " +
            "(c.id = ca.id) AND (i.status_type != 'REFUNDED') AND" +
            "((:from_date BETWEEN i.start_time AND i.end_time) OR "+
            "(:to_date BETWEEN i.start_time AND i.end_time)" +
            "OR (:from_date <= i.start_time AND :to_date >=i.end_time))) "+

            "ORDER BY ca.create_at DESC "+
            "LIMIT :limit OFFSET :offset",nativeQuery = true)
    List<Car> findCarsFilter(@Param("limit") int limit,
                             @Param("offset") int offset,
                             @Param("brand_id") String brand_id,
                             @Param("transmission") String transmission,
                             @Param("seats") String seats,
                             @Param("name") String name,
                             @Param("engine") String engine,
                             @Param("from_date") String from_date,
                             @Param("to_date") String to_date,
                             @Param("model") String model);

    @Query(value = "SELECT ca.* FROM car ca WHERE " +
            "(:id is null or ca.id = :id) AND " +
            "(:brand_id is null or ca.brand_id = :brand_id) AND " +
            "(:transmission is null or ca.transmission = :transmission) AND " +
            "(:seats is null or ca.seats = :seats) AND " +
            "(:name is null or ca.name LIKE %:name%) AND "+
            "(:engine is null or ca.engine_type = :engine) AND "+
            "(:from_date is null or ca.create_at >= :from_date) AND "+
            "(:model is null or ca.model = :model) AND "+
            "(:to_date is null or ca.create_at <= :to_date) "+

            "ORDER BY ca.id DESC "+
            "LIMIT :limit OFFSET :offset",nativeQuery = true)
    List<Car> findCarsAdmin(
                            @Param("id") String id,
                            @Param("limit") int limit,
                            @Param("offset") int offset,
                            @Param("brand_id") String brand_id,
                            @Param("transmission") String transmission,
                            @Param("seats") String seats,
                            @Param("name") String name,
                            @Param("engine") String engine,
                            @Param("from_date") String from_date,
                            @Param("to_date") String to_date,
                            @Param("model") String model);

}
