package com.example.saurabhkumar.downz.CustomClasses;

import java.util.List;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class User {

    String Name;
    String ProfilePicUrl;
    String UploadedImageUrl;
    boolean isLikedByUser;
    String UserName;
    int NumberOfLikes;
    List<String> Categories;
    String UrlToSend;

    public User() {
        /* empty constructor */
    }

    public User(String name, String profilePicUrl, String uploadedImageUrl, boolean isLikedByUser, String userName, int numberOfLikes, List<String> categories, String UrltoSend) {
        Name = name;
        ProfilePicUrl = profilePicUrl;
        UploadedImageUrl = uploadedImageUrl;
        this.isLikedByUser = isLikedByUser;
        UserName = userName;
        NumberOfLikes = numberOfLikes;
        Categories = categories;
        UrlToSend = UrltoSend;

    }

    public String getUrlToSend() {
        return UrlToSend;
    }

    public void setUrlToSend(String urlToSend) {
        UrlToSend = urlToSend;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }

    public String getUploadedImageUrl() {
        return UploadedImageUrl;
    }

    public void setUploadedImageUrl(String uploadedImageUrl) {
        UploadedImageUrl = uploadedImageUrl;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getNumberOfLikes() {
        return NumberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        NumberOfLikes = numberOfLikes;
    }

    public List<String> getCategories() {
        return Categories;
    }

    public void setCategories(List<String> categories) {
        Categories = categories;
    }
}
