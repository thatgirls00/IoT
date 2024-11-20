package com.example.smart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ID 중복 확인 요청 처리
    @GetMapping("/check-id/{id}")
    public ResponseEntity<Map<String, String>> checkIdExists(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();

        if (userService.isIdExists(id)) {
            response.put("status", "error");
            response.put("message", "이미 존재하는 ID입니다");
            return ResponseEntity.status(409).body(response);
        } else {
            response.put("status", "success");
            response.put("message", "사용 가능한 ID입니다");
            return ResponseEntity.ok(response);
        }
    }

    // 회원가입 요청 처리
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        try {
            userService.registerUser(user);
            response.put("status", "success");
            response.put("message", "회원가입 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "회원가입 실패");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User loginRequest) {
        Map<String, String> response = new HashMap<>();

        boolean loginSuccess = userService.loginUser(loginRequest.getId(), loginRequest.getPassword());
        if (loginSuccess) {
            response.put("status", "success");
            response.put("message", "로그인 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "로그인 실패");
            return ResponseEntity.status(401).body(response);
        }
    }

    // 사용자 정보 부분 업데이트
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();

        try {
            User updatedUser = userService.updateUserPartial(id, updates);
            response.put("status", "success");
            response.put("message", "사용자 정보 업데이트 성공");
            response.put("data", updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "사용자 정보 업데이트 실패");
            return ResponseEntity.status(401).body(response);
        }
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();

        // 사용자 존재 여부 확인
        if (!userService.isIdExists(id)) {
            response.put("status", "error");
            response.put("message", "사용자 삭제 실패");
            return ResponseEntity.status(404).body(response);  // 사용자 없으면 404 반환
        }

        // 사용자 삭제 처리
        userService.deleteUser(id);
        response.put("status", "success");
        response.put("message", "사용자 삭제 성공");
        return ResponseEntity.ok(response);
    }
}