package com.careerdevs.gorestsql.validation;

import com.careerdevs.gorestsql.models.User;
import com.careerdevs.gorestsql.repos.UserRepository;
import com.careerdevs.gorestsql.utils.ApiErrorHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

public class UserValidation {


    public static ValidationError validateNewUser(User user, UserRepository userRepo, boolean isUpdate) {
        ValidationError errors = new ValidationError();

        if (isUpdate) {

            if (user.getId() == null) {
                errors.addError("id", "id can't be left blank");
            } else {
                Optional<User> foundUser = userRepo.findById(user.getId());
                if (foundUser.isEmpty()) {
                    errors.addError("id", "No user found with the ID: " + user.getId());
                } else {
                    System.out.println(foundUser.get());
                }
            }
        }

        String userName = user.getName();
        String userEmail = user.getEmail();
        String userGender = user.getGender();
        String userStatus = user.getStatus();

        if (user.getName() == null || user.getName().trim().equals("")) {
            errors.addError("name", "name can't be left blank");
        }

        if (user.getEmail() == null || user.getEmail().trim().equals("")) {
            errors.addError("email", "email can't be left blank");
        }

        if (user.getGender() == null || user.getGender().trim().equals("")) {
            errors.addError("Gender", "Gender can't be left blank");
        } else if (!(userGender.equals("male") || userGender.equals("female") || userGender.equals("other"))) {
            errors.addError("gender", "Gender must be: male, female, other");
        }

        if (user.getStatus() == null || user.getStatus().trim().equals("")) {
            errors.addError("status", "Status can't be left blank");
        } else if (!(userStatus.equals("active") || userStatus.equals("inactive"))) {
            errors.addError("status", "Status must be: active or inactive");
        }

        return errors;
    }
}
