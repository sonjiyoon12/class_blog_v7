package com.tenco.blog.board;

import com.tenco.blog.reply.Reply;
import com.tenco.blog.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    // 게시글 목록 응답 DTO
    @Data
    public static class MainDTO {
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        public MainDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    } // end of inner class

    // 게시글 상세보기 응답 DTO 설계
    @Data
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;
        private boolean isBoardOwner;
        private List<ReplyDTO> replies;

        public DetailDTO(Board board, User sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
            this.isBoardOwner = sessionUser != null && board.isOwner(sessionUser.getId());
            // [() () () ()]  <---- ReplyDTO. 객체 타입
            this.replies = new ArrayList<>();

            // board.getReplies() <---  List<Reply>

            // ----------------------------
            // 댓글 3 --> [reply1, reply2, reply3]
            // for - 1 (reply1)
            //
            // reply1 -->  new ReplyDTO(r, u) 변환해서 --> add [() ]
            //  변환해서 --> add [(ReplyDTO),  ]
            // for - 1 (reply1)
            //
            // reply2 -->  new ReplyDTO(r, u) 변환해서 --> add [() ]
            //  변환해서 --> add [(ReplyDTO),  ]
            // for - 2 (reply1)
            //
            // reply3 -->  new ReplyDTO(r, u) 변환해서 --> add [() ]
            //  변환해서 --> add [(ReplyDTO), (ReplyDTO),  (ReplyDTO), ]
            // DetailDTO
            // -- 생략
            //    [(ReplyDTO), (ReplyDTO),  (ReplyDTO)]  <--- replies
            for (Reply reply : board.getReplies()) {
                // 응답 DTO 변수 안에 값을 할당하는 코드
                this.replies.add(new ReplyDTO(reply, sessionUser));
            }
        }
    }

    // 댓글 정보 DTO
    @Data
    public static class ReplyDTO {
        private Long id;
        private String comment;
        private String writerName;
        private String createdAt;
        private boolean isReplyOwner;

        @Builder
        public ReplyDTO(Reply reply, User sessionUser) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.writerName = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt().toString();
            this.isReplyOwner = sessionUser != null && reply.isOwner(sessionUser.getId());
        }
    }

    // 게시글 작성 응답 DTO
    @Data
    public static class SaveDTO {
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public SaveDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }

    // 게시글 수정 응답 DTO
    @Data
    public static class UpdateDTO {
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public UpdateDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }
}
