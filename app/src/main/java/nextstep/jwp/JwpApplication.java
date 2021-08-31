package nextstep.jwp;

import nextstep.jwp.http.WebServer;

public class JwpApplication {

    public static void main(String[] args) {
        int port = WebServer.defaultPortIfNull(args);
        final WebServer webServer = new WebServer(port);
        webServer.run();
    }
}