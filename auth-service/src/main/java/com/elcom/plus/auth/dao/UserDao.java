package com.elcom.plus.auth.dao;

import com.elcom.plus.auth.entity.User;
import org.apache.commons.codec.language.AbstractCaverphone;

public interface UserDao {
    User findByUsername(String username);
    void save(User user);
    void updateLastLogin(User user);
    void forgotPassword(String username, String passwordEncoder);
    boolean saveToken(String phone, String token);

    int saveSocialUser(String id, String name, String avatar, int type);

    User findUserById(int id);
}
