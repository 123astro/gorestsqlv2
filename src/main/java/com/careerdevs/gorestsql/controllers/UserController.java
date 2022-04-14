package com.careerdevs.gorestsql.controllers;
// all code in here

import com.careerdevs.gorestsql.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/user")

public class UserController {

    @GetMapping("/upload/{id}")
    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId, RestTemplate restTemplate
    ) {
        try {

            int uID = Integer.parseInt(userId);

            //check the range => other things to do

            String url = "https://gorest.co.in/public/v2/users/" + uID;

            User foundUser = restTemplate.getForObject(url, User.class);

            return new ResponseEntity<>("Temp", HttpStatus.OK);

        } catch (NumberFormatException e) {

            return new ResponseEntity<>("Id must be a number", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
