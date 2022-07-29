package com.example.txtlserver.repositories;

import com.example.txtlserver.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
    @Query("SELECT e FROM User e")
    Page<User> findUsers(Pageable pageable);

    @Query(value = "SELECT u.* FROM user u WHERE " +
            "(:id is null or u.id = :id) AND " +
            "(:birth_day is null or u.birth_day = :birth_day) AND " +
            "(:email is null or u.email LIKE %:email%) AND " +
            "(:id_card is null or u.id_card = :id_card) AND " +
            "(:name is null or u.name LIKE %:name%) AND "+
            "(:phone_number is null or u.phone_number = :phone_number) AND "+
            "(:from_date is null or u.create_at >= :from_date) AND "+
            "(:to_date is null or u.create_at <= :to_date) "+
            "ORDER BY u.create_at DESC "+
            "LIMIT :limit OFFSET :offset",nativeQuery = true)
    List<User> findUsersAdmin(
            @Param("id") String id,
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("name") String name,
            @Param("birth_day") String birth_day,
            @Param("email") String email,
            @Param("id_card") String id_card,
            @Param("phone_number") String phone_number,
            @Param("from_date") String from_date,
            @Param("to_date") String to_date
    );
}
