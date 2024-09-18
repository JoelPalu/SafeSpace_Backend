package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDTO {
    private int postID;
    private int postCreatorID;
    private String postContent;
    private String postPictureID;
    private String postDate;
    private int likeCount;

    private List<Integer> likers = new ArrayList<>();

    public PostDTO(Post post) {
        this.postID = post.getPostID();
        this.postContent = post.getPost_content();
        this.postPictureID = post.getPost_pictureID();
        this.postDate = post.getPost_date();
    }


    public int getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(int postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostPictureID() {
        return postPictureID;
    }

    public void setPostPictureID(String postPictureID) {
        this.postPictureID = postPictureID;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
