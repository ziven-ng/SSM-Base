package me.ziven.ssm.model.dao;

import java.util.List;

import me.ziven.ssm.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {
    int insert(SysUser record);

    int delete(int id);

    int update(SysUser record);

    List<SysUser> selectAll();

    SysUser findById(int id);
}