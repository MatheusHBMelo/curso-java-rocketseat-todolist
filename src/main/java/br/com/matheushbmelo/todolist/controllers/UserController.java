package br.com.matheushbmelo.todolist.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.matheushbmelo.todolist.models.UserModel;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @PostMapping("")
    public void createUser(@RequestBody UserModel userModel){
        System.out.println(userModel.getName());
    }

}
