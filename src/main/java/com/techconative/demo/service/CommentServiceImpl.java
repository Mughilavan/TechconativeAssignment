package com.techconative.demo.service;

import com.techconative.demo.entity.Comment;
import com.techconative.demo.repository.CommentRepository;
import com.techconative.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

}
