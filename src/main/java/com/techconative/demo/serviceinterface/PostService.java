package com.techconative.demo.serviceinterface;

import com.techconative.demo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Post createPost(Post post);
    Page<Post> getAllPosts(Pageable pageable);
    Post getPostById(Long postId);
    Post updatePost(Long postId, Post updatedPost);
}
