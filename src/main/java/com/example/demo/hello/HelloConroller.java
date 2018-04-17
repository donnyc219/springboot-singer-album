package com.example.demo.hello;

import com.example.demo.helper.DatabaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloConroller {

    @Autowired
    DatabaseHelper dbHelper;

    @RequestMapping("/")
    public String index(){
        return "Greetings from Spring Boot?!?!?";
    }

    @RequestMapping("/newUser/{name}")
    public String newUser(@PathVariable("name") String name){
        dbHelper.addNewUser(name);
        return name;
    }

}
