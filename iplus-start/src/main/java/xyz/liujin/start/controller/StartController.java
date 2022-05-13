package xyz.liujin.start.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("start")
public class StartController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("hello/{word}")
    public String hello(@PathVariable(name = "word") String word) {
        return "hello " + word;
    }

    @GetMapping("page")
    public String[] page(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        return new String[] {"pageNo" + pageNo, "pageSize" + pageSize};
    }
}
