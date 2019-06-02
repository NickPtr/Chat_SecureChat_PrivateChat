
import java.io.PrintWriter;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author oem
 */
public class Main {

    //private static final int PORT = 5000;
    static int bindPort = 25;
    public static HashSet<String> names = new HashSet<String>();
    public static HashSet<PrintWriter> users = new HashSet<PrintWriter>();
    public static HashMap<Calendar, String> ban = new HashMap<Calendar, String>();
    public static ArrayList<String> offensive = new ArrayList<>();
    public static HashMap<String, String> toban = new HashMap();
    public static ArrayList<String> ban_sec = new ArrayList();

    public static void main(String[] args) throws Exception {
        InetAddress serverIP = InetAddress.getLocalHost();
        add_flame();
        System.out.println("Server Running");
        System.out.println("Entrance Port:" + 5000);
        System.out.println("Server IP:" + serverIP.getHostAddress());
        ServerSocket serverSocket = new ServerSocket(5000);
        ServerSocket listener = new ServerSocket(5001);
        listener.setReuseAddress(true);
        SSLServerSocket serverSock;

        //PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        //load server private key
        KeyStore serverKeys = KeyStore.getInstance("JKS");
        serverKeys.load(new FileInputStream("plainserver.jks"), "password".toCharArray());
        KeyManagerFactory serverKeyManager = KeyManagerFactory.getInstance("SunX509");
        System.out.println(KeyManagerFactory.getDefaultAlgorithm());
        System.out.println(serverKeyManager.getProvider());
        serverKeyManager.init(serverKeys, "password".toCharArray());
        //load client public key
        KeyStore clientPub = KeyStore.getInstance("JKS");
        clientPub.load(new FileInputStream("clientpub.jks"), "password".toCharArray());
        TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
        trustManager.init(clientPub);
        //use keys to create SSLSoket

        SSLContext ssl = SSLContext.getInstance("TLS");
        System.out.println("fdf");
        ssl.init(serverKeyManager.getKeyManagers(), trustManager.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));

        serverSock = (SSLServerSocket) ssl.getServerSocketFactory().createServerSocket(5002);

        serverSock.setNeedClientAuth(true);

        while (true) {

            Socket sock = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String y = in.readLine();
            //System.out.println(y + "dsdsdsdsdsdsdsdsdsdsdsdsdsdsds");

            try {
                if (y.startsWith("TLS")) {
                    System.out.println("TLS REQUEST");
                    new TLS_Conncection((SSLSocket) serverSock.accept()).start();
                } else if (y.startsWith("CONNECT")) {
                    new Handler(listener.accept()).start();
                }

            } catch (Exception e) {
            }
            sock.close();
        }

    }

    public HashSet<String> get_names() {
        return names;
    }

    public HashSet<PrintWriter> get_users() {
        return users;
    }

    public ArrayList<String> get_flame() {
        return offensive;
    }

    public static void add_flame() {
        offensive.add("malaka");
        offensive.add("pousti");
        offensive.add("pwsta");
        offensive.add("gamw");
    }

    public HashMap<Calendar, String> get_ban() {
        return ban;
    }

    public HashMap<String, String> get_online() {
        return toban;
    }

    public ArrayList<String> get_bansec() {
        return ban_sec;
    }

}
