package es.codeurjc.cosmic_news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SPAController {
    
    @GetMapping({ "/new/**/{path:[^\\.]*}", "/{path:new[^\\.]*}","/new/" })
        public String redirect() {
        return "forward:/new/index.html";
    }
}
