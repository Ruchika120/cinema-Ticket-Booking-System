package com.cinema.backend.controllers;

import com.cinema.backend.dto.LoginResponseDTO;
import com.cinema.backend.models.User;
import com.cinema.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    //since it has the post request it will register the user and all the information that is entered by the user 
    //during Registration is get stored in newuser as we have used @RequestBody
    
    @PostMapping("/api/v1/register")
    User newUser(@RequestBody User newUser) {
        return userService.registerUser(newUser);
    }

    @PostMapping("/api/v1/login")
    //The method takes a User object as input, 
    //which is bound from the body of the HTTP request. 
    //This object contains the login credentials (email and password).
    //The <LoginResponseDTO> part indicates that ResponseEntity will contain a body of type LoginResponseDTO.
   // LoginResponseDTO is a Data Transfer Object (DTO) used to encapsulate the response data sent to the client.
    //In this case, it likely contains information related to the login operation, such as a message, the user's name, and the user's ID.
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody User loginRequest) {
        User user = userService.getUserByEmail(loginRequest.getEmail());
//status(HttpStatus.UNAUTHORIZED): A method that sets the HTTP status code for the response.
        //In this case, HttpStatus.UNAUTHORIZED represents the HTTP status code 401.
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO("Invalid credentials", null, null));
        }

        if (userService.isPasswordMatch(loginRequest.getPassword(), user.getPassword())) {
            LoginResponseDTO response = new LoginResponseDTO("Login successful", user.getName(), user.getId());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO("Invalid credentials", null, null));
        }
    }
}
//ResponseEntity is a class in the Spring Framework that represents the entire HTTP response,
//including status code, headers, and body. 
//It provides a powerful and flexible way to control how HTTP responses are constructed and 
//sent back to clients in a Spring application.


//loginRequest.getPassword()
//Purpose: This method retrieves the password that the user has submitted in their login request
//user.getPassword()
//Purpose: This method retrieves the password that is stored in the database for the user.