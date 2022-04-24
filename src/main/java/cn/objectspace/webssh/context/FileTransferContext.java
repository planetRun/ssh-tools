package cn.objectspace.webssh.context;

import cn.objectspace.webssh.pojo.SSHConnectInfo;
import cn.objectspace.webssh.service.WebSSHService;
import cn.objectspace.webssh.service.WebSftpService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileTransferContext {

    private final WebSSHService webSSHService;

    private final WebSftpService webSftpService;

    public FileTransferContext(WebSSHService webSSHService, WebSftpService webSftpService) {
        this.webSSHService = webSSHService;
        this.webSftpService = webSftpService;
    }

    public void transfer(SSHConnectInfo sshConnectInfo, String type, String command) throws JSchException, IOException {
        switch (type) {
            case "sftp":
                ChannelSftp channel = webSftpService.connectToFtpSSH(sshConnectInfo, sshConnectInfo.getWebSSHData());
                webSftpService.writeFile(command, sshConnectInfo.getRemotePath(), channel);

                break;

            case "ftp":

                break;

            case "ssh":

                break;
        }
    }

}
