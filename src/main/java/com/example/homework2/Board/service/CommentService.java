package com.example.homework2.Board.service;

import com.example.homework2.Board.dto.*;
import com.example.homework2.Board.entity.Board;
import com.example.homework2.Board.entity.Comment;
import com.example.homework2.Board.entity.ErrorCode.ErrorCode;
import com.example.homework2.Board.entity.ErrorCode.UserRoleEnum;
import com.example.homework2.Board.entity.User;
import com.example.homework2.Board.exception.ApiException;
import com.example.homework2.Board.repository.BoardRepository;
import com.example.homework2.Board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    public ResponseEntity<CommentResponseDto> create(Long id, CommentRequestDto requestDto, User user) {

//        선택한 게시글 DB 조회
        Optional<Board> board = boardRepository.findById(id);
        if(board.isEmpty()){
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없음");
        }

//        게시글이 있다면 댓글 등록
        Comment comment = commentRepository.save(Comment.Comment_Service(requestDto, board.get(),user));

//        comment 를 commentResponseDto로 변환 후, ResponseEntity body에 dto 담아 반환
        return ResponseEntity.ok(CommentResponseDto.comment_Service(comment));
    }

    public ResponseEntity<CommentResponseDto> updatePost(Long id, User user, CommentRequestDto commentRequestDto) {
        //        선택한 게시글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()){
            throw new ApiException(ErrorCode.NOT_FOUND_WRITING);
        }

//        선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 게시글 수정 가능)
        Optional<Comment> found = commentRepository.findByIdAndUser(id, user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER){         //  일치하는 게시물이 없다면
            throw new ApiException(ErrorCode.NOT_WRITER);
        }

//        게시글 id와 사용자 정보 일치한다면, 게시글 수정
        comment.get().update(commentRequestDto, user);
        boardRepository.flush();                        //  response 에 modifiedAt 업데이트 해주기 위해 flush 사용

        return ResponseEntity.ok(CommentResponseDto.comment_Service(comment.get()));
    }

    public ResponseEntity<MegResponseDto> deletePost(Long id, User user) {
        Optional<Board> found = boardRepository.findById(id);
        if(found.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND_WRITING);
        }

//            선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 ( 삭제하려는 사용자가 관리자라면 게시글 삭제 가능)
        Optional<Board> board = boardRepository.findByIdAndUser(id,user);
        if(board.isEmpty() && user.getRole() == UserRoleEnum.USER) {     //  일치하는 게시물이 없다면
            throw new ApiException(ErrorCode.NOT_WRITER);
        }

//        관리자이거나, 댓길의 작성자와 삭제하려는 사용자의 정보가 일치한다면, 댓글 삭제
        commentRepository.deleteById(id);


        return ResponseEntity.ok(MegResponseDto.User_ServiceCode(HttpStatus.OK,"게시글 작성 완료"));
    }
}
