package com.careerdevs.gorestsql.controllers;
// all code in here

import com.careerdevs.gorestsql.models.User;
import com.careerdevs.gorestsql.repos.UserRepository;
import com.careerdevs.gorestsql.utils.ApiErrorHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
//        try {
//
//            if (ApiErrorHandling.isStrNaN(id)) {
//                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " is not a valid ID");
//            }
//
//            int uID = Integer.parseInt(id);
//
//            Optional<User> foundUser = userRepository.findById(uID);
//
//            if (foundUser.isEmpty()) {
//                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found with ID: " + id);
//            }
//            //check the range => other things to do
//
//            String url = "https://gorest.co.in/public/v2/users/" + uID;
//            System.out.println(url);
//
//            User foundUser = restTemplate.getForObject(url, User.class);
//
//            assert foundUser != null; //0
//            User savedUser = userRepository.save(foundUser);
//
//            return new ResponseEntity<>(savedUser, HttpStatus.OK);
//
//        } catch (HttpClientErrorException e) {
//            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
//        } catch (Exception e) {
//            return ApiErrorHandling.genericApiError(e);
//        }
//    }


    @GetMapping("/upload/{id}")
    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId, RestTemplate restTemplate
    ) {
        try {

            if (ApiErrorHandling.isStrNaN(userId)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId + " is not a valid ID");
            }

            int uID = Integer.parseInt(userId);

            //check the range => other things to do

            String url = "https://gorest.co.in/public/v2/users/" + uID;
            System.out.println(url);

            User foundUser = restTemplate.getForObject(url, User.class);

            assert foundUser != null; //0
            User savedUser = userRepository.save(foundUser);

            return new ResponseEntity<>(savedUser, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }

//        catch (NumberFormatException e) {
//
//            return new ResponseEntity<>("Id must be a number", HttpStatus.NOT_FOUND);
//        }
        catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() {
        try {
            Iterable<User> allUsers = userRepository.findAll();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }
}
