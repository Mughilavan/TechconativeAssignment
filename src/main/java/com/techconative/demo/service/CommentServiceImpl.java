package com.techconative.demo.service;

import com.techconative.demo.entity.Comment;
import com.techconative.demo.repository.CommentRepository;
import com.techconative.demo.serviceinterface.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

}
