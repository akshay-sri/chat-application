package com.project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server extends JFrame implements ActionListener {
    ServerSocket server;
    Socket socket;
    JTextField jText;
    JPanel text;
    Box vertical = Box.createVerticalBox();
    DataInputStream dIn;
    DataOutputStream dOut;
    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server started");
            System.out.println("Waiting...");
            socket = server.accept();//accepts the connection request sent by client
            createGUI();
            startReading();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createGUI(){
        setLayout(null);
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(8,143,143));//background of particular color code
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        add(p1);

        //Close arrow
        ImageIcon a1 = new ImageIcon(ClassLoader.getSystemResource("Images/arrow.png"));
        Image a2 = a1.getImage().getScaledInstance(25,45,Image.SCALE_DEFAULT);
        ImageIcon a3 = new ImageIcon(a2);
        JLabel arrow = new JLabel(a3);
        arrow.setBounds(5,15,25,45);
        p1.add(arrow);
        arrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                System.exit(0);
            }
        });

        //profile icon
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Images/he.png"));
        Image i2 = i1.getImage().getScaledInstance(35,45,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel profile = new JLabel(i3);
        profile.setBounds(40,15,35,45);
        p1.add(profile);

        //video call icon
        ImageIcon v1 = new ImageIcon(ClassLoader.getSystemResource("Images/vc.png"));
        Image v2 = v1.getImage().getScaledInstance(35,45,Image.SCALE_DEFAULT);
        ImageIcon v3 = new ImageIcon(v2);
        JLabel video = new JLabel(v3);
        video.setBounds(325,25,20,15);
        p1.add(video);

        // audio call icon
        ImageIcon c1 = new ImageIcon(ClassLoader.getSystemResource("Images/call.png"));
        Image c2 = c1.getImage().getScaledInstance(35,45,Image.SCALE_DEFAULT);
        ImageIcon c3 = new ImageIcon(c2);
        JLabel call = new JLabel(c3);
        call.setBounds(345,23,65,20);
        p1.add(call);

        // dots icon
        ImageIcon d1 = new ImageIcon(ClassLoader.getSystemResource("Images/dots.png"));
        Image d2 = d1.getImage().getScaledInstance(35,45,Image.SCALE_DEFAULT);
        ImageIcon d3 = new ImageIcon(d2);
        JLabel dot = new JLabel(d3);
        dot.setBounds(370,17,80,30);
        p1.add(dot);

        //name
        JLabel name = new JLabel("Server");
        name.setBounds(110,20,100,18);
        name.setForeground(Color.white);//text color
        name.setFont(new Font("SAN_SERIF",Font.BOLD,18));
        p1.add(name);

        //Panel for text
        text = new JPanel();
        text.setBounds(5,75,440,500);
        add(text);
        //text box
        jText = new JTextField();
        jText.setBounds(5,580,330,40);
        jText.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        add(jText);

        //send button
        JButton send = new JButton("Send");
        send.setBounds(345,580,100,40);
        send.setBackground(new Color(8,143,143));
        send.setForeground(Color.white);
        send.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        send.addActionListener(this);
        add(send);

        //configuration of frame
        setSize(450,700);//size of the frame
        setLocation(200,50);
        setUndecorated(true);//removes the border window
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String sendMsg = jText.getText();
            if(!sendMsg.isEmpty()) { //it will not allow empty msg to be sent
                JPanel jPanel = formatLabel(sendMsg);
                text.setLayout(new BorderLayout());

                //messages are alligned to right side
                JPanel right = new JPanel(new BorderLayout());
                right.add(jPanel, BorderLayout.LINE_END);
                vertical.add(right);// line break after every msg
                vertical.add(Box.createVerticalStrut(15));//space between every message
                text.add(vertical, BorderLayout.PAGE_START);
                dOut.writeUTF(sendMsg);//for sending the msg
                jText.setText("");//empty the textbox

                // helps to send the msg
                repaint();
                invalidate();
                validate();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public JPanel formatLabel(String sendMsg){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JLabel output = new JLabel(sendMsg);
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(81,166,166));
        output.setOpaque(true);//to enable background color of text
        output.setBorder(new EmptyBorder(15,15,15,50));
        panel.add(output);

        //Time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);
        return panel;
    }
    private void startReading() {
            System.out.println("Reading started");
            try {
                while (!socket.isClosed()) {
                    dIn = new DataInputStream(socket.getInputStream());
                    dOut = new DataOutputStream(socket.getOutputStream());
                    String msg = dIn.readUTF();
                    JPanel panel = formatLabel(msg);
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel,BorderLayout.LINE_START);
                    vertical.add(left);
                    validate();
                }
            }
            catch (Exception e){
                System.out.println("Connection closed");
            }
    }

    public static void main(String[] args) {
        System.out.println("This is Server...");
        new Server();
    }
}
