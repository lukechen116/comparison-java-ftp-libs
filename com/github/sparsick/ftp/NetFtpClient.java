package com.github.sparsick.ftp;

import org.apache.commons.net.ftp.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NetFtpClient implements FtpClient {
    private String user;
    private String password;
    private boolean passiveMode = false;
    private FTPClient ftpClient;

    @Override
    public void authUserPassword(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void passiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    @Override
    public void connect(String host, int port) throws IOException {
        ftpClient = new FTPClient();

        ftpClient.connect(host, port);
        if (password != null) {
            ftpClient.login(user, password);
        } else {
            ftpClient.login("Anonymous", "");
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        if (passiveMode) {
            ftpClient.enterLocalPassiveMode();
        } else {
            ftpClient.enterLocalActiveMode();
        }

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect();
            throw new IOException("Ftp lost connect");
        }
    }

    @Override
    public void connect(String host) throws IOException {
        connect(host, 21);
    }

    @Override
    public void disconnect() {
        try {
            if (ftpClient != null) {
                ftpClient.abort();
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception ex) {
            // Ignore because disconnection is quietly
        }
    }

    @Override
    public void download(String remotePath, Path local) throws IOException {
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
//        FTPFile[] remoteFiles = ftpClient.listFiles(remotePath, (file) -> file != null && file.isFile());
//        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(local.toFile()));
        OutputStream outputStream = new FileOutputStream(local.toFile());
        ftpClient.retrieveFile(remotePath, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void upload(Path local, String remotePath) throws IOException {
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
        FileInputStream fileInputStream = new FileInputStream(local.toFile());
        ftpClient.storeFile(remotePath, fileInputStream);
        fileInputStream.close();
    }

    @Override
    public void move(String oldRemotePath, String newRemotePath) throws IOException {
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
        ftpClient.rename(oldRemotePath, newRemotePath);
    }

    @Override
    public void copy(String oldRemotePath, String newRemotePath) throws IOException {
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
    }

    @Override
    public void delete(String remotePath) throws IOException {
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
        ftpClient.deleteFile(remotePath);
    }

    @Override
    public boolean fileExists(String remotePath) throws IOException {
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
        FTPFile[] remoteFiles = ftpClient.listFiles(remotePath);
        if (0 < remoteFiles.length) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> listChildrenNames(String remotePath) throws IOException {
        return listChildrenNamesByFilter(remotePath, null);
    }

    @Override
    public List<String> listChildrenFolderNames(String remotePath) throws IOException {
        return listChildrenNamesByFilter(remotePath, FTPFileFilters.DIRECTORIES);
    }

    @Override
    public List<String> listChildrenFileNames(String remotePath) throws IOException {
        return listChildrenNamesByFilter(remotePath, file -> file != null && file.isFile());
    }

    private List<String> listChildrenNamesByFilter(String remotePath, FTPFileFilter ftpFileFilter) throws
            IOException {
        List<String> children = new ArrayList<>();
        if (!ftpClient.isAvailable()) {
            throw new IOException("Ftp lost connect");
        }
        FTPFile[] ftpFiles = ftpClient.listFiles(remotePath, ftpFileFilter);
        for (FTPFile f : ftpFiles) {
            children.add(f.getName());
        }
        return children;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}