package cn.edu.guet.dao;

import cn.edu.guet.bean.News;

import java.sql.SQLException;

public interface NewsDao {
    void save(News news) throws SQLException;

    //PreparedStatement getNews(String news) throws IOException, SQLException;

    //News getNews() throws SQLException;

    News getNews() throws SQLException;
}
