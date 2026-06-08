package com.pakn.dev;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Pages {
    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }
}
