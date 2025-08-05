package cn.edu.guet.service;

import cn.edu.guet.bean.News;

import java.io.IOException;
import java.sql.SQLException;

public interface NewsService {
    void saveNews(String newsContent) throws IOException, SQLException;

    //void getNews(String newsContent) throws IOException, SQLException;

    News getNews() throws IOException, SQLException;
}
