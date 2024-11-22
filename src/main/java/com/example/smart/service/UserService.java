package com.example.smart.service;

import com.example.smart.domain.User;
import com.example.smart.repository.UserRepository;
import com.example.smart.util.SHAUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ID 중복 확인 메서드
    public boolean isIdExists(String id) {
        return userRepository.existsById(id);
    }

    // 회원가입 메서드
    @Transactional
    public void registerUser(User user) {
        String salt = SHAUtil.generateSalt();
        String encryptedPassword = SHAUtil.encryptWithSalt(user.getPassword(), salt);
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        userRepository.save(user);
    }

    // 로그인 메서드
    public boolean loginUser(String id, String password) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        String encryptedPassword = SHAUtil.encryptWithSalt(password, user.getSalt());

        return encryptedPassword.equals(user.getPassword());
    }

    // 사용자 정보 일부 업데이트 메서드 (PATCH 요청)
    @Transactional
    public User updateUserPartial(String id, Map<String, Object> updates) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        User user = existingUser.get();

//        if (updates.containsKey("password")) {
//            user.setEncryptedPassword((String) updates.get("password"));
//        }

        if (updates.containsKey("serialNumber")) {
            user.setSerialNumber((String) updates.get("serialNumber"));
        }

        // 변경된 정보 저장
        return userRepository.save(user);
    }

    // 사용자 삭제 메서드
    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
