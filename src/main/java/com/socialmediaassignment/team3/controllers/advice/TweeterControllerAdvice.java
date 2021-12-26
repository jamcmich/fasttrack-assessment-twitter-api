package com.socialmediaassignment.team3.controllers.advice;

import com.socialmediaassignment.team3.dtos.ErrorDto;
import com.socialmediaassignment.team3.exceptions.BadRequestException;
import com.socialmediaassignment.team3.exceptions.NotFoundException;
import com.socialmediaassignment.team3.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice(basePackages = { "com.socialmediaassignment.team3.controllers" })
@ResponseBody
public class TweeterControllerAdvice {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorDto handleBadRequestException(HttpServletRequest request, UnauthorizedException unauthorizedException) {
        return new ErrorDto(unauthorizedException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException) {
        return new ErrorDto(badRequestException.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleBadRequestException(HttpServletRequest request, NotFoundException notFoundException) {
        return new ErrorDto(notFoundException.getMessage());
    }
}
