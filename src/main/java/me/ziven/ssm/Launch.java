package me.ziven.ssm;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

/**
 * Created by ziven on 2017/6/29.
 */
public class Launch {
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_CONTEXT_PATH = "/";
    private static final String DEFAULT_APP_CONTEXT_PATH = "./src/main/webapp";


    public static void main(String[] args) {
        runJettyServer(DEFAULT_PORT, DEFAULT_CONTEXT_PATH);
    }

    private static void runJettyServer(int port, String contextPath) {

        Server server = createDevServer(port, contextPath);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static Server createJettyServer(int port, String contextPath) {
        Server server = new Server(port);
        server.setStopAtShutdown(true);

        ProtectionDomain protectionDomain = Launch.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        String warFile = location.toExternalForm();

        WebAppContext context = new WebAppContext(warFile, contextPath);
        context.setServer(server);

        // 设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
        String currentDir = new File(location.getPath()).getParent();
        File workDir = new File(currentDir, "work");
        context.setTempDirectory(workDir);
        server.setHandler(context);
        return server;

    }

    private static Server createDevServer(int port, String contextPath) {
        Server server = new Server(port);
        server.setStopAtShutdown(true);
        // 设置web资源根路径以及访问web的根路径
        WebAppContext webAppCtx = new WebAppContext(DEFAULT_APP_CONTEXT_PATH, contextPath);
        webAppCtx.setServer(server);
        webAppCtx.setDescriptor(DEFAULT_APP_CONTEXT_PATH + "/WEB-INF/web.xml");
        webAppCtx.setResourceBase(DEFAULT_APP_CONTEXT_PATH);
        webAppCtx.setClassLoader(Thread.currentThread().getContextClassLoader());
        server.setHandler(webAppCtx);
        return server;
    }

}