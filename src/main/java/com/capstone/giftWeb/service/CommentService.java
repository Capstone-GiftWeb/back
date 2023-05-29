package com.capstone.giftWeb.service;

import com.capstone.giftWeb.domain.Comment;
import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.request.CommentRequestDto;
import com.capstone.giftWeb.repository.CommentRepository;
import com.capstone.giftWeb.repository.GiftRepository;
import com.capstone.giftWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.yarn.webapp.NotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final GiftRepository giftRepository;
    @Autowired
    private final CommentRepository commentRepository;


    @Transactional
    public Comment insert(Long giftId, CommentRequestDto commentRequestDto) {

        Member member = memberRepository.findById(commentRequestDto.getMemberId())
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + commentRequestDto.getMemberId()));

        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new NotFoundException("Could not found board id : " + giftId));

        Comment comment = new Comment();

        if (commentRequestDto.getParentId() != null) {
           Comment parentComment = commentRepository.findById(commentRequestDto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Could not found comment id : " + commentRequestDto.getParentId()));
            comment.setParent(parentComment);
        }

        comment.setWriter(member);
        comment.setGift(gift);
        comment.setContent(commentRequestDto.getContent());

        return commentRepository.save(comment);

    }
}
