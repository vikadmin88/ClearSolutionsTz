package org.clearsolutionstz.data.repository;

import org.clearsolutionstz.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
}
