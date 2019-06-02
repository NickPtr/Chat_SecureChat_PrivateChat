
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.net.ssl.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewJFrame extends javax.swing.JFrame {

    Boolean Connected = false, ConnectedTLS = false, ConnectPC1 = false, ConnectPC2 = false, DFH = false;
    String username = " ", ServerIP = "83.212.97.199", MyPCiP;
    int Port = 5000;
    Socket socket, sock, prox;
    Socket so;
    SSLSocket s;
    ServerSocket server;
    BufferedReader reader;
    PrintWriter writer;
    ArrayList<String> userList = new ArrayList();
    String[] data, changeddata, dataPC, PCspliter;
    int flag = 0, ok = 0, User = 0;
    DH serv;
    DH clien;

    public NewJFrame() {
        initComponents();
    }

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String[] dataout;
            String stream, ban = "BAN", TLS = "TLS", connect = "CONNECT", disconnect = "DISCONNECT", chat = "MESSAGE", change = "CHANGE", pc = "PC";

            try {
                while (true) {
                    stream = reader.readLine();
                    System.out.println(stream);
                    dataout = stream.split(":");

                    if (dataout[0].equals(chat)) {
                        OutputArea.append(dataout[1] + ": " + dataout[2] + "\n");
                        OutputArea.setCaretPosition(OutputArea.getDocument().getLength());
                        if (!(userList.contains(dataout[1]))) {
                            userList.add(dataout[1]);
                        }
                        UsersList.setText(" ");
                        for (int i = 0; i < userList.size(); i++) {
                            UsersList.append(userList.get(i) + "\n");
                        }
                    } else if (dataout[0].equals(connect)) {
                        userAdd(dataout[1]);
                        System.out.println("NAIIIIIIIII");
                    } else if (dataout[0].equals(disconnect)) {

                        userRemove(dataout[1]);
                        userList.remove(dataout[1]);
                    } else if (dataout[0].equals(change)) {
                        OutputArea.append("Username Changed!\n");
                        UsernameField.setText(dataout[1]);
                        username = dataout[1];
                        userAdd(dataout[1]);
                    } else if (dataout[0].equals(ban)) {
                        OutputArea.append(dataout[1] + ": " + dataout[2] + "\n");
                    } else if (dataout[0].equals(TLS)) {
                        OutputArea.append(dataout[0] + " OK\n");
                    } else if (dataout[0].equals(pc)) {
                        System.out.println("NAi");
                        if (dataout[2].equals(username) && ok == 0) {
                            UsernameForPrivChat.setText(dataout[1]);
                            UsernameForPrivChat.setEditable(false);
                            ConnectPC1 = true;
                            String[] sp = dataout[4].split("/");
                            MyPCiP = sp[1];
                            System.out.println("etsi mpravo");
                        } else if (dataout[1].equals(username)) {
                            UsernameForPrivChat.setText(dataout[2]);
                            UsernameForPrivChat.setEditable(false);
                            String[] sp = dataout[3].split("/");
                            MyPCiP = sp[1];
                            ConnectPC2 = true;
                            System.out.println("mpravo");
                        }
                        System.out.println(MyPCiP);
                    }

                }
            } catch (Exception ex) {
            }
        }
    }

    public void ListenThread() {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    public void userAdd(String data) {
        userList.add(data);
    }

    public void userRemove(String data) {

        OutputArea.append(data + " has disconnected.\n");
    }

    public void writeUsers() {
        String[] tempList = new String[(userList.size())];
        userList.toArray(tempList);
        for (String token : tempList) {

            UsersList.append(token + "\n");

        }

    }

    public void sendDisconnect() {

        String bye = ("DISCONNECT:" + data[1]);
        try {
            writer.println(bye);
            writer.flush();
        } catch (Exception e) {
            OutputArea.append("Could not send Disconnect message.\n");
        }

    }

    public void Disconnect() {

        try {

            OutputArea.append("Disconnected.\n");

            UsernameField.setText("");

            if (flag == 0) {
                sock.close();
            } else if (flag == 1) {
                s.close();
            } else if (flag == 2) {
                so.close();
            }
            ok = 0;

        } catch (Exception ex) {
            System.out.println(ex);
            OutputArea.append("Failed to disconnect. \n");
        }
        DFH = false;
        Connected = false;
        ConnectedTLS = false;
        ConnectPC1 = false;
        ConnectPC2 = false;
        UsernameForPrivChat.setText(" ");
        UsernameForPrivChat.setEditable(true);
        userList.remove(data[1]);
        UsersList.setText("");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ConnectButton = new javax.swing.JButton();
        DisconnectButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        UsernameField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        OutputArea = new javax.swing.JTextArea();
        TextArea = new javax.swing.JTextField();
        SendButton = new javax.swing.JButton();
        UsernameChangeButton = new javax.swing.JButton();
        SecureConnect = new javax.swing.JButton();
        TorConnect = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        PrivateChannel = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        UsersList = new javax.swing.JTextArea();
        UsernameForPrivChat = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        OKforPrivChat = new javax.swing.JButton();
        FileBotton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ConnectButton.setText("Connect");
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectButtonActionPerformed(evt);
            }
        });

        DisconnectButton.setText("Disconnect");
        DisconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisconnectButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Username:");

        UsernameField.setEditable(false);

        OutputArea.setEditable(false);
        OutputArea.setColumns(20);
        OutputArea.setLineWrap(true);
        OutputArea.setRows(5);
        jScrollPane1.setViewportView(OutputArea);

        SendButton.setText("Send");
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });

        UsernameChangeButton.setText("Change Username");
        UsernameChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameChangeButtonActionPerformed(evt);
            }
        });

        SecureConnect.setText("Secure Connect");
        SecureConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SecureConnectActionPerformed(evt);
            }
        });

        TorConnect.setText("Tor Connect");
        TorConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TorConnectActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Online Users");

        PrivateChannel.setText("Private Channel");
        PrivateChannel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrivateChannelActionPerformed(evt);
            }
        });

        UsersList.setEditable(false);
        UsersList.setColumns(20);
        UsersList.setRows(5);
        jScrollPane2.setViewportView(UsersList);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("UserName for Private Chat");

        OKforPrivChat.setText("OK");
        OKforPrivChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKforPrivChatActionPerformed(evt);
            }
        });

        FileBotton.setText("File");
        FileBotton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileBottonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(UsernameChangeButton)
                                .addGap(18, 18, 18)
                                .addComponent(ConnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(DisconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SecureConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PrivateChannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)
                            .addComponent(TorConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UsernameForPrivChat)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OKforPrivChat))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SendButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FileBotton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(271, 271, 271)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DisconnectButton)
                    .addComponent(ConnectButton)
                    .addComponent(UsernameChangeButton)
                    .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TextArea, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(FileBotton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TorConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SecureConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PrivateChannel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UsernameForPrivChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OKforPrivChat))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectButtonActionPerformed
        // TODO add your handling code here:
        if (Connected == false) {
            try {

                socket = new Socket(ServerIP, Port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("CONNECT");
                socket.close();
                System.out.println("SOCKET1 " + socket);
                sock = new Socket(ServerIP, 5001);
                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                writer = new PrintWriter(sock.getOutputStream(), true);
                System.out.println("SOCK " + sock);
                String line = reader.readLine();

                data = line.split(":");
                System.out.println("SOCK " + sock);
                if (data[0].equals("USERNAME")) {
                    System.out.println("MPIKE");
                    OutputArea.append("Connected!\n");
                    writer.println("MESSAGE:" + username + ":" + "CLEAN");
                    flag = 0;
                    UsernameField.setText(data[1]);
                    username = data[1];
                    Connected = true;
                }

            } catch (IOException ex) {
                System.out.println(ex);
                OutputArea.append("Cannot Connect! Try Again. \n");
            }
            ListenThread();
        } else if (Connected == true) {
            OutputArea.append("You are already connected. \n");
        }
    }//GEN-LAST:event_ConnectButtonActionPerformed

    private void DisconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisconnectButtonActionPerformed
        // TODO add your handling code here:
        sendDisconnect();
        Disconnect();

    }//GEN-LAST:event_DisconnectButtonActionPerformed

    private void UsernameChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameChangeButtonActionPerformed
        // TODO add your handling code here:
        try {
            String Change_UN = ("CHANGE:" + data[1]);
            System.out.println("PRIN  " + data[1]);
            userList.remove(data[1]);
            System.out.println(data[1]);
            writer.println(Change_UN);
            writer.flush();
        } catch (Exception e) {
            OutputArea.append("Could not change username.\n");
        }

    }//GEN-LAST:event_UsernameChangeButtonActionPerformed

    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
        // TODO add your handling code here:
        String nothing = "";
        if ((TextArea.getText()).equals(nothing)) {
            TextArea.setText("");
            TextArea.requestFocus();
        } else {
            try {

                writer.println("MESSAGE:" + username + ":" + TextArea.getText());
                writer.flush(); // flushes the buffer

            } catch (Exception ex) {
                System.out.println(ex);
                OutputArea.append("Message was not sent. \n");
            }
            TextArea.setText("");
            TextArea.requestFocus();
        }

        TextArea.setText("");
        TextArea.requestFocus();
    }//GEN-LAST:event_SendButtonActionPerformed

    private void SecureConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SecureConnectActionPerformed
        // TODO add your handling code here:
        if (ConnectedTLS == false) {

            try {
                socket = new Socket(ServerIP, Port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("TLS");
                //System.out.println("Estalei TLS flag");
                socket.close();
                System.out.println("SOCKET2 " + socket);
                //load client private key
                KeyStore clientKeys = KeyStore.getInstance("JKS");
                clientKeys.load(new FileInputStream("plainclient.jks"), "password".toCharArray());
                KeyManagerFactory clientKeyManager = KeyManagerFactory.getInstance("SunX509");
                clientKeyManager.init(clientKeys, "password".toCharArray());
                //load server public key
                KeyStore serverPub = KeyStore.getInstance("JKS");
                serverPub.load(new FileInputStream("serverpub.jks"), "password".toCharArray());
                TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
                trustManager.init(serverPub);
                //use keys to create SSLSoket
                SSLContext ssl = SSLContext.getInstance("TLS");
                ssl.init(clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));
                s = (SSLSocket) ssl.getSocketFactory().createSocket(ServerIP, 5002);
                System.out.println("S " + s);
                s.startHandshake();
                //receive data
                System.out.println("EGINE");
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                writer = new PrintWriter(s.getOutputStream(), true);
                String line = reader.readLine();
                data = line.split(":");
                if (data[0].equals("USERNAME")) {
                    OutputArea.append("Connected in Secure Channel!\n");
                    flag = 1;
                    //writer.println("TLS");
                    UsernameField.setText(data[1]);
                    username = data[1];
                    ConnectedTLS = true;
                    writer.println("MESSAGE:" + username + ":" + "CLEAN");
                }

            } catch (Exception e) {
                System.out.println(e);
                OutputArea.append("Cannot Connect! Try Again. \n");
            } finally {
                ListenThread();
                System.out.println("OK");
            }
        } else if (ConnectedTLS == true) {
            OutputArea.append("You are already connected. \n");
        }
    }//GEN-LAST:event_SecureConnectActionPerformed

    private void TorConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TorConnectActionPerformed
        // TODO add your handling code here:
      
                    try {
                        socket = new Socket(ServerIP, Port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("CONNECT");
                socket.close();
                System.out.println("SOCKET1 " + socket);
            SocketAddress sockAddr = new InetSocketAddress("127.0.0.1", 9150);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, sockAddr);
            URL url = new URL("https://api.ipify.org/");
            url.openConnection(proxy);
            InputStream in = url.openConnection(proxy).getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] stuff = new byte[1024];
            int readBytes = 0;
            while ((readBytes = in.read(stuff)) > 0) {
                bout.write(stuff, 0, readBytes);
            }
            byte[] result = bout.toByteArray();

            System.out.println(new String(result));
            //s = (SSLSocket) ssl.getSocketFactory().createSocket(socket,"127.0.0.1", 9150,true);
                
                prox = new Socket(proxy);
                prox.connect(new InetSocketAddress(ServerIP,5001));
                //prox.connect(new InetAddress (ServerIP,Port));
                System.out.println(prox);
                reader = new BufferedReader(new InputStreamReader(prox.getInputStream()));
                writer = new PrintWriter(prox.getOutputStream(), true);
                System.out.println("PROX " + prox);
                String line = reader.readLine();

                data = line.split(":");
                System.out.println("Prox " + prox);
                if (data[0].equals("USERNAME")) {
                    System.out.println("MPIKE");
                    OutputArea.append("Connected!\n");
                    writer.println("MESSAGE:" + username + ":" + "CLEAN");
                    flag = 0;
                    UsernameField.setText(data[1]);
                    username = data[1];
                    //Connected = true;
                }
           
        } catch (Exception e) {
            System.out.println(e);
            OutputArea.append("Cannot Connect with Tor! Try Again. \n");
        }
         ListenThread();       
        
    }//GEN-LAST:event_TorConnectActionPerformed

    private void PrivateChannelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrivateChannelActionPerformed
        // TODO add your handling code here:

        if (ConnectPC1 == true || ConnectPC2 == true) {

            DFH = true;
            try {
                if (flag == 0) {
                    sock.close();
                } else if (flag == 1) {
                    s.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
            flag = 2;
            NewJFrame1 newJF = new NewJFrame1();
            newJF.setVisible(true);
            newJF.setget_username(username);
            newJF.setget_IP(MyPCiP);

        } else {
            OutputArea.append("You have not select anyone for Private Channel\n");
        }

    }//GEN-LAST:event_PrivateChannelActionPerformed

    private void OKforPrivChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKforPrivChatActionPerformed
        // TODO add your handling code here:
        String nothing = " ";
        if ((UsernameForPrivChat.getText()).equals(nothing)) {
            UsernameForPrivChat.setText("");
            UsernameForPrivChat.requestFocus();
        } else {
            try {

                writer.println("PC:" + username + ":" + UsernameForPrivChat.getText());
                System.out.println("EGINE");
                ok = 1;

                User = 1;

                writer.flush();
            } catch (Exception ex) {
                OutputArea.append("User not found. \n");
            }
            UsernameForPrivChat.setText("");
            UsernameForPrivChat.requestFocus();
        }
        UsernameForPrivChat.setText("");
        UsernameForPrivChat.requestFocus();

    }//GEN-LAST:event_OKforPrivChatActionPerformed

    private void FileBottonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileBottonActionPerformed
        // TODO add your handling code here:
        try {
            writer.println("FILE");
            DataOutputStream dos = null;
            DataInputStream dis = null;
            if (flag == 0) {
                dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
                dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            } else if (flag == 1) {
                dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
                dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            }

            File file = new File(TextArea.getText());
            dos.writeUTF(file.getName());
            dos.flush();
            int n = 0;
            System.out.println("Sending File");
            byte[] done = new byte[3];
            byte[] buffer = new byte[4092];
            String str = "done";  //randomly anything
            done = str.getBytes();
            FileInputStream fis = new FileInputStream(file);

            while ((n = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, n);

                dos.flush();
            }

            dos.write(done, 0, 3);
            dos.flush();
            OutputArea.append("File Uploaded Successfully to server");

            dos.close();

        } catch (IOException ex) {
            System.out.println(ex);
        }


    }//GEN-LAST:event_FileBottonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
                //new NewJFrame().setVisible(true);
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConnectButton;
    private javax.swing.JButton DisconnectButton;
    private javax.swing.JButton FileBotton;
    private javax.swing.JButton OKforPrivChat;
    private javax.swing.JTextArea OutputArea;
    private javax.swing.JButton PrivateChannel;
    private javax.swing.JButton SecureConnect;
    private javax.swing.JButton SendButton;
    private javax.swing.JTextField TextArea;
    private javax.swing.JButton TorConnect;
    private javax.swing.JButton UsernameChangeButton;
    private javax.swing.JTextField UsernameField;
    private javax.swing.JTextField UsernameForPrivChat;
    private javax.swing.JTextArea UsersList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
