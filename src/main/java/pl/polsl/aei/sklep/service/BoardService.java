package pl.polsl.aei.sklep.service;

import pl.polsl.aei.sklep.dto.PostDTO;
import pl.polsl.aei.sklep.dto.ThreadDTO;
import pl.polsl.aei.sklep.dto.ThreadWithPostsDTO;
import pl.polsl.aei.sklep.exception.ThreadAlreadyExist;

import java.util.List;

public interface BoardService {
    Long addThread(String username, ThreadDTO threadDTO) throws ThreadAlreadyExist;
    List<ThreadDTO> getAllThreads();
    ThreadWithPostsDTO getPostForThread(Long threadId);
    void addPostToThread(String username, PostDTO postDTO);
}
