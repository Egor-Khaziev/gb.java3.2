package Server;

import Server.Interface.Censor;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class ClientHandler implements Censor {

    private MyServer myServer;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private volatile boolean close = false;


    private ExecutorService executorService;


    private int connect_timeout_sec = 120;

    private String str;

    private volatile boolean isAuthentication = false;



    String nickname;



    public ClientHandler(MyServer myServer, Socket socket) {


        executorService = Executors.newSingleThreadExecutor();



        try {
            this.myServer = myServer;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.socket = socket;
            this.nickname = "";

            Thread process = new Thread( () -> {
                try {
                    authentication();
                    if (!close) {
                        readMessages();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    closeConnection();
                }

            });


            executorService.execute(process);
//            process.start();


        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }

        executorService.shutdown();

    }

    private void closeConnection() {
        System.out.println("CLOSE CONNECTION");
        if (!close) {
            myServer.unSubscribe(this);
            myServer.broadCastMessage(nickname + " вышел из чата");
        }
        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    public String censored(String strFromClient) throws IOException {

        for (String str: myServer.getCensorList()) {
            String newStr[] = str.split("->");
            if (strFromClient.contains(newStr[0])){
                strFromClient = strFromClient.replace(newStr[0], newStr[1]);
            }
        }
        return strFromClient;
    }

    private void readMessages() throws IOException, SQLException {
        while (true) {
            String strFromClient = dis.readUTF();

            strFromClient = censored(strFromClient);

            if (strFromClient.startsWith("/")) {

                if (strFromClient.equals("/quit")) {
                    return;
                }

                if (strFromClient.startsWith("/rn")) {
                    String[] part = strFromClient.substring(2).split("\\s", 2);
                    if (part.length == 2 && part[1] != null && part[1].length() >= 3) {
                        BaseAuthService authService = (BaseAuthService) myServer.getAuthService();
                        authService.renameUser(nickname, part[1]);
                        myServer.broadCastMessage(nickname + " сменил имя на " + part[1]);
                        myServer.unSubscribe(this);
                        nickname = part[1];
                        sendMessage("/renameok " + nickname);
                        myServer.subscribe(this);
                    } else if (part.length == 2 && part[1] != null && part[1].length() < 3) {
                        sendMessage("need 3 or more symbol for name");
                    } else System.out.println("wrong command");

                }


                if (strFromClient.equals("/all")) {
                    allLst();
                    continue;
                }

                if (strFromClient.startsWith("/w")) {
                    String[] part = strFromClient.substring(2).split("\\s", 3);

                    if (part.length == 3) {
                        if (myServer.privateMessage(part[1], nickname + " private massage: " + part[2])) {
                        } else {
                            sendMessage("пользователь не в сети");
                        }

                    } else {
                        sendMessage("ошибка в приватном сообщении");
                    }
                    continue;
                }
                continue;
            }
            myServer.broadCastMessage(nickname + ": " + strFromClient);

        }
    }

    private void allLst() {
        myServer.allList(this);
    }

    private void authentication() throws IOException {


        Thread t = new Thread(() -> {
            while (true) {

                try {
                    str = dis.readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (close) break;
                if (str.startsWith("/auth")) {
                    String[] parts = str.split("\\s");

                    Arrays.stream(parts).forEach(a -> System.out.println(a));

                    if (parts.length < 3) {
                        sendMessage("Ошибка ввода логин/пароль");
                        continue;
                    }

                    String nick = null;
                    try {
                        nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (nick != null) {
                        if (!myServer.isNickBusy(nick)) {
                            sendMessage("/authok " + nick);
                            nickname = nick;
                            myServer.broadCastMessage(nickname + " зашел в чат");
                            myServer.subscribe(this);
                            isAuthentication = true;

                            return;
                        } else {
                            sendMessage("Учетная запись уже используется");
                        }
                    } else {
                        sendMessage("Неверные логин/пароль");
                    }
                }
            }
        });
//        executorService.execute(t);
        t.start();


        try {
            for (int i = 0; i < (connect_timeout_sec * 4); i++) {
                sleep(250);
                if (isAuthentication) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!isAuthentication) {
            System.out.println("client does not connected");
            close = true;
            t.stop();
            return;
        }


    }

    public String getNickname() {
        return nickname;
    }

    public void sendMessage(String message) {

        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
