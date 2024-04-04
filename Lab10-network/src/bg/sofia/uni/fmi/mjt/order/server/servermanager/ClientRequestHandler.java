package bg.sofia.uni.fmi.mjt.order.server.servermanager;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private final OrderRepository repository;

    private static final int TOKEN_SIZE = 1;
    private static final int TOKEN_COLOR = 2;
    private static final int TOKEN_DESTINATION = 3;

    public ClientRequestHandler(Socket socket, OrderRepository repository) {
        this.repository = repository;
        this.socket = socket;
    }

    private String[] parseRequestLine(String[] tokens) {
        String[] result = new String[tokens.length];
        for (int i = 1; i < tokens.length; i++) {
            String[] tempTokens = tokens[i].split("=");
            result[i] = tempTokens[1];
        }

        return result;
    }

    private Response getResponseForCommand(String command, String[] commandTokens) {
        switch (command) {
            case "request" -> {
                String[] requestTokens = parseRequestLine(commandTokens);
                return repository.request(requestTokens[TOKEN_SIZE], requestTokens[TOKEN_COLOR],
                    requestTokens[TOKEN_DESTINATION]);
            }
            case "get" -> {
                return switch (commandTokens[1]) {
                    case "all" -> repository.getAllOrders();
                    case "all-successful" -> repository.getAllSuccessfulOrders();
                    case "my-order" -> {
                        int id = Integer.parseInt(commandTokens[2].split("=")[1]);
                        yield repository.getOrderById(id);
                    }
                    default -> Response.decline("Unknown command");
                };
            }
            default -> {
                return Response.decline("Unknown command!");
            }
        }
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] tokens = inputLine.split(" ");
                String command = tokens[0];

                if (!command.equals("request") && !command.equals("get")) {
                    out.println("Unknown command");
                    continue;
                }

                Response response = getResponseForCommand(command, tokens);
                System.out.println(response.toString());
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
