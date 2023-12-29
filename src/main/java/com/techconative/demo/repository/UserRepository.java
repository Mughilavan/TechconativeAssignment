package com.techconative.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techconative.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT max(id) FROM User")
    Long findLatestUserId();
}
