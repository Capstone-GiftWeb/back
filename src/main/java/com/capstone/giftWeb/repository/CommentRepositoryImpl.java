package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Comment;
import com.capstone.giftWeb.dto.response.CommentResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static com.capstone.giftWeb.domain.QComment.comment;
import static com.capstone.giftWeb.dto.response.CommentResponseDto.convertCommentToDto;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentResponseDto> findByGiftId(Long id) {
        List<Comment> comments = jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.parent).fetchJoin()
                .where(comment.gift.id.eq(id))
                .orderBy(comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc())
                .fetch();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Map<Long, CommentResponseDto> commentDtoHashMap = new HashMap<>();

        comments.forEach(c -> {
            CommentResponseDto commentResponseDto = convertCommentToDto(c);
            commentDtoHashMap.put(commentResponseDto.getId(), commentResponseDto);
            if (c.getParent() != null) commentDtoHashMap.get(c.getParent().getId()).getChildren().add(commentResponseDto);
            else commentResponseDtoList.add(commentResponseDto);
        });
        return commentResponseDtoList;
    }

    @Override
    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        Comment comment=em.find(Comment.class,id);
        return Optional.ofNullable(comment);
    }
}
