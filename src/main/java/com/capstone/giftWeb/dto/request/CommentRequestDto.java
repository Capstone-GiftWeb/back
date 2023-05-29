package com.capstone.giftWeb.dto.request;

import com.capstone.giftWeb.domain.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {
    private Long memberId;
    private Long parentId;
    private String content;

    public CommentRequestDto(String content){
        this.content=content;
    }
}
