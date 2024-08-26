package com.anupam.Splitwise.service;

import com.anupam.Splitwise.entity.OneTimePassword;
import com.anupam.Splitwise.exception.OTPNotFoundException;
import com.anupam.Splitwise.handler.OTPHandler;
import com.anupam.Splitwise.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPService {
    @Autowired
    private OTPHandler otpHandler;

    public Response generateOTP(String mobileNumber) {
        SecureRandom random = new SecureRandom();
        long randomInt = random.nextInt(1000000);
        //persisting otp in DB asynchronously
        CompletableFuture.runAsync(()->otpHandler.persistOTPInDB(mobileNumber,randomInt));
        return new Response(200, randomInt);
    }


    public Response getOTP(String mobileNumber) throws Exception {
      long otp = otpHandler.getOtp(mobileNumber);
      return new Response(200,otp);
    }

    public Response getOTPByStatusAndMobileNumber(String mobileNumber,Boolean isExpired) throws Exception {
        List<OneTimePassword> otp = otpHandler.getAllOTPForMobileNumber(mobileNumber,isExpired);
        return new Response(200,otp);
    }
}
