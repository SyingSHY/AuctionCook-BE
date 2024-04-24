package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.AuthResponseDTO;
import ajou.mse.auctioncookbe.entity.UserData;
import ajou.mse.auctioncookbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final long USER_TIME_TO_LIVE_SECOND = 600;

    @Autowired
    private UserRepository userRepository;

    public AuthResponseDTO loginUser(String name) {
        // User login process
        // ------------------
        // 사용자 로그인 처리

        UserData newUser = UserData.builder()
                .redisId(null)
                .name(name)
                .timeToLive(USER_TIME_TO_LIVE_SECOND)
                .build();

        UserData savedUser = userRepository.save(newUser);

        return AuthResponseDTO.builder()
                .resultStatus("SUCCESS")
                .userUUID(savedUser.getRedisId())
                .name(savedUser.getName())
                .description("Login Successfully.")
                .build();
    }

    public AuthResponseDTO logoutUser(String userUUID) {
        // User logout process
        // ------------------
        // 사용자 로그아웃 처리

        Optional<UserData> resultUserData = userRepository.findById(userUUID);

        if (resultUserData.isEmpty()) {
            return AuthResponseDTO.builder()
                    .resultStatus("FAILED")
                    .userUUID(userUUID)
                    .name("NOT FOUND")
                    .description("Cannot find " + userUUID + " from server. Maybe already logout.")
                    .build();
        }
        else {
            UserData userData = resultUserData.get();

            userRepository.deleteById(userUUID);

            return AuthResponseDTO.builder()
                    .resultStatus("SUCCESS")
                    .userUUID(userData.getRedisId())
                    .name(userData.getName())
                    .description("Logout Successfully.")
                    .build();
        }
    }

    public AuthResponseDTO renewUser(String userUUID) {
        // Redis 상의 User Entity에 대해 TimeToLive 업데이트

        return null;
    }
}
