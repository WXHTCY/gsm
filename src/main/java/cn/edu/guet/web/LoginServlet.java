package cn.edu.guet.web;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.dao.UserDao;
import cn.edu.guet.dao.impl.UserDaoImplOracle;
import cn.edu.guet.service.UserService;
import cn.edu.guet.service.impl.UserServiceImpl;
import cn.edu.guet.util.ConfigReader;
import cn.edu.guet.vo.PermissionListVo;
import com.alibaba.fastjson2.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class LoginServlet extends HttpServlet {

    private UserDao userDao;
    private UserService userService;

    public LoginServlet() {
        userService = new UserServiceImpl();
        //        userDao = new UserDaoImplOracle();

        String className = ConfigReader.getClassName();
        try {
            // 反射创建对象
            userDao = (UserDao) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        boolean loginResult = userDao.login(username, password);
        // 如果登录成功，则跳转到index.html
        if (loginResult) {
            // 跳转到index.html
            // 目前演示的是通过后端跳转，但是前后端分离的项目，都是前端跳转
            // request.getRequestDispatcher("index.html").forward(request, response);

            // 跳转页面之前，先把用户对应的菜单查询到，然后把菜单以JSON格式返回给前端（浏览器），使用fastjson把List转成JSON即可
            List<Permission> permissionList = userService.getPermissionsByUsername(username);

            PermissionListVo permissionListVo = new PermissionListVo();
            permissionListVo.setPermissionList(permissionList);
            permissionListVo.setStatus("200");

            Object json = JSON.toJSON(permissionListVo);

            System.out.println(json);
            // 返回登录成功的信号给前端
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        }
    }
}
