package com.example.demo.model;

public class GuideLanguages {
    private Integer guide_id;
    private String language;

    public GuideLanguages() {}

    public GuideLanguages(Integer guide_id, String language) {
        this.guide_id = guide_id;
        this.language = language;
    }

    public Integer getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(Integer guide_id) {
        this.guide_id = guide_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Guide_Languages{" +
                "guide_id=" + guide_id +
                ", language='" + language + '\'' +
                '}';
    }
}
