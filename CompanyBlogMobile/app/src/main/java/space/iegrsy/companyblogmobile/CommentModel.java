package space.iegrsy.companyblogmobile;

import java.io.Serializable;

public class CommentModel implements Serializable {
    int commentid;
    String username;
    String comment;
    String date;

    public CommentModel() {
    }

    public CommentModel(int commentid, String username, String comment, String date) {
        this.commentid = commentid;
        this.username = username;
        this.comment = comment;
        this.date = date;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "commentid=" + commentid +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
