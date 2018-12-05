package space.iegrsy.companyblogmobile;

import java.io.Serializable;

public class PostModel implements Serializable {
    int postid;
    String user;
    String title;
    String body;
    String date;

    public PostModel() {
    }

    public PostModel(int postid, String user, String title, String body, String date) {
        this.postid = postid;
        this.user = user;
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "postid=" + postid +
                ", user='" + user + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                '}';
    }
}
