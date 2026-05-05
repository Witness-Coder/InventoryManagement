package com.example.InvetoryManagement.ServiceImple;


import org.springframework.stereotype.Service;

import com.example.InvetoryManagement.Entities.RequestToAdd;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.RequestToAddRepository;
import com.example.InvetoryManagement.Repository.WorkersRepo;
import com.example.InvetoryManagement.Services.RequestToAddService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class RequestToAddServiceImpl implements RequestToAddService {

	@Autowired
    private RequestToAddRepository requestRepo;

    @Autowired
    private WorkersRepo workersRepo; 

    @Override
    public void saveRequest(RequestToAdd request) {
        requestRepo.save(request);
    }

    @Override
    public List<RequestToAdd> getAllRequests() {
        return requestRepo.findAll();
    }

    @Override
    public List<RequestToAdd> getUnreadRequests() {
        return requestRepo.findByReadStatusFalse();
    }

    @Override
    public void markAsRead(Long id) {
        requestRepo.findById(id).ifPresent(r -> {
            r.setReadStatus(true);
            requestRepo.save(r);
        });
    }

    @Override
    public int getUnreadCount() {
        return requestRepo.countByReadStatusFalse();
    }

    @Override
    public RequestToAdd getRequestById(Long id) {
        return requestRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepo.deleteById(id);
    }

    @Override
    public void addWorker(Workers worker) {
        workersRepo.save(worker);
    }
}

