package com.authx.services;

import com.authx.userdto.RegisterRequest;
import com.authx.userdto.RegisterResponse;

public interface ProfileService {

   RegisterResponse createProfile(RegisterRequest request);

   RegisterResponse getProfile(String email);

   void sendResetOtp(String email);

   void resetPassword(String email, String otp, String newPassword);

   void sendOtp(String id);

   void verifyOtp(String userId, String otp);

   // String getLoggedInUserId(String email);
}
