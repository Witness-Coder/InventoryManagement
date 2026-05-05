package com.example.InvetoryManagement.Services;

import java.util.List;
import com.example.InvetoryManagement.Entities.RequestToAdd;
import com.example.InvetoryManagement.Entities.Workers;

public interface RequestToAddService {

	void saveRequest(RequestToAdd request);

    List<RequestToAdd> getAllRequests();

    List<RequestToAdd> getUnreadRequests();

    void markAsRead(Long id);

    int getUnreadCount();

    RequestToAdd getRequestById(Long id);

    void deleteRequest(Long id);

    void addWorker(Workers worker);
}

