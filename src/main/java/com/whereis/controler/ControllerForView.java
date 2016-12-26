package com.whereis.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class ControllerForView {
    @RequestMapping(value = "/", produces = "text/html")
    public String getPostByIdHtml() throws IOException {
        return "resources/webapp/index.html";
    }
}
