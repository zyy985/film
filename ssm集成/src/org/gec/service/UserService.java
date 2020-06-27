package org.gec.service;

import org.gec.pojo.Users;

public interface UserService {
    Users login(String username, String password);
}
