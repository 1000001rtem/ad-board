package ru.eremin.ad.board.business.service.impl;

import org.springframework.stereotype.Service;
import ru.eremin.ad.board.business.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getCurrentUserName() {
        return "system";
    }
}
