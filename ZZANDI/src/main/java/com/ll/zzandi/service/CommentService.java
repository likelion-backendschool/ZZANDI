package com.ll.zzandi.service;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.dto.CommentDto;
import com.ll.zzandi.dto.CommentDto.Response;
import com.ll.zzandi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Response> commentList(Long boardId) {
        List<Comment> list = commentRepository.findCommentByBoardId(boardId);
        List<Response> responseList = new ArrayList<>();
        for (Comment comment : list) {
            responseList.add(new Response(comment.getId(), comment.getBoard().getId(), comment.getUser().getId(), comment.getUser().getUserNickname(),
                    comment.getParentId(), comment.getContent(), comment.getDeleteStatus(), comment.getCreatedDate()));
        }
        return responseList;
    }


}
