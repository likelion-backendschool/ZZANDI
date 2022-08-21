package com.ll.zzandi.service;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Long save(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }


}
