package com.trix.uploader.validators;

import com.trix.uploader.exceptions.validation.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PathValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        String path = (String) target;

        Pattern illegalCharacters = Pattern.compile("[\\\\|<>:\"?*!@#$%^]");
        Matcher matcher = illegalCharacters.matcher(path);
        if (matcher.find()) {
            errors.reject("illegal_characters", "Path variable contains illegal characters");
        }
        Path relativePath = Paths.get(path);

        if (relativePath.isAbsolute())
            errors.reject("absolute_path", "Path should not be absolute. Please insert relative path");

    }

    public BindingResult validate(Object target) {
        MapBindingResult errors = new MapBindingResult(new HashMap<String, Object>(), "result");

        this.validate(target, errors);

        if (errors.hasErrors()) {

            String message = errors.getAllErrors()
                    .stream()
                    .map(objectError -> objectError.getDefaultMessage() + " ; ")
                    .collect(Collectors.joining());

            throw new ValidationException(message);
        }

        return errors;
    }
}
