package com.trix.uploader.validators;

import com.trix.uploader.exceptions.validation.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        String path = (String) target;

        Pattern illegalCharacters = Pattern.compile("[\\\\|<>:\"?*]");
        Matcher matcher = illegalCharacters.matcher(path);
        if (matcher.find()) {
            errors.reject("illegal_characters", "Path variable contains illegal characters");
        }

    }

    public BindingResult validate(Object target) {
        MapBindingResult errors = new MapBindingResult(new HashMap<String, Object>(), "result");
        this.validate(target, errors);

        if (errors.hasErrors())
            throw new ValidationException(errors.getAllErrors().toString());

        return errors;
    }
}
