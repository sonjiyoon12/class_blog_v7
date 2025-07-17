package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog._core.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service // IoC
@Transactional(readOnly = true) // 클래스 레벨에서의 읽기 전용 설정
public class UserService {

    private final UserJpaRepository userJpaRepository;

    // 회원 가입 처리
    @Transactional // 메서드 레벨에서 쓰기 전용 트랜잭션 활성화
    public UserResponse.JoinDTO join(UserRequest.JoinDTO joinDTO) {
        //1. 사용자명 중복 체크
        userJpaRepository.findByUsername(joinDTO.getUsername())
                .ifPresent(user1 -> {
                    throw new Exception400("이미 존재하는 사용자명입니다");
                });

        // 2. 회원 가입 처리
        User savedUser = userJpaRepository.save(joinDTO.toEntity());
        // 3. 응답 DTO로 변환해서 변환 일을 시킴
        return new UserResponse.JoinDTO(savedUser);
    }

    // 로그인 처리
    public String login(UserRequest.LoginDTO loginDTO) {
        // 회원 정보 조회
        User selectedUser = userJpaRepository
                .findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> {
                    throw new Exception401("사용자명 또는 비밀번호가 틀렸어요");
                });
        // JWT 발급해서 Controller 단으로 넘겨 주면 된다.
        String jwt = JwtUtil.create(selectedUser);
        return jwt;
    }

    // 회원 정보 조회
    public UserResponse.DetailDTO findUserById(Long requestUserId, Long sessionUserId) {

        // 권한 검사
        if (!requestUserId.equals(sessionUserId)) {
            throw new Exception403("본인 정보만 조회 가능합니다");
        }
        // 정보 조회
        User selectedUser = userJpaRepository.findById(requestUserId).orElseThrow(() -> {
            throw new Exception404("사용자를 찾을 수 없습니다");
        });
        // 응답 DTO 변환 처리
        return new UserResponse.DetailDTO(selectedUser);
    }

    // 회원 정보 수정 처리
    @Transactional
    public UserResponse.UpdateDTO updateById(Long requestUserId,
                                             Long sessionUserId,
                                             UserRequest.UpdateDTO updateDTO) {
        // 1. 권한 체크
        if (!requestUserId.equals(sessionUserId)) {
            throw new Exception403("본인 정보만 조회 가능합니다");
        }
        // 정보 조회 1차 캐시에 영속화 시키기
        User selectedUser = userJpaRepository.findById(requestUserId).orElseThrow(() -> {
            throw new Exception404("사용자를 찾을 수 없습니다");
        });
        // 2. 더티 체킹을 통한 회원 정보 수정
        selectedUser.update(updateDTO);

        // 3. 응답 DTO 반환
        return new UserResponse.UpdateDTO(selectedUser);
    }
}
