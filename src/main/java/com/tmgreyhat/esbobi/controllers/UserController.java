package com.tmgreyhat.esbobi.controllers;




import com.tmgreyhat.esbobi.repositories.UserRepository;
import com.tmgreyhat.esbobi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {




    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    private String  getAllUser(Model model){

        model.addAttribute("users", repository.findAll());

        return  "allusers";

    }

    @GetMapping("/users/{id}")
    private String findOne(@PathVariable("id") Long id, Model model){

        model.addAttribute("user", repository.findById(id));


        return "userdetails";
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private String saveUser(@RequestBody User user, Model model){

       // return  repository.save(user);
        repository.save(user);

        model.addAttribute("users", repository.findAll());


        return  "allusers";
    }


    @GetMapping("/add-user")
    private String userForm(){

        return "adduser";
    }


/*
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private User update(@PathVariable("id") Long id , @RequestBody User user){


        return  repository.findById(id)
                .map(user1 -> {


                    user1.setEmail(user.getEmail());
                    user1.setFirst_name(user.getFirst_name());
                    user1.setLast_name(user.getLast_name());
                    user1.setPassword(user.getPassword());
                    return  repository.save(user);
                })
                .orElseThrow(()->new ResourceNotFoundException(" user ",id));




    }*/

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private String deleteONe(@PathVariable(name = "id") Long id, Model model){

        repository.findById(id);

        model.addAttribute("users", repository.findAll());
        return "allusers";

    }






}
