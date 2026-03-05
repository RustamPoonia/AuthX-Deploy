package com.authx.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.authx.jwt.JwtUtil;
import com.authx.services.serviceImpl.AppUserDetailsService;
import com.authx.services.ProfileService;
import com.authx.userdto.AuthRequest;
import com.authx.userdto.AuthResponse;
import com.authx.userdto.ResetPasswordRequest;
import com.authx.userdto.Verifyemailrequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "User APIs",description = "User Login, Password Update, Email Verification, Sending OTP")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService appUserDetailsService;

    private final JwtUtil jwtUtil;

    private final ProfileService profileService;

    @GetMapping("/email-test")
public String testEmail() {
     Syastem.out.println("In the email send message ");
    profileService.sendResetOtp("rustampoonia@gmail.com");
    System.out.println("Email triggered");
    return "Email triggered";
}

    @PostMapping("/login")
    @Operation(summary = "Login user with username and password")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticate(request.getEmail(), request.getPassword());
            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());

            String token = jwtUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Lax")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(request.getEmail(), token));
        } catch (BadCredentialsException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("error", "Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (DisabledException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("error", "Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("error", "Authentication failed due to exception" + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @GetMapping("/is-authenticated")
    @Operation(summary = "checks is user authenticated or not")
    public ResponseEntity<Boolean> isAuthenticated(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        return ResponseEntity.ok(email != null);
    }

    @PostMapping("/send-reset-otp")
    @Operation(summary = "Send OTP for password reset")
    public void sendResetPasswordOtp(@RequestParam String email) {
        try {
            profileService.sendResetOtp(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password if you enter the valid OTP")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            profileService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP to verify user email")
    public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email) {

        try {
            profileService.sendOtp(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify user email", description = "Verify user's email address using the OTP sent to their email")
    public ResponseEntity<String> verifyEmail(@RequestBody Verifyemailrequest request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {

        if (request.getOtp() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing details");
        }
        try {
            profileService.verifyOtp(email, request.getOtp());
            return ResponseEntity.ok("Email verified successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
     @PostMapping("/logout")
     @Operation(summary = "Logout the user and clear the Jwt token")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create a cookie with the same name and maxAge=0 to delete it
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0) // delete immediately
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }
}
