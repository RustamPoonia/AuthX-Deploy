package com.authx.services.serviceImpl;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.sender.email}")
    private String senderEmail;

    @Value("${resend.sender.name}")
    private String senderName;

    private final OkHttpClient client = new OkHttpClient();

    private void sendEmail(String toEmail, String subject, String body) {

        String jsonBody = """
        {
          "from": "%s <%s>",
          "to": ["%s"],
          "subject": "%s",
          "html": "%s"
        }
        """.formatted(senderName, senderEmail, toEmail, subject, body);

        RequestBody requestBody = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.resend.com/emails")
                .addHeader("Authorization", "Bearer " + resendApiKey)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("Email sending failed: " + response.body().string());
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to send email", e);
        }
    }

    public void sendWelcomeEmail(String toEmail, String name) {

        String subject = "Welcome to AuthX";

        String body = """
        <p>Hello %s,</p>
        <p>Thanks for registering with us!</p>
        <p>Regards,<br>AuthX Team</p>
        """.formatted(name);

        sendEmail(toEmail, subject, body);
    }

    public void sendResetOtpEmail(String toEmail, String otp) {

        String subject = "Password Reset OTP";

        String body = """
        <p>Your OTP for resetting your password is:</p>
        <h2>%s</h2>
        <p>Use this OTP to proceed with resetting your password.</p>
        """.formatted(otp);

        sendEmail(toEmail, subject, body);
    }

    public void sendVerificationOtpEmail(String toEmail, String otp) {

        String subject = "Account Verification OTP";

        String body = """
        <p>Your OTP for account verification is:</p>
        <h2>%s</h2>
        <p>Use this OTP to verify your account.</p>
        """.formatted(otp);

        sendEmail(toEmail, subject, body);
    }
}
