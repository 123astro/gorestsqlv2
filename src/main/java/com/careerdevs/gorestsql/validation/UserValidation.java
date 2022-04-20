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

        if (user.getName() == null || user.getName().trim().equals("")) {
            errors.addError("name", "name can't be left blank");
        }

        if (user.getEmail() == null || user.getEmail().trim().equals("")) {
            errors.addError("email", "email can't be left blank");
        }

        if (user.getGender() == null || user.getGender().trim().equals("")) {
            errors.addError("Gender", "Gender can't be left blank");
        }

        if (user.getStatus() == null || user.getStatus().trim().equals("")) {
            errors.addError("status", "Status can't be left blank");
        }
        return errors;
    }
}
