package com.example.smart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 로그인 시 사용할 사용자 조회 메서드
    User findByIdAndPassword(String id, String password);
}