package nextstep.jwp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MockSocketWithBody extends Socket {

    private final String request;
    private final List<Byte> bytes = new ArrayList<>();

    public MockSocketWithBody(String request) {
        this.request = request;
    }

    public MockSocketWithBody() {
        this("POST /login.html HTTP/1.1\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: 46\r\n\r\naccount=gugu&password=password&email=hkkang%40woowahan.com\r\n");
    }

    public InetAddress getInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException ignored) {
            return null;
        }
    }

    public int getPort() {
        return 0;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(request.getBytes());
    }

    public OutputStream getOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) {
                bytes.add((byte) b);
            }
        };
    }

    public String output() {
        byte[] converted = toByteArray(bytes);
        return new String(converted, StandardCharsets.UTF_8);
    }

    private byte[] toByteArray(List<Byte> bytes) {
        byte[] byteArray = new byte[bytes.size()];
        int index = 0;
        for (byte b : bytes) {
            byteArray[index++] = b;
        }
        return byteArray;
    }

    public String getRequest() {
        return request;
    }
}