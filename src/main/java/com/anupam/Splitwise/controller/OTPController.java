package com.anupam.Splitwise.controller;

import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("splitwise")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/otp/{mobileNumber}")
    public ResponseEntity generateOTP(@PathVariable(name ="mobileNumber" ) String mobileNumber) {
        Response response = otpService.generateOTP(mobileNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/otp/{mobileNumber}")
    public ResponseEntity getOTP(@PathVariable(name ="mobileNumber" ) String mobileNumber) throws Exception {
        Response response = otpService.getOTP(mobileNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/otp")
    public ResponseEntity getOTPByStatusAndMobileNumber(@RequestParam(required = false) String mobileNumber,@RequestParam(required = false) Boolean isExpired) throws Exception {
        Response response = otpService.getOTPByStatusAndMobileNumber(mobileNumber,isExpired);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
