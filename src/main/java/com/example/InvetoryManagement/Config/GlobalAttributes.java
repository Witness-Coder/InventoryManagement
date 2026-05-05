package com.example.InvetoryManagement.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.InvetoryManagement.Services.RequestToAddService;

@ControllerAdvice
public class GlobalAttributes {

    @Autowired
    private RequestToAddService requestService;

    @ModelAttribute("unreadCount")
    public int getUnreadCount() {
        return requestService.getUnreadRequests().size();
    }
}