package com.lcc.cloist.advice;

import com.lcc.cloist.advice.exception.*;
import com.lcc.cloist.model.response.CommonResult;
import com.lcc.cloist.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final MessageSource messageSource;
    private final ResponseService responseService;

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("unknown.code")), getMessage("unknown.message"));
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected CommonResult authenticationEntryPointException(HttpServletRequest request, AuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("entry-point-exception.code")), getMessage("entry-point-exception.message"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("access-denied.code")), getMessage("access-denied.message"));
    }

    @ExceptionHandler(CommunicationErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult communicationErrorException(HttpServletRequest request, CommunicationErrorException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("communication-error.code")), getMessage("communication-error.message"));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected CommonResult notAuthorizedException(HttpServletRequest request, NotAuthorizedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("not-authorized-exception.code")), getMessage("not-authorized-exception.message"));
    }

    @ExceptionHandler(InvalidFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult invalidFormException(HttpServletRequest request, InvalidFormException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("invalid-form-exception.code")), getMessage("invalid-form-exception.message"));
    }

    @ExceptionHandler(SignInFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult signInFailedException(HttpServletRequest request, SignInFailedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("sign-in-failed.code")), getMessage("sign-in-failed.message"));
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected CommonResult userExistException(HttpServletRequest request, UserExistException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("user-exist-exception.code")), getMessage("user-exist-exception.message"));
    }

    @ExceptionHandler(VoteExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected CommonResult voteExistException(HttpServletRequest request, VoteExistException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("vote-exist-exception.code")), getMessage("vote-exist-exception.message"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("user-not-found.code")), getMessage("user-not-found.message"));
    }

    @ExceptionHandler(GatheringNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult gatheringNotFoundException(HttpServletRequest request, GatheringNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("gathering-not-found.code")), getMessage("gathering-not-found.message"));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult messageNotFoundException(HttpServletRequest request, MessageNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("message-not-found.code")), getMessage("message-not-found.message"));
    }

    @ExceptionHandler(VoteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult voteNotFoundException(HttpServletRequest request, VoteNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("vote-not-found.code")), getMessage("vote-not-found.message"));
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult postNotFoundException(HttpServletRequest request, PostNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("post-not-found.code")), getMessage("post-not-found.message"));
    }
}