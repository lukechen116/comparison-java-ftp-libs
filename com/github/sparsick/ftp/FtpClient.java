package com.github.sparsick.ftp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FtpClient extends AutoCloseable {
    void authUserPassword(String user, String password);

    void passiveMode(boolean passiveMode);

    void connect(String host, int port) throws IOException;

    void connect(String host) throws IOException;

    void disconnect();

    void download(String remotePath, Path local) throws IOException;

    void upload(Path local, String remotePath) throws IOException;

    void move(String oldRemotePath, String newRemotePath) throws IOException;

    void copy(String oldRemotePath, String newRemotePath) throws IOException;

    void delete(String remotePath) throws IOException;

    boolean fileExists(String remotePath) throws IOException;

    List<String> listChildrenNames(String remotePath) throws IOException;

    List<String> listChildrenFolderNames(String remotePath) throws IOException;

    List<String> listChildrenFileNames(String remotePath) throws IOException;

}