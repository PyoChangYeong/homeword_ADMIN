package com.example.homework2.Board.service;


import com.example.homework2.Board.dto.BoardRequestDto;
import com.example.homework2.Board.dto.BoardResponseDto;
import com.example.homework2.Board.dto.MegResponseDto;
import com.example.homework2.Board.entity.Board;
import com.example.homework2.Board.entity.Comment;
import com.example.homework2.Board.entity.ErrorCode.ErrorCode;
import com.example.homework2.Board.entity.ErrorCode.UserRoleEnum;
import com.example.homework2.Board.entity.User;
import com.example.homework2.Board.exception.ApiException;
import com.example.homework2.Board.jwt.JwtUtil;
import com.example.homework2.Board.repository.BoardRepository;
import com.example.homework2.Board.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    //          게시글 전체 목록 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getBoard() {
        List<Board> boards = getBoardRepository().findAllByOrderByModifiedAtDesc();
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();
        for (Board bo : boards) {
//            댓글리스트 작성일자 기준 내림차순 정렬
            bo.getCommentlist()
                    .sort(Comparator.comparing(Comment::getModifiedAt)
                            .reversed());

            // List<BoardResponseDto> 로 만들기 위해 board 를 BoardResponseDto 로 만들고, list 에 dto 를 하나씩 넣는다.
            boardResponseDtos.add(BoardResponseDto.User_Response(bo));

//             id값 1로 다시 변경 해보기
        }
        return ResponseEntity.ok(boardResponseDtos);
    }

    //            게시글 작성
    @Transactional
    public ResponseEntity<?> createPost(BoardRequestDto requestDto,User user) {
        Board board = Board.builder()
                .boardRequestDto(requestDto)
                .user(user)
                .build();
//          요청 받은 DTO 로 DB에 저장할 객체 만듬
        boardRepository.save(board);
        return ResponseEntity.ok().body(BoardResponseDto.builder().entity(board).build());

//        작성 글 저장
//        Board board = boardRepository.save(Board.User_Service(requestDto, user));


//        Board ResponseDto로 변환 후 responseEntity body에 담아 반환
//        return ResponseEntity.ok(BoardResponseDto.User_Response(board));
    }

    //      선택한 게시글 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getPost(Long id) {
        return new BoardResponseDto(getElseThrow(id));
    }


    //      아이디 예외 처리
    private Board getElseThrow(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
    }

    @Transactional
    public ResponseEntity<BoardResponseDto> updatePost(Long id, User user, BoardRequestDto boardRequestDto) {

//        선택한 게시글이 DB에 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if(board.isEmpty()){
            throw new ApiException(ErrorCode.NOT_FOUND_WRITING);
        }

//        선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 게시글 수정 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER){         //  일치하는 게시물이 없다면
            throw new ApiException(ErrorCode.NOT_WRITER);
        }

//        게시글 id와 사용자 정보 일치한다면, 게시글 수정
        board.get().update(boardRequestDto, user);
        boardRepository.flush();                        //  response 에 modifiedAt 업데이트 해주기 위해 flush 사용

        return ResponseEntity.ok(BoardResponseDto.User_Response(board.get()));
    }


    @Transactional
    public ResponseEntity<MegResponseDto> deletePost(Long id, User user) {

//              선택한 게시글이 DB에 있는지 확인
        Optional<Board> found = boardRepository.findById(id);
            if(found.isEmpty()) {
                throw new ApiException(ErrorCode.NOT_FOUND_WRITING);
            }

//            선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 ( 삭제하려는 사용자가 관리자라면 게시글 삭제 가능)
        Optional<Board> board = boardRepository.findByIdAndUser(id,user);
            if(board.isEmpty() && user.getRole() == UserRoleEnum.USER) {     //  일치하는 게시물이 없다면
                throw new ApiException(ErrorCode.NOT_WRITER);
            }

//            게시글 id와 사용자 정보 일치한다면, 게시글 수정
           boardRepository.deleteById(id);
            return ResponseEntity.ok(MegResponseDto.User_ServiceCode(HttpStatus.OK,"게시글 작성 완료"));

        }

}