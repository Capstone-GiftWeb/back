package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Comment;
import com.capstone.giftWeb.dto.response.CommentResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<CommentResponseDto> findByGiftId(Long id);

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

}


