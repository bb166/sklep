package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.PostDTO;
import pl.polsl.aei.sklep.dto.ThreadDTO;
import pl.polsl.aei.sklep.dto.ThreadWithPostsDTO;
import pl.polsl.aei.sklep.exception.ThreadAlreadyExist;
import pl.polsl.aei.sklep.repository.ThreadRepository;
import pl.polsl.aei.sklep.repository.UserRepository;
import pl.polsl.aei.sklep.repository.entity.Post;
import pl.polsl.aei.sklep.repository.entity.Thread;
import pl.polsl.aei.sklep.repository.entity.User;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BoardServiceImpl implements BoardService{

    private ThreadRepository threadRepository;

    private UserRepository userRepository;

    private ThreadLocal<DateFormat> formatThreadLocal;

    @Autowired
    public void setFormatThreadLocal(ThreadLocal<DateFormat> formatThreadLocal) {
        this.formatThreadLocal = formatThreadLocal;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setThreadRepository(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    @Override
    public Long addThread(String username, ThreadDTO threadDTO) throws ThreadAlreadyExist {
        Thread threa = threadRepository.getThreadByName(threadDTO.getTopic());

        if (threa != null)
            throw new ThreadAlreadyExist();

        User user = userRepository.findUserByUsername(username);

        Thread thread = new Thread();
        thread.setUser(user);
        thread.setName(threadDTO.getTopic());
        thread.setCreateDate(new Date());

        Post post = new Post();
        post.setContent(threadDTO.getContent());
        post.setCreateDate(new Date());
        post.setUser(user);
        post.setThread(thread);

        thread.setPosts(Collections.singleton(post));

        threadRepository.save(thread);

        return thread.getId();
    }

    @Override
    public List<ThreadDTO> getAllThreads() {
        return StreamSupport.stream(threadRepository.findAll().spliterator(), false)
                        .map(this::threadEntityToDTOMapper).collect(Collectors.toList());
    }

    private ThreadDTO threadEntityToDTOMapper(Thread thread) {
        ThreadDTO threadDTO = new ThreadDTO();
        threadDTO.setId(thread.getId().toString());
        threadDTO.setAuthor(thread.getUser().getUsername());
        threadDTO.setTopic(thread.getName());
        threadDTO.setDate(formatThreadLocal.get().format(thread.getCreateDate()));
        threadDTO.setTopic(thread.getName());

        thread.getPosts().stream()
                .max((a, b) -> a.getCreateDate().equals(b.getCreateDate()) ? 0 : a.getCreateDate().before(b.getCreateDate()) ? -1 : 1)
                .ifPresent(e -> threadDTO.setContent(e.getContent()));

        return threadDTO;
    }

    @Override
    public ThreadWithPostsDTO getPostForThread(Long threadId) {
        List<PostDTO> postDTOS = threadRepository
                .findById(threadId)
                .map(Thread::getPosts)
                .map(e -> e
                        .stream()
                        .sorted((a, b) -> a.getCreateDate().equals(b.getCreateDate()) ? 0 : a.getCreateDate().before(b.getCreateDate()) ? -1 : 1)
                        .collect(Collectors.toList())
                )
                .map(e -> e.stream().map(this::postEntityToDTOMapper).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return new ThreadWithPostsDTO(threadId.toString(), postDTOS);
    }

    @Override
    public void addPostToThread(String username, PostDTO postDTO) {
        threadRepository.findById(Long.parseLong(postDTO.getThreadId()))
                .ifPresent(e -> {
                    Post post = new Post();
                    post.setThread(e);
                    User user = userRepository.findUserByUsername(username);
                    post.setUser(user);
                    post.setCreateDate(new Date());
                    post.setContent(postDTO.getContent());

                    e.getPosts().add(post);
                    threadRepository.save(e);
                });
    }

    private PostDTO postEntityToDTOMapper(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setAuthor(post.getUser().getUsername());
        postDTO.setContent(post.getContent());
        return postDTO;
    }
}