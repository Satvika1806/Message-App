package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import java.net.*;      //for networking
import java.io.*;       //for transfer of data

public class Server extends JFrame{ 
    JPanel p1;
    JTextField t1;
    static JTextArea a1;  
    static JScrollPane sp;
    JButton send;
    JButton exit; 
    
    private static ServerSocket SS;         
    private static Socket skt;
    private static DataInputStream din;
    private static DataOutputStream dout;
    
    //constructor - GUI 
    Server(){
        
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        setSize(600, 550);  
        setResizable(false);       
        setLocation(25,50);            //to appear SERVER frame at location of 50,100 on our system
        setUndecorated(true);   
        
        p1 = new JPanel();      
        p1.setLayout(null);
        p1.setBackground(new Color(210, 82, 127));     
        p1.setBounds(0,0, 600, 70);
        add(p1);
        
        JLabel l1 = new JLabel("SERVER");       
        l1.setFont(new Font("Verdana", Font.BOLD, 18));
        l1.setForeground(Color.WHITE);
        l1.setBounds(260, 15 , 250, 40); 
        p1.add(l1); 

        exit = new JButton("EXIT");
        exit.setBounds(20, 15, 100, 40);
        exit.setBackground(new Color(204,0,0));
        exit.setForeground(Color.WHITE);
        exit.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        
        exit.setToolTipText("Disconnect from chat");
        exit.setFont(new Font("Verdana", Font.PLAIN, 14));

        p1.add(exit);
        //Exit button functionality
        exit.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent ae){
                System.exit(0); 
             }
         });

        sp = new JScrollPane();
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        

        a1 = new JTextArea();
        a1.setBounds(10, 85, 580, 410);
        a1.setBackground(new Color(228,223,237)); 
        a1.setForeground(Color.BLACK);
        a1.setFont(new Font("Verdana", Font.PLAIN, 14));
        a1.setBorder(new LineBorder(Color.BLACK));
        a1.setEditable(false);      
        a1.setLineWrap(true);
        a1.setWrapStyleWord(true); 
        
        sp.setBounds(10, 85, 580, 410);
        sp.getViewport().setBackground(Color.WHITE);
        sp.getViewport().add(a1);
        add(sp);
        
        
        t1 = new JTextField();    
        t1.setBounds(10, 505, 480, 35);
        t1.setText("Type your message here");
        t1.setForeground(new Color(153,153,153));  
        t1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                t1FocusGained(evt);
            }
            public void focusLost(FocusEvent evt) {
                t1FocusLost(evt);
            } 
        }); 
      
      
        t1.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(t1);
   
        send = new JButton("Send");
        send.setBounds(500, 505, 90, 35);
        send.setBackground(new Color(0,204,0));
        send.setForeground(Color.BLACK);
        send.setFont(new Font("Verdana", Font.PLAIN, 14));
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendActionPerformed(ae);
            }
        });
        add(send);

        setVisible(true);
        
    }
      
    //constructor to put Port number - SOCKET PROGRAMMING
    public Server(int port){
        //to establish socket connection
        try{
             SS = new ServerSocket(port);
            
            //starts as we run Server.java and waits for client to send request
            
             a1.append(" 'Server Started' ");       
            
             a1.append("\n 'Waiting for Client..!!' ");
             
             skt = SS.accept();
             a1.append("\n 'Client request Accepted..Connection Established..!!' ");
            
            //collects input and output stream from client socket
             
            din = new DataInputStream(skt.getInputStream());    
            dout = new DataOutputStream(skt.getOutputStream());
        }
        //IO Exception - when client is already binded 
        catch(Exception e)  
        {
            JOptionPane.showMessageDialog(this, e, "ERROR FOUND", JOptionPane.ERROR_MESSAGE);
        }
            
        String msgin = "";
        //reads message until there is flow of stream
        while(true){
            try{  
                msgin = din.readUTF();
                a1.setText(a1.getText() + "\n CLIENT: " + msgin);    
            } 
            catch(Exception e){}  //null point exception or IOException
        }
    }

    
//Initialising Place holder for TextField by focusevent --> Focuslost => place holder appears ; Focusgained => place holder disappears
    
    private void t1FocusGained(FocusEvent evt){
          if(t1.getText().equals("Type your message here"))
          {
            t1.setText("");   
            t1.setForeground(new Color(0,0,0)); 
          }
    }
    private void t1FocusLost(FocusEvent evt){
          if(t1.getText().equals(""))
          {
            t1.setText("Type your message here");
            t1.setForeground(new Color(153,153,153)); 
          }
    } 
    
//Creating Send functionality by action event on button
    private void sendActionPerformed(ActionEvent ae){
       
        try{
            if(!(t1.getForeground().equals(new Color(153,153,153))))
                    { 
                        String msg = t1.getText();  
                        a1.setText(a1.getText() + "\n ME: " + msg);
                      
                        dout.writeUTF(msg);
                        t1.setText("Type your message here");
                        t1.setForeground(new Color(153, 153, 153));                      
                    }
        } 
    /*When Client is disconnected from chat or when connection is not established and you try to send a text then,
      that text will appear to you on your TextArea but it is not sent to Server
      for this exception a warning dialog box appears */
        catch(Exception e)    
        {
          JOptionPane.showMessageDialog(this, "Connection not found \n Message not sent", "WARNING", JOptionPane.WARNING_MESSAGE);
        }        
    }
    
    
    public static void main(String[] args){
       
        new Server().setVisible(true);  //to make UI visible to user and performs all actions inside the Server() constructor.
        
        Server server = new Server(6040);   //to access server constructor with port number and interact
        //closing all connections
        try {
                din.close();    
                dout.close();
                skt.close(); 
                SS.close();
            } 
        catch(IOException ex) {}
       }
}
       
