package com.anupam.Splitwise.scheduler;

import com.anupam.Splitwise.entity.OneTimePassword;
import com.anupam.Splitwise.handler.OTPHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@Slf4j
@Component
@Data
public class OTPScheduler {

    @Autowired
    private OTPHandler otpHandler;

    //@Scheduled(fixedDelayString = "${splitwise.otp.scheduler.inactivate.time.hour}")
    public void inactivateOTP() {
        String fName = "inactivateOTP";
        log.info("starting scheduler:{}", fName);
        try {
            List<OneTimePassword> activeOTPS = otpHandler.getAllOTPForMobileNumber(null, false);
            List<OneTimePassword> oldOTP = filterExpiredOTP(activeOTPS);
            if(CollectionUtils.isEmpty(oldOTP)){
                log.debug("There is nothing to inactivate");
            }else{
                otpHandler.inactivateOldOTPS(oldOTP);
                log.info("Scheduler finished;{}", fName);
            }
        } catch (Exception e) {
            log.error("error occurred while running scheduler scheduler:{} with error message:{}", fName, e.getMessage());
        }
    }

    //@Scheduled(fixedDelayString = "${splitwise.otp.scheduler.delete.time.hour}")
    public void deleteOTP() {
        String fName = "deleteOTP";
        log.info("starting scheduler:{}", fName);
        try {
            List<OneTimePassword> expiredOTPs = otpHandler.getAllOTPForMobileNumber(null, true);
            otpHandler.deleteOTPs(expiredOTPs);
            log.info("ending scheduler:{}", fName);
        } catch (Exception e) {
            log.error("error occurred while running scheduler scheduler:{} with error message:{}", fName, e.getMessage());
        }
    }

    private List<OneTimePassword> filterExpiredOTP(List<OneTimePassword> activeOTPS) {
        Instant fifteenMinutesFromNow = Instant.now().minus(15, ChronoUnit.MINUTES);
        List<OneTimePassword> oldOTP = activeOTPS.stream().
                filter(oneTimePassword -> oneTimePassword.getCreateDate().toInstant(ZoneOffset.UTC).isBefore(fifteenMinutesFromNow)).
                collect(Collectors.toList());
        return oldOTP;
    }
}
