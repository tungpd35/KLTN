package com.example.logistic.controllers;
import com.example.logistic.entities.Message;
import com.example.logistic.entities.Notification;
import com.example.logistic.entities.User;
import com.example.logistic.repositories.NotifyRepository;
import com.example.logistic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class NotificationController {
    @Autowired
    private UserService userService;
    @Autowired
    private NotifyRepository notifyRepository;
    @MessageMapping("/create-order")
    @SendTo("/topic/staff/notify")
    public @ResponseBody Notification send(@RequestBody Notification notify) throws Exception {
        notify.setCreateTime(new Date());
        notifyRepository.save(notify);
        return notify;
    }
    @GetMapping("api/v1/notify/list")
    public @ResponseBody Page<Notification> getNotice(@RequestHeader("Authorization") String token,
                          @RequestParam int page, @RequestParam int page_size) {
        PageRequest pageRequest = PageRequest.of(page,page_size);
        User user = userService.getUserFromToken(token);
        return notifyRepository.getNotificationsByUserIdOrderByCreateTimeDesc(user.getId(), pageRequest);
    }
    @GetMapping("api/v1/notify/read")
    public @ResponseBody ResponseEntity<?> readNotice(@RequestHeader("Authorization") String token
                                                      ) {
        User user = userService.getUserFromToken(token);
        List<Notification> notifications = notifyRepository.getAllByUserId(user.getId());
        notifications.forEach(notification -> notification.setUnread(false));
        notifyRepository.saveAll(notifications);
        return ResponseEntity.ok("Successfully");
    }
}