package br.com.matheushbmelo.todolist.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.matheushbmelo.todolist.models.UserModel;
import br.com.matheushbmelo.todolist.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    @PostMapping("")
    public ResponseEntity<Object> createUser(@RequestBody UserModel userModel){
        var user = userRepository.findByUsername(userModel.getUsername());
        if (user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um usuário com o mesmo username");
        }
        userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

}
