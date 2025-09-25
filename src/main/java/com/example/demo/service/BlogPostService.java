package com.example.demo.service;

import com.example.demo.dao.BlogPostDao;
import com.example.demo.dto.BlogPostDto;
import com.example.demo.model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostDao blogPostDao;

    // Add a new blog post
    public Integer addBlog(BlogPostDto dto, Integer staffId) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new RuntimeException("Title is required");
        }
        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new RuntimeException("Content is required");
        }

        // Call DAO directly with dto and staffId
        return blogPostDao.createBlog(dto, staffId);
    }

    // Get blog by ID
    public BlogPost getBlogById(Integer blogId) {
        BlogPost blog = blogPostDao.getBlogById(blogId);
        if (blog == null) {
            throw new RuntimeException("Blog not found");
        }
        return blog;
    }

    // Get all blogs
    public List<BlogPost> getAllBlogs() {
        return blogPostDao.getAllBlogs();
    }

    // Update a blog post
    public int updateBlog(Integer blogId, BlogPostDto dto) {
        // Optional: you could check if blog exists first
        BlogPost existing = blogPostDao.getBlogById(blogId);
        if (existing == null) {
            throw new RuntimeException("Blog not found");
        }

        // Call DAO directly with dto
        return blogPostDao.updateBlog(blogId, dto);
    }

    // Delete a blog post
    public int deleteBlog(Integer blogId) {
        BlogPost existing = blogPostDao.getBlogById(blogId);
        if (existing == null) {
            throw new RuntimeException("Blog not found");
        }
        return blogPostDao.deleteBlog(blogId);
    }
}
