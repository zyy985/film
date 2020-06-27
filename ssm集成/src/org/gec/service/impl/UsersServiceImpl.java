package org.gec.service.impl;

import org.gec.mapper.UsersMapper;
import org.gec.pojo.Users;
import org.gec.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users login(String username, String password) {
        Users user = usersMapper.login(username,password);
        return null;
    }
}
