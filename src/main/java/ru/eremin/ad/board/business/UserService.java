package ru.eremin.ad.board.business;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String getCurrentUserName() {
        return "system";
    }
}
