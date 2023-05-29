package com.capstone.giftWeb.dto.response;

import com.capstone.giftWeb.domain.Comment;
import com.capstone.giftWeb.dto.MemberDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {

    private Long id;
    private String content;
    private MemberDto writer;
    private List<CommentResponseDto> children = new ArrayList<>();

    public CommentResponseDto(Long id, String content, MemberDto writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }

    public static CommentResponseDto convertCommentToDto(Comment comment) {
        return comment.getIsDeleted() ?
                new CommentResponseDto(comment.getId(), "삭제된 댓글입니다.", null) :
                new CommentResponseDto(comment.getId(), comment.getContent(), new MemberDto(comment.getWriter()));
    }
}