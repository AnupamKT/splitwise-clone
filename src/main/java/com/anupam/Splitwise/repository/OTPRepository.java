package com.anupam.Splitwise.repository;

import com.anupam.Splitwise.entity.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OTPRepository extends JpaRepository<OneTimePassword, UUID> {
    List<OneTimePassword> findByMobileNumber(String mobileNumber);
    @Query(value = "select * from one_time_password ot where ot.is_expired=:isExpired and ot.mobile_number=:mobileNumber",nativeQuery = true)
    List<OneTimePassword> findOTPByMobileNumberAndStatus(@Param("isExpired")boolean isExpired,@Param("mobileNumber") String mobileNumber);
    List<OneTimePassword> findByIsExpired(boolean isExpired);
}
