package com.example.misaka.photoapplication.Model;

public class AccountInfo {

    private String user_id;
    private String user_name;
    private int posts;
    private int following;
    private int followers;

    public AccountInfo(String id, String name, int posts, int following, int followers){
        this.user_id = id;
        this.user_name = name;
        this.posts = posts;
        this.following = following;
        this.followers = followers;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

}
