package cn.objectspace.webssh.service.impl;

import cn.objectspace.webssh.constant.ConstantPool;
import cn.objectspace.webssh.pojo.SSHConnectInfo;
import cn.objectspace.webssh.pojo.WebSSHData;
import cn.objectspace.webssh.service.WebSSHService;
import cn.objectspace.webssh.service.WebSftpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @Description: WebSSH业务逻辑实现
* @Author: NoCortY
* @Date: 2020/3/8
*/
@Service
public class WebSftpServiceImpl implements WebSftpService {


    private Logger logger = LoggerFactory.getLogger(WebSftpServiceImpl.class);

    private static final ExecutorService SINGLE = Executors.newSingleThreadExecutor();

    @Override
    public void close(Channel channel) {
       if (!channel.isClosed()) {
           channel.disconnect();
       }
    }

    @Override
    public ChannelSftp connectToFtpSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData) throws JSchException, IOException {
        Session session = null;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        session.setConfig(config);
        //设置密码
        session.setPassword(Base64Utils.decode(webSSHData.getPassword().getBytes(StandardCharsets.UTF_8)));
        //连接  超时时间30s
        session.connect(30000);

        //开启shell通道
        Channel channel = session.openChannel("sftp");

        //通道连接 超时时间3s
        channel.connect(3000);

        return (ChannelSftp)channel;
    }
    /**
     * 写文件至远程sftp服务器
     *
     * @return
     */
    @Override
    public void writeFile(String localPath, String remoteDir, ChannelSftp channel){
        ByteArrayInputStream input = null;
        try {
            String substring = localPath.split("\rfile:")[1];
            String filePath = new String(Base64Utils.decode(substring.getBytes(StandardCharsets.UTF_8)));
            // 更改服务器目录
            channel.cd(remoteDir);
            // 发送文件
            String name = FilenameUtils.getName(filePath);
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(filePath));
            input = new ByteArrayInputStream(bytes);
            channel.put(input, name);
            SINGLE.execute(() -> this.deleteLocalFile(filePath));
        } catch (Exception e) {
            logger.error("发送文件时有异常!",e);
        } finally {
            try {

                if (null != input) {
                    input.close();
                }
                // 关闭连接
                close(channel);
            } catch (Exception e) {
                logger.error("关闭文件时出错!",e);
            }
        }
    }

    private void deleteLocalFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
