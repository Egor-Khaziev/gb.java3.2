package Server;

import Server.Interface.AuthService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.*;

public class MyServer {

    private final int PORT = 8181;

    private List<ClientHandler> clients;
    private AuthService authService;

    private static Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private Handler myServerLog;



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
                        logger.log(Level.INFO, "censor error IO");
                    }
                    censorList.add(str);
                }
                logger.log(Level.INFO, "censor done");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.log(Level.INFO, "censor error. file not found");
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.INFO, "censor error IO");
            }


        }

    }


    public MyServer() {

        startLog();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            try {
                authService = new BaseAuthService() {
                };
                logger.log(Level.INFO, "SQL done");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                logger.log(Level.INFO, "SQL error");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                logger.log(Level.INFO, "SQL error ClassNotFoundException");
            }


            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Server wait to connection");
                logger.log(Level.INFO, "Server wait to connection");
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                logger.log(Level.INFO, "new client connected");
                new ClientHandler(this, socket);
            }


        } catch (IOException e) {
            System.out.println("server error");
            logger.log(Level.SEVERE, "server error");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    private void startLog() {
        File file;
        int i = 0;
        String path;

        while (true) {
            i++;
            path = "server" + i + ".log";
            file = new File(path);
            if (!file.exists()) {
                break;
            }
        }

        try {
            myServerLog = new FileHandler(path);
            myServerLog.setFormatter(new SimpleFormatter());
            logger.addHandler(myServerLog);
            logger.log(Level.INFO, "Log start");
            Arrays.stream(logger.getHandlers()).forEach(a -> System.out.println(a));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void subscribe(ClientHandler c) {
        clients.add(c);
        logger.log(Level.INFO, "client " + c + " subscribe");
    }

    public void unSubscribe(ClientHandler c) {
        clients.remove(c);
        logger.log(Level.INFO, "client " + c + " unsubscribe");
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getNickname().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadCastMessage(String message) {
        for (ClientHandler c : clients) {
            c.sendMessage(" " + message);
        }
    }

    public synchronized boolean privateMessage(String nickName, String message) {
        for (ClientHandler c : clients) {
            if (c.nickname.equals(nickName)) {
                c.sendMessage(message);
                return true;
            }
        }
        return false;
    }


    public void allList(ClientHandler client) {
        for (ClientHandler c : clients) {
            client.sendMessage(c.nickname);
        }
    }


}
