package ru.eremin.ad.board.business.service;

import org.springframework.stereotype.Service;
import ru.eremin.ad.board.business.service.api.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getCurrentUserName() {
        return "system";
    }
}
