package com.authx.services.serviceImpl;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.authx.exceptions.EmailAlreadyExistException;
import com.authx.exceptions.UnableToSendEmail;
import com.authx.services.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.authx.entity.UserEntity;
import com.authx.model.Role;
import com.authx.repository.UserRepository;
import com.authx.userdto.RegisterRequest;
import com.authx.userdto.RegisterResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    @Override
    public RegisterResponse createProfile(RegisterRequest request) {
          UserEntity newprofile = convertToUserEntity(request);

          if(!userRepo.existsByEmail(request.getEmail())){

             newprofile = userRepo.save(newprofile);

             return convertToProfileResponse(newprofile);
          }

          throw new EmailAlreadyExistException("Email already Exist");
    }

    private RegisterResponse convertToProfileResponse(UserEntity newprofile) {
           return RegisterResponse.builder()
                        .email(newprofile.getEmail())
                        .isAccountVerified(newprofile.getIsAccountVerified())
                        .name(newprofile.getName())
                        .userId(newprofile.getUserId())
                        .build();
    }

    private UserEntity convertToUserEntity(RegisterRequest request) {

       if (request.getPassword() == null || request.getPassword().isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty");
    }
       return UserEntity.builder()
                    .userId(UUID.randomUUID().toString())
                    .email(request.getEmail())
                    .name(request.getName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .isAccountVerified(false)
                    .verifyOtp(null)
                    .verifyOtpExpired(0L)
                    .resetPassword(null)
                    .resetOtpExpiredAt(0L)
                    .build();
    }

    @Override
    public RegisterResponse getProfile(String email) {
              UserEntity existingUser =  userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found: "+ email));
               return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {

      UserEntity existingEntity =  userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found:"+email));

      // Generat 6 digit otp
      String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

      // Calculate expiry time(15 min)
      Long expiry= System.currentTimeMillis()+(15 * 60 * 1000);

      // update in DB
      existingEntity.setResetOtp(otp);
      existingEntity.setResetOtpExpiredAt(expiry);

      userRepo.save(existingEntity);

      try {
         emailService.sendResetOtpEmail(existingEntity.getEmail(), otp);
      } catch (Exception e) {
         throw new UnableToSendEmail("Unable to send email");
      }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
       UserEntity existinguser =  userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("username not found :"+email));

       if(existinguser.getResetOtp() == null || !existinguser.getResetOtp().equals(otp)){
         throw new RuntimeException("Invalid OTP");
       }

       if(existinguser.getResetOtpExpiredAt()<System.currentTimeMillis()){
         throw new RuntimeException("OTP Expired");
       }
      existinguser.setPassword(passwordEncoder.encode(newPassword));
       existinguser.setResetOtp(null);
       existinguser.setResetOtpExpiredAt(0L);
      userRepo.save(existinguser);
    }

    @Override
    public void sendOtp(String email) {

       UserEntity existingUser =  userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found:"+email));

            if(existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()){
               return;
            }
       // Generat 6 digit otp
      String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

      // Calculate expiry time(24 hours)
      Long expiry= System.currentTimeMillis()+(24 * 60 * 60 * 1000);

      // Update user Entity
      existingUser.setVerifyOtp(otp);
      existingUser.setVerifyOtpExpired(expiry);
      // Update DB
      userRepo.save(existingUser);

      try {
         emailService.sendVerificationOtpEmail(existingUser.getEmail(), otp);
      } catch (Exception e) {
         throw new RuntimeException("Unable to send email");
      }
    }

    @Override
    public void verifyOtp(String email, String otp) {
         UserEntity existingUser =  userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found:"+email));

            if(existingUser.getVerifyOtp() == null || !existingUser.getVerifyOtp().equals(otp)){
                     throw new RuntimeException("Invalid OTP");   
            }
            if( existingUser.getVerifyOtpExpired() < System.currentTimeMillis()){
                  throw new RuntimeException("OTP expired");
            }

               existingUser.setIsAccountVerified(true);
               existingUser.setVerifyOtp(null);
               existingUser.setVerifyOtpExpired(0L);
            
               userRepo.save(existingUser);            
   }

   //  @Override
   //  public String getLoggedInUserId(String email) {
   //      UserEntity existingUser = userRepo.findByEmail(email)
   //                   .orElseThrow(() -> new UsernameNotFoundException("User not found :"+email));
         
   //     return existingUser.getUserId();
    
   //                }

}
