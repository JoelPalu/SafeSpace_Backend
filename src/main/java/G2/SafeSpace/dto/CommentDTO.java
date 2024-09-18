package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Comment;

public class CommentDTO {
    private int commentID;
    private int userID;
    private String username;
    private String commentContent;

    public CommentDTO(Comment comment) {
        this.commentID = comment.getCommentID();
        this.userID = comment.getUser().getUserID();
        this.username = comment.getUser().getUsername();
        this.commentContent = comment.getCommentContent();
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
