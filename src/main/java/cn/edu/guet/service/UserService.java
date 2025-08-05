package cn.edu.guet.service;


import cn.edu.guet.bean.Permission;

import java.util.List;

public interface UserService {
    List<Permission> getPermissionsByUsername(String username);
}
