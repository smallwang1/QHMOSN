package com.piesat.site.datasearch.service.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Http工具类
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 设置下载参数
     * @param response
     * @param data
     * @param fileName
     * @throws IOException
     */
    public static void setParam(HttpServletResponse response, byte[] data, String fileName) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.addHeader("Content-Length", "" + data.length);
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 将InputStream写入本地文件
     * @param inputStream
     * @param filePath
     * @param fileName
     */
    public static void createLocalFile(InputStream inputStream, String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.exists()) file.mkdirs();

        File targetFile = new File(filePath + File.separator + fileName);
        OutputStream outputStream = null;
        try {
            int index;
            byte[] bytes = new byte[1024];
            outputStream = new FileOutputStream(targetFile);
            while ((index = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, index);
                outputStream.flush();
            }
            logger.info("Create File Success !");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Create File Fail !", e.getMessage());
        } finally {
            if (null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
