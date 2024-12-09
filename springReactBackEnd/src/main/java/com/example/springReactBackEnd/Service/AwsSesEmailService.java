package com.example.springReactBackEnd.Service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class AwsSesEmailService {
    private final SesClient sesClient = SesClient.builder().build();

    public String sendEmail(String to, String subject, String text) {
        Destination destination = Destination.builder().toAddresses(to).build();
        Content content = Content.builder().data(text).build();
        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder().text(content).build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source("your_email@yourdomain.com")
                .destination(destination)
                .message(message)
                .build();

        SendEmailResponse response = sesClient.sendEmail(request);
        return response.messageId();
    }
}

