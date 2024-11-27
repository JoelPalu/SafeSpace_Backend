package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Comment;

/**
 * Data Transfer Object (DTO) for transferring comment-related data.
 * This class is used to represent a comment's data in a simplified form, including details such as the comment's ID,
 * the user who posted it, and the content of the comment.
 */
public class CommentDTO {

    private int commentID;
    private int userID;
    private String username;
    private String commentContent;

    /**
     * Constructs a CommentDTO from a Comment entity.
     * The constructor initializes the DTO's fields based on the provided Comment entity.
     *
     * @param comment the Comment entity from which data will be extracted
     */
    public CommentDTO(Comment comment) {
        this.commentID = comment.getCommentID();
        this.userID = comment.getUser().getUserID();
        this.username = comment.getUser().getUsername();
        this.commentContent = comment.getCommentContent();
    }

    /**
     * Gets the ID of the comment.
     *
     * @return the comment's ID
     */
    public int getCommentID() {
        return commentID;
    }

    /**
     * Sets the ID of the comment.
     *
     * @param commentID the comment's ID to set
     */
    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    /**
     * Gets the ID of the user who posted the comment.
     *
     * @return the user ID of the comment's author
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID of the comment's author.
     *
     * @param userID the user ID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the content of the comment.
     *
     * @return the content of the comment
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * Sets the content of the comment.
     *
     * @param commentContent the content to set for the comment
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**
     * Gets the username of the user who posted the comment.
     *
     * @return the username of the comment's author
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user who posted the comment.
     *
     * @param username the username to set for the comment's author
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
