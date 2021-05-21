package ru.eremin.ad.board.storage.callback;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.UserService;
import ru.eremin.ad.board.storage.model.AbstractTable;
import ru.eremin.ad.board.storage.model.Ad;
import ru.eremin.ad.board.storage.model.Category;

import java.time.Instant;

@Component
class AdBeforeConvertCallback implements BeforeConvertCallback<Ad> {
    private final UserService userService;

    @Autowired
    public AdBeforeConvertCallback(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public Publisher<Ad> onBeforeConvert(final Ad entity, final SqlIdentifier table) {
        return Mono.just(Utils.fill(userService.getCurrentUserName(), entity));
    }
}

@Component
class CategoryBeforeConvertCallback implements BeforeConvertCallback<Category> {
    private final UserService userService;

    @Autowired
    public CategoryBeforeConvertCallback(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public Publisher<Category> onBeforeConvert(final Category entity, final SqlIdentifier table) {
        return Mono.just(Utils.fill(userService.getCurrentUserName(), entity));
    }
}

class Utils {
    static <T extends AbstractTable> T fill(String userName, T entity) {
        if (entity.getCreateUser() == null) {
            entity.setCreateUser(userName);
            entity.setCreateTime(Instant.now());
        }
        entity.setLastModifiedTime(Instant.now());
        entity.setLastModifiedUser(userName);

        return entity;
    }
}
