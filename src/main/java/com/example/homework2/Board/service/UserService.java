package com.example.homework2.Board.service;


import com.example.homework2.Board.dto.MegResponseDto;
import com.example.homework2.Board.dto.UserRequestDto;
import com.example.homework2.Board.entity.ErrorCode.ErrorCode;
import com.example.homework2.Board.entity.ErrorCode.UserRoleEnum;
import com.example.homework2.Board.entity.User;
import com.example.homework2.Board.exception.ApiException;
import com.example.homework2.Board.jwt.JwtUtil;
import com.example.homework2.Board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

//                회원가입
@Transactional
    public ResponseEntity<MegResponseDto> signup(UserRequestDto userRequestDto) {
    String username = userRequestDto.getUsername();
    String password = passwordEncoder.encode(userRequestDto.getPassword());


//        회원 중복 확인
    Optional<User> found = userRepository.findByUsername(username);
    if (found.isPresent()) {
        throw new ApiException(ErrorCode.DUPLICATED_USERNAME);
    }

//    입력한 username, password, admin으로 user 객체 만들어 repository 저장
    UserRoleEnum role = userRequestDto.getAdmin() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
    userRepository.save(User.User_Service(username, password, role));

    return ResponseEntity.ok(MegResponseDto.User_ServiceCode(HttpStatus.OK,"회원가입 성공"));
}


@Transactional
    public ResponseEntity<MegResponseDto> login(UserRequestDto requestDto) {
    String username = requestDto.getUsername();
    String password = requestDto.getPassword();

//              사용자 확인 및 비밀번호 확인
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
        throw new ApiException(ErrorCode.NOT_MATCHING_INFO);
    }



//    header에 들어갈 JWT 세팅
    HttpHeaders headers = new HttpHeaders();
    headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));

    return ResponseEntity.ok()
            .headers(headers)
            .body(MegResponseDto.User_ServiceCode(HttpStatus.OK,"로그인 성공"));

}

}
