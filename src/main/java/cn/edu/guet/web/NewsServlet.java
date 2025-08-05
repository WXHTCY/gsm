package cn.edu.guet.web;

import cn.edu.guet.bean.News;
import cn.edu.guet.http.HttpResult;
import cn.edu.guet.service.NewsService;
import cn.edu.guet.service.impl.NewsServiceImpl;
import cn.edu.guet.vo.Data;
import cn.edu.guet.vo.WangEditorVo;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

// 添加@WebServlet注解，明确指定URL映射路径
@WebServlet(urlPatterns = {"/news", "/news/*"})
public class NewsServlet extends HttpServlet {

    private NewsService newsService = new NewsServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        String requestURI = request.getRequestURI();
        System.out.println("接收到POST请求: " + requestURI);

        try {
            if (requestURI.contains("saveNews")) {
                saveNews(request, response);
            } else if (requestURI.contains("upload")) {
                upload(request, response);
            } else if (requestURI.contains("getNews")) {
                doGet(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(JSON.toJSONString(HttpResult.error(404, "未找到对应接口")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(JSON.toJSONString(HttpResult.error(500, "服务器内部错误")));
        }
    }

    private void saveNews(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        StringBuffer sb = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        System.out.println("新闻内容：" + sb.toString());
        newsService.saveNews(sb.toString());

        String result = JSON.toJSONString(HttpResult.ok("新闻保存成功!"));
        response.getWriter().write(result);
    }

    private void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 使用ServletContext获取真实路径，避免硬编码
        String realPath = request.getServletContext().getRealPath("/upload");
        File uploadDir = new File(realPath);

        // 确保上传目录存在
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        System.out.println("上传目录：" + realPath);

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        File savedFile = new File(uploadDir, fileName);

                        item.write(savedFile);

                        String contextPath = request.getContextPath();
                        String url = request.getScheme() + "://" +
                                request.getServerName() + ":" +
                                request.getServerPort() +
                                contextPath + "/upload/" + fileName;

                        Data data = new Data();
                        data.setUrl(url);

                        WangEditorVo wangEditorVo = new WangEditorVo();
                        wangEditorVo.setErrno(0);
                        wangEditorVo.setData(data);

                        response.getWriter().print(JSON.toJSONString(wangEditorVo));
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(JSON.toJSONString(HttpResult.error(500, "文件上传失败")));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(JSON.toJSONString(HttpResult.error(400, "请求格式错误")));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("接收到GET请求: " + request.getRequestURI());
        response.setContentType("application/json;charset=UTF-8");
        try {
            News news = newsService.getNews();
            System.out.println("新闻：" + news);

            if (news != null) {
                String jsonResult = JSON.toJSONString(news);
                System.out.println(jsonResult);
                response.getWriter().write(jsonResult);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(JSON.toJSONString(HttpResult.error(404, "未找到新闻")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(JSON.toJSONString(HttpResult.error(500, "数据库错误")));
        }
    }
}