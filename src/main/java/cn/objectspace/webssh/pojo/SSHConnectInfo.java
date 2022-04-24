package cn.objectspace.webssh.pojo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import org.springframework.web.socket.WebSocketSession;
/**
* @Description: ssh连接信息
* @Author: NoCortY
* @Date: 2020/3/8
*/
public class SSHConnectInfo {
    private WebSocketSession webSocketSession;
    private JSch jSch;
    private Channel channel;

    /**
     * 当前文件夹路径  上传文件用
     */
    private String remotePath = "/root";

    private WebSSHData webSSHData;

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public WebSSHData getWebSSHData() {
        return webSSHData;
    }

    public void setWebSSHData(WebSSHData webSSHData) {
        this.webSSHData = webSSHData;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public JSch getjSch() {
        return jSch;
    }

    public void setjSch(JSch jSch) {
        this.jSch = jSch;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
