package cn.edu.guet.dao.impl;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.dao.UserDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplOracle implements UserDao {

    @Override
    public boolean login(String username, String password) {
        // 写JDBC代码(固定模式，没有任何难度）
        String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
        String USER = "scott";
        String PASSWORD = "tiger";

        String sql = "SELECT * FROM USERS WHERE USER_NAME = ? AND PASSWORD = ?";
        try {
            // 加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("数据库操作错误: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Permission> getPermissionsByUsername(String username) {
        String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
        String USER = "scott";
        String PASSWORD = "tiger";

        List<Permission> permissionList = new ArrayList<>();

        String sql = "SELECT p.*\n" +
                "FROM users u,\n" +
                "     user_role ur,\n" +
                "     roles_permission rp,\n" +
                "     permission p\n" +
                "WHERE u.user_id = ur.user_id\n" +
                "  AND ur.role_id = rp.role_id\n" +
                "  AND rp.per_id = p.per_id\n" +
                "  AND u.user_name = ?";
        try {
            // 加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setPerId(rs.getInt("per_id"));
                    permission.setPerName(rs.getString("per_name"));
                    permission.setUrl(rs.getString("url"));
                    permission.setIcon(rs.getString("icon"));
                    permission.setParent(rs.getBoolean("is_parent"));
                    permission.setParentId(rs.getInt("parent_id"));
                    permissionList.add(permission);
                }
                return permissionList;
            }
        } catch (SQLException e) {
            System.err.println("数据库操作错误: " + e.getMessage());
        }
        return List.of();
    }
}
