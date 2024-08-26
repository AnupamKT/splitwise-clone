package com.anupam.Splitwise.handler;

import com.anupam.Splitwise.converter.OTPConverter;
import com.anupam.Splitwise.entity.OneTimePassword;
import com.anupam.Splitwise.exception.OTPNotFoundException;
import com.anupam.Splitwise.repository.OTPRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class OTPHandler {

    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private OTPConverter otpConverter;

    public void persistOTPInDB(String mobileNumber, long randomInt) {
        try {
            List<OneTimePassword> oldOtps = getAllOTPForMobileNumber(mobileNumber, false);
            inactivateOldOTPS(oldOtps);
            OneTimePassword otp = otpConverter.createOnePasswordEntity(mobileNumber, randomInt);
            otpRepository.save(otp);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void inactivateOldOTPS(List<OneTimePassword> otps) throws Exception {
        List<OneTimePassword> expiredOtps = new ArrayList<>();
        try {
            otps.forEach(oneTimePassword -> {
                oneTimePassword.setExpired(true);
                expiredOtps.add(oneTimePassword);
            });
            otpRepository.saveAll(expiredOtps);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void deleteOTPs(List<OneTimePassword> otps) throws Exception {
        try {
            if (CollectionUtils.isEmpty(otps)) {
                log.debug("There is nothing to delete");
            }else{
                otpRepository.deleteAll(otps);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<OneTimePassword> getAllOTPForMobileNumber(String mobileNumber, Boolean isExpired) throws Exception {
        try {
            List<OneTimePassword> otpList = new ArrayList<>();
            //find All when neither mobile number nor status is given
            if (mobileNumber == null && isExpired == null) {
                otpList = otpRepository.findAll();
                //when only status is provided
            } else if (mobileNumber == null && isExpired != null) {
                otpList = otpRepository.findByIsExpired(isExpired);
                //when only mobile number is provided
            } else if (mobileNumber != null && isExpired == null) {
                otpList = otpRepository.findByMobileNumber(mobileNumber);
                //when both MobileNumber and status is provided
            } else if (mobileNumber != null && isExpired != null) {
                otpList = otpRepository.findOTPByMobileNumberAndStatus(isExpired, mobileNumber);
            }
            return otpList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public long getOtp(String mobileNumber) throws Exception {
        String msg = "otp not found for mobile number " + mobileNumber;
        try {
            List<OneTimePassword> otp = getAllOTPForMobileNumber(mobileNumber, false);
            if (!otp.isEmpty()) {
                return otp.stream().findAny().get().getOtp();
            } else {
                throw new OTPNotFoundException(msg);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }


}
