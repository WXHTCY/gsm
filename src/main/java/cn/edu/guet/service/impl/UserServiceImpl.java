package cn.edu.guet.service.impl;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.dao.UserDao;
import cn.edu.guet.dao.impl.UserDaoImplOracle;
import cn.edu.guet.service.UserService;
import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImplOracle();
    }

    @Override
    public List<Permission> getPermissionsByUsername(String username) {
        // 既包含1级也包含2级
        List<Permission> permissionList = userDao.getPermissionsByUsername(username);

        List<Permission> secondPermissionList = new ArrayList<Permission>();
        // lambda：函数式编程，创建二级菜单的集合
        permissionList.forEach(permission -> {
            if (permission.getParentId() != 0) {
                secondPermissionList.add(permission);
            }
        });
        // 下午不用lambda，而是使用增强for循环或传统for循环
        permissionList.forEach(permission -> {
            List<Permission> tempList = new ArrayList<>();
            secondPermissionList.forEach(secondPermission -> {
                if (secondPermission.getParentId() == permission.getPerId()) {
                    tempList.add(secondPermission);
                }
            });
            permission.setSubPermissionList(tempList);
        });
        // 集合运算：移除二级菜单
        permissionList.removeAll(secondPermissionList);
        System.out.println(JSON.toJSON(permissionList));
        return permissionList;
    }
}
