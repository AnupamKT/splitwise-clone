package com.anupam.Splitwise.handler.user;

import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.user.UserNotFoundException;
import com.anupam.Splitwise.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
@Slf4j
public class UserDataHandler {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserEntity createUser(UserEntity userEntity) {
        long startTime = Instant.now().toEpochMilli();
        String email = userEntity.getEmail();
        log.info("started createUser for user with email:{}", email);
        try {
            userEntity = userRepository.save(userEntity);
            log.debug("user with email:{} is created", email);
            return userEntity;
        } catch (Exception ex) {
            log.error("error creating user with email:{} with error message:{}", email, ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.info("finished createUser for user with email:{}, total time taken is:{}", email, totalTime);
        }
    }

    public UserEntity findUserByEmail(String email) throws Exception {
        log.info("started findUserByEmail for user with email:{}", email);
        long startTime = Instant.now().toEpochMilli();
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
            if (userEntityOptional.isPresent()) {
                return userEntityOptional.get();
            } else {
                throw new UserNotFoundException("user not found with email: " + email);
            }
        } catch (Exception ex) {
            log.error("error occurred while fetching user with email:{}", email);
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.info("Finished findUserByEmail for user with email:{}, total time taken is: {}", email, totalTime);
        }
    }

    public boolean doesUserExists(String email) {
        try {
            UserEntity userEntity = findUserByEmail(email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
