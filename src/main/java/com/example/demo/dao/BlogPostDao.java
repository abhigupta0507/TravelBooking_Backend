package com.example.demo.dao;

import com.example.demo.dto.BlogPostDto;
import com.example.demo.model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class BlogPostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Insert new blog post
    public Integer createBlog(BlogPostDto dto, Integer staffId) {
        String sql = "INSERT INTO BlogPost (title, content, published_at, last_updated, featured_image, staff_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getContent());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(3, now); // published_at
            ps.setTimestamp(4, now); // last_updated
            ps.setString(5, dto.getFeaturedImage());
            ps.setInt(6, staffId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // Get blog post by ID
    public BlogPost getBlogById(Integer blogId) {
        String sql = "SELECT * FROM BlogPost WHERE blog_id = ?";
        return jdbcTemplate.queryForObject(sql, new BlogPostRowMapper(), blogId);
    }

    // Get all blog posts
    public List<BlogPost> getAllBlogs() {
        String sql = "SELECT * FROM BlogPost ORDER BY published_at DESC";
        return jdbcTemplate.query(sql, new BlogPostRowMapper());
    }

    // Optional: Update blog post
    public int updateBlog(Integer blogId, BlogPostDto dto) {
        String sql = "UPDATE BlogPost SET title = ?, content = ?, last_updated = ?, featured_image = ? WHERE blog_id = ?";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return jdbcTemplate.update(sql, dto.getTitle(), dto.getContent(), now, dto.getFeaturedImage(), blogId);
    }

    // Optional: Delete blog post
    public int deleteBlog(Integer blogId) {
        String sql = "DELETE FROM BlogPost WHERE blog_id = ?";
        return jdbcTemplate.update(sql, blogId);
    }

    // RowMapper for BlogPost
    private static class BlogPostRowMapper implements RowMapper<BlogPost> {
        @Override
        public BlogPost mapRow(ResultSet rs, int rowNum) throws SQLException {
            BlogPost blog = new BlogPost();
            blog.setBlog_id(rs.getInt("blog_id"));
            blog.setTitle(rs.getString("title"));
            blog.setContent(rs.getString("content"));
            blog.setPublished_at(rs.getTimestamp("published_at"));
            blog.setLast_updated(rs.getTimestamp("last_updated"));
            blog.setFeatured_image(rs.getString("featured_image"));
            blog.setStaff_id(rs.getInt("staff_id"));
            return blog;
        }
    }
}
