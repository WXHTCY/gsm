package cn.edu.guet.service.impl;

import cn.edu.guet.bean.News;
import cn.edu.guet.dao.NewsDao;
import cn.edu.guet.dao.impl.NewsDaoImpl;
import cn.edu.guet.service.NewsService;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsServiceImpl implements NewsService {

    private NewsDao newsDao = new NewsDaoImpl();

    @Override
    public void saveNews(String newsContent) throws IOException, SQLException {
        // 具体业务代码
        String title = "";
        String content = "";
        String provider = "";
        try {
            JSONObject jsonObject = JSONObject.parseObject(newsContent);
            provider = jsonObject.getString("provider");
            // 根据key拿到value
            content = jsonObject.getString("content");
            // 然后拿到标题
            Matcher matcher = Pattern.compile("<h[1-2].*>(.*?)</h[1-2]>").matcher(content);
            // 只匹配一次。
            if (matcher.find()) {
                // group(1)获取第一个分组的内容
                // group(0)获取整个匹配的内容
                title = matcher.group(1);
                System.out.println("标题：" + title);
                content = content.substring(matcher.end());
            }
        } catch (JSONException e) {
            throw new IOException("Error parsing JSON request string");
        }
        // 调用dao层来实现真正的数据保存
        News news = new News();

        String id = UUID.randomUUID().toString().replace("-", "");
        news.setId(id);

        news.setTitle(title);
        news.setAuthor("IKUN");
        // hutool去生成当前时间

        news.setPubTime(new Date(System.currentTimeMillis()));
        news.setClickCount(0);
        // 处理：作者、时间、点击数，什么时候组装？点击新闻（查看新闻的时候再组装）
        System.out.println("这里是具体的新闻内容：" + content);
        news.setContent(content);
        news.setProvider(provider);
        newsDao.save(news);
    }

    @Override
    public News getNews() throws IOException, SQLException {
        News news = newsDao.getNews();
        //String content = news.getContent();
        //Matcher matcher = Pattern.compile("<h[1-2].*>(.*?)</h[1-2]>").matcher(content);
        // 调用dao层来实现真正的数据保存
        //News news = new News();

        return news;
    }
}