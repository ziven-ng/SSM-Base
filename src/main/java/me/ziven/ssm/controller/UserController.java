package me.ziven.ssm.controller;

import me.ziven.ssm.common.exception.BizException;
import me.ziven.ssm.model.cache.Cache;
import me.ziven.ssm.model.entity.SysUser;
import me.ziven.ssm.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final String CACHE_USER_LIST = "USER_LIST";
    private final Logger LOG = LogManager.getLogger(this.getClass());

    private final UserService userService;

    @Qualifier("RedisCache")
    private final Cache cache;

    @Autowired
    public UserController(UserService userService, Cache cache) {
        this.userService = userService;
        this.cache = cache;
    }

    @RequestMapping(value = "edit")
    public String edit(Model model, Integer id) {
        if (null != id) {
            SysUser sysUser = userService.findByID(id);
            if (null == sysUser) {
                throw new BizException("编辑的用户不存在", BizException.Type.page);
            }
            model.addAttribute("item", sysUser);
        }
        return "/user/edit";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(SysUser sysUser) {
        userService.save(sysUser);
        cache.delete(CACHE_USER_LIST);
        return "redirect:list";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete(int id) {
        userService.delete(id);
        cache.delete(CACHE_USER_LIST);
        return "redirect:list";
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Integer offset, Integer limit) {
        offset = offset == null ? 0 : offset;//默认便宜0
        limit = limit == null ? 50 : limit;//默认展示50条
        List<SysUser> list = cache.getList(CACHE_USER_LIST, SysUser.class);
        if (CollectionUtils.isEmpty(list)) {
            list = userService.userList(offset / limit, limit);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            cache.putList(CACHE_USER_LIST, list);
        }
        model.addAttribute("userList", list);
        return "/user/list";
    }

}
