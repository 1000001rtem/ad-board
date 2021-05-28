package ru.eremin.ad.board.business.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String getCurrentUserName() {
        return "system";
    }
}
