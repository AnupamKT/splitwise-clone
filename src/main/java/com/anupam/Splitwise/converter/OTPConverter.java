package com.anupam.Splitwise.converter;

import com.anupam.Splitwise.entity.OneTimePassword;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OTPConverter {

    public OneTimePassword createOnePasswordEntity(String mobileNumber, long randomInt) {
        OneTimePassword otp=new OneTimePassword();
        otp.setMobileNumber(mobileNumber);
        otp.setOtp(randomInt);
        otp.setExpired(false);
        otp.setCreateDate(LocalDateTime.now());
        return otp;
    }
}
