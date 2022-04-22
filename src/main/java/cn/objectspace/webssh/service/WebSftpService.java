package cn.objectspace.webssh.service;

import cn.objectspace.webssh.pojo.SSHConnectInfo;
import cn.objectspace.webssh.pojo.WebSSHData;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @Description: WebSSH的业务逻辑
 * @Author: NoCortY
 * @Date: 2020/3/7
 */
public interface WebSftpService {


    ChannelSftp connectToFtpSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData) throws JSchException, IOException;

    public void writeFile(String path, String remoteDir, ChannelSftp channel);
    /**
     * @Description: 关闭连接
     * @Param:
     * @return:
     * @Author: NoCortY
     * @Date: 2020/3/7
     */
    public void close(Channel channel);
}
