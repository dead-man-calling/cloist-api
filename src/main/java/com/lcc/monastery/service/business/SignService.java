package com.lcc.monastery.service.business;

import com.lcc.monastery.advice.exception.InvalidFormException;
import com.lcc.monastery.advice.exception.SignInFailedException;
import com.lcc.monastery.advice.exception.UserExistException;
import com.lcc.monastery.advice.exception.UserNotFoundException;
import com.lcc.monastery.config.security.JwtTokenProvider;
import com.lcc.monastery.dto.sign.SignInRq;
import com.lcc.monastery.dto.sign.SignUpRq;
import com.lcc.monastery.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SignService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public String signIn(SignInRq signInRq) {
        User user = userService.findUser(signInRq.getUsername(), signInRq.getPhoneNumber());

        if (!passwordEncoder.matches(signInRq.getPassword(), user.getPassword()))
            throw new SignInFailedException();

        return jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRole());
    }

    @Transactional
    public void signUp(SignUpRq signUpRq) {
        try {
            User user =
                    userService.findUser(signUpRq.getUsername(), signUpRq.getPhoneNumber());

            if (user != null)
                throw new UserExistException();

        } catch (UserNotFoundException e) {
            String regKoreanName = "^[가-힣]{2,6}$";
            String regPhoneNumber = "^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$";

            if (!Pattern.matches(regKoreanName, signUpRq.getUsername()) ||
                !Pattern.matches(regPhoneNumber, signUpRq.getPhoneNumber()))
                throw new InvalidFormException();

            userService.createUser(signUpRq.getUsername(), signUpRq.getPassword(), signUpRq.getPhoneNumber());
        }
    }
}
