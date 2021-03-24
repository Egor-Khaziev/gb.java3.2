package Server;

import Server.Interface.AuthService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyServer {

    private final int PORT = 8181;

    private List<ClientHandler> clients;
    private AuthService authService;





    public AuthService getAuthService() {
        return authService;
    }

    private File censorFile = new File("censored.txt");

    public List<String> getCensorList() {
        return censorList;
    }

    private List<String> censorList = new LinkedList<>();


    {
        if (censorFile.exists() && censorFile.isFile() && censorFile.canRead()) {
            String str = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(censorFile))) {
                while (true) {
                    try {
                        if (!((str = reader.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    censorList.add(str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



    public MyServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            try {
                authService = new BaseAuthService() {
                };
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Server wait to connection");
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                new ClientHandler(this, socket);
            }


        } catch (IOException e) {
            System.out.println("server error");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public void subscribe(ClientHandler c) {
        clients.add(c);
    }

    public void unSubscribe(ClientHandler c) {
        clients.remove(c);
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getNickname().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadCastMessage (String message){
        for (ClientHandler c: clients) {
            c.sendMessage(" " + message);
        }
    }
    public synchronized boolean privateMessage(String nickName, String message) {
        for (ClientHandler c: clients) {
            if (c.nickname.equals(nickName)){
                c.sendMessage(message);
                return true;
            }
        }
        return false;
    }


    public void allList(ClientHandler client) {
        for (ClientHandler c: clients) {
            client.sendMessage(c.nickname);
        }
    }


}
