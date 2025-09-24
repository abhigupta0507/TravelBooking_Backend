package com.example.demo.model;

import java.sql.Timestamp;

public class BlogPost {

    private Integer blog_id;
    private String title;
    private String content;
    private Timestamp published_at;
    private Timestamp last_updated;
    private String featured_image;
    private Integer staff_id;

    public BlogPost() {}

    public BlogPost(Integer blog_id, String title, String content, Timestamp published_at,
                    Timestamp last_updated, String featured_image, Integer staff_id) {
        this.blog_id = blog_id;
        this.title = title;
        this.content = content;
        this.published_at = published_at;
        this.last_updated = last_updated;
        this.featured_image = featured_image;
        this.staff_id = staff_id;
    }

    public Integer getBlog_id() { return blog_id; }
    public void setBlog_id(Integer blog_id) { this.blog_id = blog_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getPublished_at() { return published_at; }
    public void setPublished_at(Timestamp published_at) { this.published_at = published_at; }

    public Timestamp getLast_updated() { return last_updated; }
    public void setLast_updated(Timestamp last_updated) { this.last_updated = last_updated; }

    public String getFeatured_image() { return featured_image; }
    public void setFeatured_image(String featured_image) { this.featured_image = featured_image; }

    public Integer getStaff_id() { return staff_id; }
    public void setStaff_id(Integer staff_id) { this.staff_id = staff_id; }

    @Override
    public String toString() {
        return "BlogPost{" +
                "blog_id=" + blog_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", published_at=" + published_at +
                ", last_updated=" + last_updated +
                ", featured_image='" + featured_image + '\'' +
                ", staff_id=" + staff_id +
                '}';
    }
}
