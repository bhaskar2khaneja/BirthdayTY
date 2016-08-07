package com.projects;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;
import com.restfb.Connection;

import java.util.*;

public class BirthdayManager {

    private FacebookClient facebookClient;
    private User me;
    private int date;
    private int month;
    private int currentYear;

    public BirthdayManager(String accessToken, int date, int month, int currentYear) {
        facebookClient = new DefaultFacebookClient(accessToken);
        me = facebookClient.fetchObject("me", User.class);
        this.date = date;
        this.month = month;
        this.currentYear = currentYear;
    }

    private void commentOn(Post post) {
        facebookClient.publish(post.getId() + "/likes", Boolean.class);
        facebookClient.publish(post.getId() + "/comments", String.class, Parameter.with("message", "Thanks man! :)"));
    }

    public void respondToBirthdayPosts() {
        Date start = new Date(year - 1900, month, date);
        Date end = new Date(start.getTime() + (1000 * 60 * 60 * 24));
        Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);
        for (List<Post> feed: myFeed) {
            for (Post post: feed) {
                if (post.getFrom().getId() == me.getId()) {
                    continue;
                }
                Date dateCreated = post.getCreatedTime();
                System.out.println(created);
                if (dateCreated.after(start) && dateCreated.before(end)) {
                    Post post = facebookClient.fetchObject(post.getId(), Post.class, Parameter.with("fields", "from,to,likes.summary(true),comments.summary(true)"));
                    commentOn(post);
                }
            }
        }
    }

    public static void main(String[] args) {
        BirthdayManager manager = new BirthdayManager(args[0], args[1], args[2], args[3]);
        manager.respondToBirthdayPosts();
    }

}