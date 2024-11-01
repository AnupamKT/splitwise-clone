package com.anupam.Splitwise.service.user;

import com.anupam.Splitwise.converter.user.UserConverter;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.user.InvalidUserException;
import com.anupam.Splitwise.handler.user.UserDataHandler;
import com.anupam.Splitwise.kafka.KafkaProducerService;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.member.User;
import com.anupam.Splitwise.validator.user.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Slf4j
public class UserService {

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private UserDataHandler userDataHandler;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Response createUser(User user) throws Exception {
        //validation
        userValidator.validateCreateUser(user);
        //conversion
        UserEntity userEntity = userConverter.convertUser(user);
        //persist
        userEntity = userDataHandler.createUser(userEntity);
        Response response = new Response(HttpStatus.ACCEPTED.value(), userEntity);
        //kafkaProducerService.sendMessage("user","user created");
        return response;
    }

    public Response getUser(String email) throws Exception {
        if (StringUtils.hasText(email)) {
            UserEntity userEntity = userDataHandler.findUserByEmail(email);
            Response response = new Response(HttpStatus.OK.value(), userEntity);
            return response;
        } else {
            throw new InvalidUserException("email cannot be null or blank");
        }
    }
}
