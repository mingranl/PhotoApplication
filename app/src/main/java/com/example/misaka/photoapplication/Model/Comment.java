package com.example.misaka.photoapplication.Model;

public class Comment {
    private String comment_id;
    private String feed_id;
    private String username;
    private String comment;


    public Comment(String comment_id, String feed_id, String username, String comment) {
        this.comment_id = comment_id;
        this.feed_id = feed_id;
        this.username = username;
        this.comment = comment;
    }

    public Comment() {
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id='" + comment_id + '\'' +
                ", feed_id='" + feed_id + '\'' +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
