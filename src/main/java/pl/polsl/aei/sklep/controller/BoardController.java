package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.polsl.aei.sklep.dto.PostDTO;
import pl.polsl.aei.sklep.dto.ThreadDTO;
import pl.polsl.aei.sklep.exception.ThreadAlreadyExist;
import pl.polsl.aei.sklep.service.BoardService;

import java.security.Principal;

@Controller
public class BoardController {

    private BoardService boardService;

    @Autowired
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping(value = "/addThread", method = RequestMethod.POST)
    public ModelAndView addThread(Principal principal, ThreadDTO threadDTO) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            Long id = boardService.addThread(principal.getName(), threadDTO);
            modelAndView.setViewName("redirect:/showThread/" + id);
        } catch (ThreadAlreadyExist ex) {
            modelAndView.addObject("threadAlreadyExist", "");
            modelAndView.setViewName("addThread");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/addPost", method = RequestMethod.POST)
    public ModelAndView addPost(Principal principal, PostDTO postDTO) {
        boardService.addPostToThread(principal.getName(), postDTO);

        return new ModelAndView("redirect:/showThread/"+postDTO.getThreadId());
    }

    @RequestMapping("/addPost/{id}")
    public ModelAndView addPost(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("threadId", id);
        modelAndView.setViewName("addPost");
        return modelAndView;
    }

    @RequestMapping("/addThread")
    public ModelAndView addThread() {
        return new ModelAndView("addThread");
    }

    @RequestMapping("/showAllThreads")
    public ModelAndView showAllThreads() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("thread", boardService.getAllThreads());
        modelAndView.setViewName("threads");

        return modelAndView;
    }

    @RequestMapping("/showThread/{id}")
    public ModelAndView showThread(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("post", boardService.getPostForThread(id));
        modelAndView.setViewName("showThread");
        return modelAndView;
    }
}
