package ajou.mse.auctioncookbe.controller;

import ajou.mse.auctioncookbe.DTO.AuthResponseDTO;
import ajou.mse.auctioncookbe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth")
    public ResponseEntity<AuthResponseDTO> issueUserToken(@RequestParam String name) {
        // Generate UUID when a client clicked 'Game Start' and sent request
        // After generation, the client can be distinguished by UUID
        // --------------------------------------
        // 사용자 최초 접속 시 UUID 생성
        // 발급된 UUID 정보를 바탕으로 사용자 식별

        AuthResponseDTO authResponseDTO;

        if (isNicknameValid(name)) {
            // When input 'name' is appropriate String
            // -----------------------------------
            // 사용자가 입력한 name이 정상 문자열인 경우

            authResponseDTO = authService.loginUser(name);

            return ResponseEntity.ok(authResponseDTO);
        }
        else {
            // When input 'name' is inappropriate String
            // -----------------------------------
            // 사용자가 입력한 name이 비정상 문자열인 경우

            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/auth")
    public ResponseEntity<AuthResponseDTO> logoutUserToken(@RequestParam String userUUID) {
        // 사용자 UUID 제시하여 로그아웃

        AuthResponseDTO authResponseDTO;

        authResponseDTO = authService.logoutUser(userUUID);

        return ResponseEntity.ok(authResponseDTO);
    }

    private boolean isNicknameValid(String name) {
        if (name.isBlank()) return false;

        return true;
    }
}
