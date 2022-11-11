package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private static HashMap<User> userMap=new ConcurrentHashMap<>();

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return userMap.remove(id)!=null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if(user.isNew()){
            userMap.put(user.getId(),user);
        }
        else
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return userMap.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return userMap.values();
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return userMap.values().stream().filter(s->s.);
    }
}
