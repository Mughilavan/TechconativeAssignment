package com.techconative.demo.service;

import com.techconative.demo.entity.Post;
import com.techconative.demo.repository.PostRepository;
import com.techconative.demo.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }


    public Post getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.orElse(null);
    }

    public Post updatePost(Long postId, Post updatedPost) {
        Post existingPost = getPostById(postId);

        if (existingPost != null) {
            existingPost.setContent(updatedPost.getContent());
            existingPost.setUpdatedAt(LocalDateTime.now());
            return postRepository.save(existingPost);
        }
        return null;
    }

    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public boolean isValidPostId(Long postId) {
        return postRepository.findById(postId).isPresent();
    }
}
