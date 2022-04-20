package com.careerdevs.gorestsql.controllers;
// all code in here

import com.careerdevs.gorestsql.models.User;
import com.careerdevs.gorestsql.repos.UserRepository;
import com.careerdevs.gorestsql.utils.ApiErrorHandling;
import com.careerdevs.gorestsql.validation.UserValidation;
import com.careerdevs.gorestsql.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/user")

public class UserController {

    /*
    Required Routes for GoRestSQL MVP:
        *Get route that returns one user by ID from the SQL database
        *Get route that returns all users stored in the SQL database
        * Delete route that deletes one user by ID from SQLa database (returns the deleted SQL DATA)
        * Delete route that deletes all users from SQL database (returns how many users were deleted)
        * Post route that queries one user by ID from GoRest and saves their data to your local database
        (returns the SQL user data)
        * Post route that uploads all users from the GoRest API into the SQL DATABASE (returns how many
        users were uploaded.)
        * Post route that creates a user on JUST the SQL database (returns the newly created SQL user data)
        * Put route that updates a user on JUST the SQL database (returns the updated SQL user data)

     */

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        try {
            // control over error message and you get the 400. And code block is not needed.
            if (ApiErrorHandling.isStrNaN(id)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " is not a valid ID");
            }

            int uID = Integer.parseInt(id);

            Optional<User> foundUser = userRepository.findById(uID);

            if (foundUser.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found with ID: " + id);
            }
            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }


//    @GetMapping("/upload/{id}")
//    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId, RestTemplate restTemplate
//    ) {
//        try {
//
//            if (ApiErrorHandling.isStrNaN(userId)) {
//                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId + " is not a valid ID");
//            }
//
//            int uID = Integer.parseInt(userId);
//
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
//        }
//
////        catch (NumberFormatException e) {
////
////            return new ResponseEntity<>("Id must be a number", HttpStatus.NOT_FOUND);
////        }
//        catch (Exception e) {
//            return ApiErrorHandling.genericApiError(e);
//        }
//    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<?> deleteAllUsers() {
        try {

            long totalUsers = userRepository.count(); // count method whole number
            userRepository.deleteAll();

            return new ResponseEntity<Long>(totalUsers, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id, RestTemplate restTemplate
    ) {
        try {

            if (ApiErrorHandling.isStrNaN(id)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " is not a valid ID");
            }

            int uID = Integer.parseInt(id);

            //check the range => other things to do

            Optional<User> foundUser = userRepository.findById(uID);

            if (foundUser.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Not Found with ID: " + id);
            }

            userRepository.deleteById(uID);

            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }


    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadUserById(
            @PathVariable("id") String userId,
            RestTemplate restTemplate // making an external api request
    ) {

        try {

            if (ApiErrorHandling.isStrNaN(userId)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId + "is not a valid ID");
            }

            int uId = Integer.parseInt(userId);

            String url = "https://gorest.co.in/public/v2/users" + uId;

            User foundUser = restTemplate.getForObject(url, User.class);
            if (foundUser == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with ID:" + uId + " not found");
            }

            User savedUser = userRepository.save(foundUser);

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) {
        try {

            ValidationError newUserErrors = UserValidation.validateNewUser(newUser);

            if (newUserErrors.hasError()){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, newUserErrors.toString());
            }

            User savedUser = userRepository.save(newUser);

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @PostMapping("/uploadall")
    public ResponseEntity<?> uploadAll(
            RestTemplate restTemplate
    ) {
        try {
            String url = "https://gorest.co.in/public/v2/users";

            ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);

            User[] firstPageUsers = response.getBody();

            // assert firstPageUsers != null;

            if (firstPageUsers == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                        "users from GOREST");
            }

            ArrayList<User> allUsers = new ArrayList<>(Arrays.asList(firstPageUsers));

            HttpHeaders responseHeaders = response.getHeaders();

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);
            int totalPgNum = Integer.parseInt(totalPages);

            for (int i = 2; i <= totalPgNum; i++) {
                String pageUrl = url + "?page=" + i;
                User[] pageUsers = restTemplate.getForObject(pageUrl, User[].class);

                if (pageUsers == null) {
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Failed to GET first page " + i + " of users from GoRest ");
                }
                allUsers.addAll(Arrays.asList(firstPageUsers));
            }

            userRepository.saveAll(allUsers);

            return new ResponseEntity<>("Users Created " + allUsers.size(), HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }


    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {

            User savedUser = userRepository.save(user);

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    //http://localhost:8080/user/all
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
