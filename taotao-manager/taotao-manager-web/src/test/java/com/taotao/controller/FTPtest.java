package com.taotao.controller;

import com.taotao.common.utils.FtpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * @auther: ZhouCong
 * @date: Create in 2019/8/3 14:59
 * @description:
 */
public class FTPtest {
    @Test
    public void testFtpClient () throws Exception{
//        创建一个FTPClient对象
        FTPClient ftpClient = new FTPClient();
//        创建ftp连接
        ftpClient.connect("192.168.174.128",21);
//        登录ftp服务器，使用用户名和密码
        ftpClient.login("ftpuser","ftpuser");
//        设置上传路径
        ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
//        修改文件上传格式
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//        上传文件
//        读取本地文件
        FileInputStream inputStream = new FileInputStream(new File("E:\\1.jpg"));
//        第一个参数：服务器端文档名
//        第二个参数：上传文档的inputStream
        ftpClient.storeFile("hello.jpg",inputStream);
//        关闭连接
        ftpClient.logout();

    }

    @Test
    public  void testFtpUtils() throws Exception{
        FileInputStream inputStream = new FileInputStream(new File("E:\\1.jpg"));
        FtpUtil.uploadFile("192.168.174.128",21,"ftpuser","ftpuser","/home/ftpuser/www/images","/2019/08/03","hello.jpg",inputStream);
    }
}
