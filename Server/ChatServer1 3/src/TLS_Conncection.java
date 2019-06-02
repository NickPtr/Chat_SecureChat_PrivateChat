
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Certificate;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class TLS_Conncection extends Thread {

    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out, out2;
    Main main1 = new Main();
   
    ArrayList<String> blacklist = new ArrayList();
    String[] temp;

    public TLS_Conncection(Socket socket) {
        this.socket = socket;

    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {

                String user_ip = socket.getRemoteSocketAddress().toString();
                temp = user_ip.split(":");
                // System.out.println(ban_sec.get(0));
                if (main1.ban_sec.contains(temp[0])) {
                    System.out.println("BANNED IP LOGIN ATTEMPT");
                    socket.close();
                    break;
                }

                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                main1.get_ban().put(cal, temp[0]);

                for (Calendar time : main1.get_ban().keySet()) {
                    String ip = main1.get_ban().get(time);
                    System.out.println(dateFormat.format(time.getTime()) + ": " + ip);
                }
                System.out.println("Connected");
                String fname = "guest";
                Random rand = new Random();
                int guestnumber = rand.nextInt(50) + 1;
                StringBuilder sb = new StringBuilder();
                sb.append(fname);
                sb.append(guestnumber);
                String name = sb.toString();
                System.out.println(name);
                synchronized (main1.get_names()) {
                    if (!main1.get_names().contains(name)) {
                        main1.get_names().add(name);

                        out.println("USERNAME" + ":" + name);
                        main1.get_online().put(name, temp[0]);
                        String h = in.readLine();
                        System.out.println(h);
                        

                        break;
                    }

                }

            }
            main1.get_users().add(out);
            while (true) {
                
                String input = in.readLine();
                System.out.println(input);
                String data[] = input.split(":");
                if (input.startsWith("DISCONNECT")) {
                     socket.close();
                    System.out.println(data[1] + " Disconnected");
                    main1.get_names().remove(name);
                   
                    break;
                
                }else if(input.startsWith("PC"))
                {
                   
                    System.out.println("Private Request");
                    String hh=null;
                    String hh2=null;
                    for (String key : main1.get_online().keySet()) {
                        System.out.println(key);
                        hh2 = main1.get_online().get(data[2]);
                       
                        hh = main1.get_online().get(data[1]);
                        System.out.println(hh);
                        System.out.println(hh2);
                        }
                        System.out.println("Private Request");
                        
                        for (PrintWriter writer : main1.get_users()) {
                             writer.println("PC" + ":" + data[1] + ":" + data[2]+":"+"ts"+hh2+":"+"ts"+hh);
                            out.println("PC" + ":" + data[1] + ":" + data[2]+":"+"ts"+hh2+":"+"ts"+hh);
                        }
                }else if (input.startsWith("MESSAGE")) {
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : main1.get_users()) {
                        System.out.println("Printing Message");
                        String[] checksum = data[2].split(" ");
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < checksum.length; i++) {
                            int num = 0;
                            if (main1.get_flame().contains(checksum[i])) {
                                checksum[i] = "****";
                                sb.append(checksum[i]);
                                blacklist.add(data[1]);
                                for (String key : main1.get_online().keySet()) {
                                    key = main1.get_online().get(data[1]);
                                    blacklist.add(key);
                                    num = ban(key);
                                    System.out.println("ban");
                                }
                                if (num == 4) {
                                    if (blacklist.contains(temp[0])) {
                                        main1.ban_sec.add(temp[0]);
                                        writer.println("BAN" + ":" + data[1] + ":" + "You are banned");
                                        socket.close();
                                        System.out.println("PERMA BAN" + temp[0]);
                                    }

                                }

                            } else {
                                sb.append(checksum[i]);
                            }
                            sb.append(" ");
                        }
                        data[2] = sb.toString();
                        writer.println("MESSAGE" + ":" + data[1] + ":" + data[2]);
                    }
                } else if (input.startsWith("CHANGE")) {
                    main1.get_names().remove(data[1]);
                    String fname = "guest";
                    Random rand = new Random();
                    int guestnumber = rand.nextInt(50) + 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append(fname);
                    sb.append(guestnumber);
                    String name = sb.toString();
                    System.out.println("Change Request");
                    out.println("CHANGE" + ":" + name);
                    main1.get_names().add(name);

                } else if(input.startsWith("PRIVATE"))
                {
                    System.out.println("PRIVATE" + ":"+data[1]+":"+data[2]);
                }  else if (input.startsWith("FILE")) {
                   
                    try {
                       Socket s = new Socket();
                       //s=socket;
                     
                        int n = 0;
                        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                        System.out.println("theio");
                        File file = new File(in.readUTF());
                        byte[] buffer = new byte[4092];
                        System.out.println("Start Recieving file" + file.getName());
                        FileOutputStream fos = new FileOutputStream("/RecievedFiles/" + file.getName());
                        while ((n = in.read(buffer)) != -1 && n!=3) {
                            fos.write(buffer, 0, n);
                            fos.flush();
                        }
                        fos.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

            }

        } catch (IOException e) {
        
        } finally {
            if (name != null) {
                main1.get_names().remove(name);
            }
            if (out != null) {
                main1.get_users().remove(out);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }

        }
    }

    public int ban(String key) {
        int counter = 0;
        for (int i = 0; i < blacklist.size(); i++) {
            if (blacklist.get(i).equals(key)) {
                counter++;
            }
        }
        return counter;
    }

    
}
