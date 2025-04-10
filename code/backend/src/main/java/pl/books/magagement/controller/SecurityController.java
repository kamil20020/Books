package pl.books.magagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/security")
public class SecurityController {

    @GetMapping(path = "/public")
    public String testPublic(){
        return "public";
    }

    @GetMapping(path = "/requires-login")
    public String testLogin(){
        return "login";
    }

    @GetMapping(path = "/requires-admin")
    public String testAdmin(){
        return "admin";
    }
}
