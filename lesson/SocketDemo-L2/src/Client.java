import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author andy.an
 * @since 2019/2/18
 */
public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        // 超时时间
        socket.setSoTimeout(3000);

        // 连接本地，端口1024；超时时间3000ms
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 1024), 3000);

        System.out.println("Server connection established");
        System.out.println("Client info: " + socket.getLocalAddress() + ", port: " + socket.getLocalPort());
        System.out.println("Server info: " + socket.getInetAddress() + ", port: " + socket.getPort());

        send(socket);

        socket.close();
        System.out.println("Client quit");
    }

    private static void send(Socket client) throws Exception {
        InputStream in = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // 得到Socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        // 得到Socket输入流，并转换为BufferedReader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;
        while (flag) {
            String msg = reader.readLine();
            socketPrintStream.println(msg);

            String echo = socketBufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        }

        socketBufferedReader.close();
        socketPrintStream.close();
    }
}
