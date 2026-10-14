package com.example.library.service;

import com.example.library.entity.Notification;
import com.example.library.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendDueDateNotification(String to, Long memberId, String bookTitle, String dueDate) {
        String msg = "Dear Member,\n\nThis is a reminder that the book '" + bookTitle + "' is due on " + dueDate + ".\n\nThank you,\nYour Library";
        
        Notification notification = new Notification(null, msg, memberId, new Date());
        notificationRepository.save(notification);

        if (mailSender != null && to != null && !to.isEmpty()) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject("Library Due Date Notification");
                message.setText(msg);
                mailSender.send(message);
            } catch (Exception ignored) {
                // Ignore mail sending failure if SMTP is not configured
            }
        }
    }

    public void sendNewArrivalNotification(String to, Long memberId, String bookTitle) {
        String msg = "Dear Member,\n\nWe are excited to inform you that the book '" + bookTitle + "' has just arrived at our library.\n\nThank you,\nYour Library";
        
        Notification notification = new Notification(null, msg, memberId, new Date());
        notificationRepository.save(notification);

        if (mailSender != null && to != null && !to.isEmpty()) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject("New Book Arrival Notification");
                message.setText(msg);
                mailSender.send(message);
            } catch (Exception ignored) {
                // Ignore mail sending failure if SMTP is not configured
            }
        }
    }

    public void sendDueDateNotification(String to, String bookTitle, String dueDate) {
        sendDueDateNotification(to, null, bookTitle, dueDate);
    }

    public void sendNewArrivalNotification(String to, String bookTitle) {
        sendNewArrivalNotification(to, null, bookTitle);
    }

    public List<Notification> getNotificationsForMember(Long memberId) {
        return notificationRepository.findByMemberId(memberId);
    }
}