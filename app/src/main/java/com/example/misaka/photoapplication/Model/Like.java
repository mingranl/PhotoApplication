package com.example.misaka.photoapplication.Model;

public class Like {
    private String like_id;
    private String feed_id;
    private String username;

    public Like(String like_id, String feed_id, String username) {
        this.like_id = like_id;
        this.feed_id = feed_id;
        this.username = username;
    }

    public Like() {
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLike_id() {
        return like_id;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Like{" +
                "like_id='" + like_id + '\'' +
                ", feed_id='" + feed_id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
