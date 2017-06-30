package me.ziven.ssm.service;

import me.ziven.ssm.model.entity.SysUser;

import java.util.List;

/**
 * Created by ziven on 2017/6/29.
 */
public interface UserService {
    List<SysUser> userList(int page, int limit);

    void save(SysUser sysUser);

    void delete(int id);

    SysUser findByID(int id);
}
