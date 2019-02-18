import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author andy.an
 * @since 2019/2/18
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1024);

        System.out.println("Server is ready!");
        System.out.println(
                "Server info: " + serverSocket.getInetAddress()
                        + ", port: " + serverSocket.getLocalPort());


        // 等待客户端连接
        for (; ; ) {
            Socket client = serverSocket.accept();
            new Thread(new ClientHandler(client)).start();
        }
    }

    private static class ClientHandler implements Runnable {

        private Socket socket;
        private volatile boolean flag = true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println(
                    "New client connecting: " + socket.getInetAddress()
                            + ", port: " + socket.getPort());

            try {
                // 得到打印流，用于数据输出；服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());

                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (flag) {
                    String msg = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(msg)) {
                        flag = false;
                        // 回送
                        socketOutput.println("bye");
                    } else {
                        System.out.println(msg);
                        // 回送
                        socketOutput.println("received " + msg.length());
                    }
                }

                socketInput.close();
                socketOutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Client quit!");
        }
    }
}
