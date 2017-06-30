package me.ziven.ssm.service.impl;

import me.ziven.ssm.common.exception.BizException;
import me.ziven.ssm.model.dao.UserDao;
import me.ziven.ssm.model.entity.SysUser;
import me.ziven.ssm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ziven on 2017/6/29.
 */

@Service
public class UserServiceImpl implements UserService {
    private final Logger LOG = LogManager.getLogger(this.getClass());

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<SysUser> userList(int page, int limit) {
        return userDao.selectAll();
    }

    @Override
    public void save(SysUser sysUser) {
        boolean insert = null == sysUser.getId() || 0 == sysUser.getId();
        int rowChange = insert ? userDao.insert(sysUser) : userDao.update(sysUser);
        if (0 == rowChange) {
            throw new BizException((insert ? "添加" : "修改") + "用户失败", BizException.Type.page);
        }
    }

    @Override
    public void delete(int id) {
        if (1 > userDao.delete(id)) {
            throw new BizException("删除的对象不存在", BizException.Type.page);
        }
    }

    @Override
    public SysUser findByID(int id) {
        return userDao.findById(id);
    }
}
