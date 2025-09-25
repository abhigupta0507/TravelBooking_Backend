package com.example.demo.dto;

public class BlogPostDto {
    private int blog_id;
    private String title;
    private String content;
    private String featuredImage;

    // Optional: if you want to allow client to set published_at
    // private Timestamp publishedAt;

    public BlogPostDto() {}

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public BlogPostDto(String title, String content, String featuredImage) {
        this.title = title;
        this.content = content;
        this.featuredImage = featuredImage;
    }



    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }


    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFeaturedImage() { return featuredImage; }

    @Override
    public String toString() {
        return "BlogPostDto{" +
                "blog_id=" + blog_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", featuredImage='" + featuredImage + '\'' +
                '}';
    }

    public void setFeaturedImage(String featuredImage) { this.featuredImage = featuredImage; }

}
