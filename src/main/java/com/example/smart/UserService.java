package com.example.smart;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ID 중복 확인 메서드
    public boolean isIdExists(String id) {
        return userRepository.existsById(id);
    }

    // 회원가입 메서드
    @Transactional
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // 로그인 메서드
    public boolean loginUser(String id, String password) {
        User user = userRepository.findByIdAndPassword(id, password);
        return user != null;
    }

    // 사용자 정보 일부 업데이트 메서드 (PATCH 요청)
    @Transactional
    public User updateUserPartial(String id, Map<String, Object> updates) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // updates에 포함된 필드만 업데이트
            if (updates.containsKey("password")) {
                user.setPassword((String) updates.get("password"));
            }
            if (updates.containsKey("serialNumber")) {
                user.setSerialNumber((String) updates.get("serialNumber"));
            }

            // 변경된 정보 저장
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // 사용자 삭제 메서드
    @Transactional
    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}