package com.example.homework2.Board.repository;

import com.example.homework2.Board.entity.Comment;
import com.example.homework2.Board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
        Optional<Comment> findByIdAndUser(Long id, User user);
}
