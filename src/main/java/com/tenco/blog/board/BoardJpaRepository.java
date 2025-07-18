package com.tenco.blog.board;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 게시글 관련 데이터베이스 접근을 담당
 * 기본적인 CRUD 제공
 */

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    // (게시글 목록 - 페이징 처리)
    @Query("SELECT b FROM Board b JOIN FETCH b.user u ORDER BY b.id DESC")
    Page<Board> findAllJoinUser(Pageable pageable);
    // List<Board> findAllJoinUser();
    // JOIN FETCH 는 모든 Board 엔티티와 연관괸 User 를 한방 쿼리로 가져 옴
    // LAZY 전략이라서 N + 1 방지를 할 수 있다
    // : 게시글 10개가 있다면 지연 로딩 1(Board 조회) + 10(User 조회) = 11번 쿼리가 발생

    // 게시글 상세보기
    @Query("SELECT b FROM Board b JOIN FETCH b.user u WHERE b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id") Long id);
}
