package cn.edu.guet.dao.impl;

import cn.edu.guet.bean.News;
import cn.edu.guet.dao.NewsDao;

import java.sql.*;

public class NewsDaoImpl implements NewsDao {

    private static final String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
    private static final String USER = "scott";
    private static final String PASSWORD = "tiger";

    @Override
    public void save(News news) throws SQLException {
        String sql = "INSERT INTO news (ID, TITLE, AUTHOR, PUB_TIME, CLICK_COUNT, CONTENT, PROVIDER, REVIEWER) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 获取连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 创建预编译语句
            pstmt = conn.prepareStatement(sql);
            // 设置参数
            pstmt.setString(1, news.getId());
            pstmt.setString(2, news.getTitle());
            pstmt.setString(3, news.getAuthor());
            pstmt.setDate(4, news.getPubTime());
            pstmt.setInt(5, news.getClickCount());
            pstmt.setString(6, news.getContent());
            pstmt.setString(7, news.getProvider());
            pstmt.setString(8, news.getReviewer());
            // 执行更新
            pstmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle驱动加载失败", e);
        } finally {
            // 关闭资源
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public News getNews() throws SQLException {
        String sql = "SELECT * FROM NEWS WHERE AUTHOR = 'IKUN' ";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        News news = null;

        try {
            // 加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 获取连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 创建预编译语句
            pstmt = conn.prepareStatement(sql);
            // 执行查询
            rs = pstmt.executeQuery();

            // 处理结果集
            if (rs.next()) {
                news = new News();
                news.setId(rs.getString("ID"));
                news.setTitle(rs.getString("TITLE"));
                news.setAuthor(rs.getString("AUTHOR"));
                news.setPubTime(rs.getDate("PUB_TIME"));
                news.setClickCount(rs.getInt("CLICK_COUNT"));
                news.setContent(rs.getString("CONTENT"));
                news.setProvider(rs.getString("PROVIDER"));
                news.setReviewer(rs.getString("REVIEWER"));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return news;
    }
}