package com.example.misaka.photoapplication.Model;

import java.io.Serializable;

public class AccountInfo implements Serializable {

    private String user_id;
    private String username;
    private int posts;
    private int following;
    private int followers;

    public AccountInfo(String user_id, String username, int posts, int following, int followers){
        this.user_id = user_id;
        this.username = username;
        this.posts = posts;
        this.following = following;
        this.followers = followers;
    }

    public AccountInfo(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "AccountInfo{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", posts=" + posts +
                ", following=" + following +
                ", followers=" + followers +
                '}';
    }
}