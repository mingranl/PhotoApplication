package com.example.misaka.photoapplication.Model;

public class FeedItem {
    private String feed_id;
    private String username;
    private String img;
    private String description;
    private int like_count;
    boolean current_like=false;

    public FeedItem(String feed_id, String username, String img, String description, int like_count, boolean current_like) {
        this.feed_id = feed_id;
        this.username = username;
        this.img = img;
        this.description = description;
        this.like_count = like_count;
        this.current_like=current_like;
    }

    public FeedItem() {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public boolean isCurrent_like() {
        return current_like;
    }

    public void setCurrent_like(boolean currentUserlike) {
        this.current_like = currentUserlike;
    }

    @Override
    public String toString() {
        return "FeedItem{" +
                "feed_id='" + feed_id + '\'' +
                ", username='" + username + '\'' +
                ", img='" + img + '\'' +
                ", description='" + description + '\'' +
                ", like_count=" + like_count +
                ", current_like=" + current_like +
                '}';
    }
}
