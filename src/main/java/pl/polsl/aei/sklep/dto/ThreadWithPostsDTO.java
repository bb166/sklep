package pl.polsl.aei.sklep.dto;

import java.util.List;

public class ThreadWithPostsDTO {
    private String id;
    private List<PostDTO> postList;

    public ThreadWithPostsDTO(String id, List<PostDTO> postList) {
        this.id = id;
        this.postList = postList;
    }

    public List<PostDTO> getPostList() {
        return postList;
    }

    public void setPostList(List<PostDTO> postList) {
        this.postList = postList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
