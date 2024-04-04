package bg.sofia.uni.fmi.mjt.order.server.servermanager;

import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int SERVER_PORT = 1910;
    private static final int MAX_EXECUTOR_THREADS = 10;

    public static void main(String... args) {
        OrderRepository repository = new MJTOrderRepository();
        try (ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS)) {

            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

                Socket clientSocket;

                while (true) {

                    System.out.println("Server started and listening for connect requests");

                    clientSocket = serverSocket.accept();

                    ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, repository);
                    executor.execute(clientHandler);
                }

            } catch (IOException e) {
                throw new RuntimeException("There is a problem with the server socket", e);
            }
        }
    }
}
