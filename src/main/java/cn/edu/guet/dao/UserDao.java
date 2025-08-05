package cn.edu.guet.dao;

import cn.edu.guet.bean.Permission;
import java.util.List;
/**
 * 封装对用户的数据库操作，接口只是规定可以做什么，但不具体实现
 * 不同的项目登录验证的方式不同、数据库的不同
 * 1、验证用户名和密码
 * 2、添加用户
 * 3、删除用户
 * 4、查询用户
 * 5、获取用户的菜单（权限）
 */
public interface UserDao {
    boolean login(String username, String password);
    List<Permission> getPermissionsByUsername(String username);
}
