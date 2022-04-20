package com.careerdevs.gorestsql.validation;

import com.careerdevs.gorestsql.models.User;

public class UserValidation {

    public static ValidationError validateNewUser (User user) {
        ValidationError errors = new ValidationError();

        if(user.getName() == null || user.getName().trim().equals("")){
            errors.addError("name", "name can't be left blank");
        }

        if(user.getEmail() == null || user.getEmail().trim().equals("")){
            errors.addError("email", "email can't be left blank");
        }

        if(user.getGender() == null || user.getGender().trim().equals("")){
            errors.addError("Gender", "Gender can't be left blank");
        }

        if(user.getStatus() == null || user.getStatus().trim().equals("")){
            errors.addError("status", "Status can't be left blank");
        }
        return errors;
    }
}
