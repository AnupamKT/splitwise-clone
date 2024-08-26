package com.anupam.Splitwise.controller.user;

import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.member.User;
import com.anupam.Splitwise.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("splitwise/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) throws Exception {
        Response response = userService.createUser(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity getUser(@PathVariable String email) throws Exception {
        Response response = userService.getUser(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
