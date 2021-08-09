package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import java.net.*;
import java.io.*;

public class Client extends JFrame{
    
    JPanel p1;
    static JTextField tf;
    JTextField t1;
    static JTextArea a1;
    static JScrollPane sp;
    JButton send;
    JButton exit;  
   
    private static Socket skt; 
    private static DataInputStream din;
    private static DataOutputStream dout;
    
    //constructor - GUI 
    public Client()
    {
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(Color.BLACK);        
        setSize(700, 550);   
        setResizable(false);
        setLocation(600,50);    //to appear this CLIENT frame at a location of 600,50 on our system
        setUndecorated(true);      //minimise and drag options to be available to user   
        setLayout(null); 
        
        p1 = new JPanel();     
        p1.setLayout(null);
        p1.setBackground(new Color(145, 61, 136)); 
        p1.setBounds(0,0, 700, 70);
        add(p1);
         
        JLabel l1 = new JLabel("CLIENT");      
        l1.setFont(new Font("Verdana", Font.BOLD, 18));
        l1.setForeground(Color.WHITE);
        l1.setBounds(300, 15 , 250, 40); 
        p1.add(l1); 
        
        exit = new JButton("EXIT");
        exit.setBounds(600, 15, 80, 40);
        exit.setBackground(new Color(204,0,0));
        exit.setForeground(Color.WHITE);
        exit.setToolTipText("Disconnect from chat");
        exit.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        exit.setFont(new Font("Verdana", Font.PLAIN, 14));
        p1.add(exit);
        //exit button functionality
        exit.addActionListener(new ActionListener(){        
             public void actionPerformed(ActionEvent ae){
                System.exit(0);
             }
         });
        
        //TextField to enter IP Address
        
        tf = new JTextField();
        tf.setBounds(20, 15, 200, 40);
        tf.setText("Enter your IP address here");
        tf.setForeground(new Color(153, 153, 153));
        tf.setFont(new Font("Verdana", Font.PLAIN, 12));
        tf.setToolTipText("Type IP Address and press Enter");
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                ipFocusGained(evt);
            }
            public void focusLost(FocusEvent evt) {
                ipFocusLost(evt);
            }
        }); 
        tf.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                ipKeyPressed(ke);
            }
        });
        p1.add(tf);
        
       
        sp = new JScrollPane();
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        a1 = new JTextArea();
        a1.setBounds(10, 85, 680, 410);
        a1.setBackground(new Color(228,223,237));         
        a1.setFont(new Font("Verdana", Font.PLAIN, 14));
        a1.setBorder(new LineBorder(Color.BLACK));
        a1.setEditable(false);     
        a1.setLineWrap(true);
        a1.setWrapStyleWord(true);
        
        sp.setBounds(10, 85, 680, 410);
        sp.getViewport().setBackground(Color.WHITE);
        sp.getViewport().add(a1);
        add(sp);
        
        t1 = new JTextField();     
        t1.setBounds(10, 505, 580, 35);
        t1.setText("Type your message here");
        t1.setBackground(Color.white);
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
        send.setBounds(600, 505, 90, 35);
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
    
    //constructor to put IP Address & Port number - SOCKET PROGRAMMING
    public Client(String address, int port)
    {
        // to establish socket connection
        try{
            skt = new Socket(address, port);
            a1.append("\n 'Socket Connection Established successfully...!!' ");
            
            tf.setEditable(false);      //after establishing connection IP Address textfield can't be edited 
            
            din = new DataInputStream(skt.getInputStream());        
            dout = new DataOutputStream(skt.getOutputStream()); 
            //collects input and output stream from server socket   
        }
        catch(UnknownHostException e)  
        {
            JOptionPane.showMessageDialog(this, e, "ERROR FOUND", JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException i)        //when entered IP address is wrong or Server is not yet started then two error dialog boxes appear
        {
            JOptionPane.showMessageDialog(this, "Recheck IP Address" , "ERROR", JOptionPane.ERROR_MESSAGE);  
            JOptionPane.showMessageDialog(this, "If IP address of server is correct then: \nStart Sever first..!!" , "ERROR", JOptionPane.ERROR_MESSAGE); 
        }
    }
    
 //Initialising Place holder for TextField of IP ADDRESS by focusevent --> Focuslost => place holder appears ; Focusgained => place holder disappears
    
    private void ipFocusGained(FocusEvent evt){
          if(tf.getText().equals("Enter your IP address here"))
          {
            tf.setText("");   //when focus is gained txt field becomes empty
            tf.setForeground(new Color(0,0,0)); //new text we enter - color is set to black
          }
      }
    private void ipFocusLost(FocusEvent evt){
          if(tf.getText().equals(""))
          {
            tf.setText("Enter your IP address here");
            tf.setForeground(new Color(153,153,153)); 
          }
      }
//Initialising key event to IP Address TextField --> press Enter in order to send request to server by getting text from TextField
    private void ipKeyPressed(KeyEvent ke){
         if(ke.getKeyCode()== KeyEvent.VK_ENTER){
             String address = tf.getText();
            //to access Client constructor by putting IP address & port num
             Client client = new Client(address, 6040); //same port as server 
         }
    }

 //Initialising Place holder for TextBox by focusevent --> Focuslost => place holder appears ; Focusgained => place holder disappears
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
      
//Creating Send functionality by action event onto button
    private void sendActionPerformed(ActionEvent ae){                
      
        try{
            if(! (t1.getForeground().equals(new Color(153, 153, 153))))
            {
                    String msg = t1.getText(); 
                    a1.setText(a1.getText() + "\n ME: " + msg);
                    dout.writeUTF(msg);       
                    t1.setText("Type your message here");
                    t1.setForeground(new Color(153, 153, 153));
                
            }
        }
        /*When Server is disconnected from chat or not at all connected and you try to send a text then,
        that text will appear to you on your TextArea but it is not sent to Server
        for this exception a warning dialog box appears */
        catch(Exception e){
         JOptionPane.showMessageDialog(this, "Connection not found \n Message not sent", "WARNING", JOptionPane.WARNING_MESSAGE);
        }  
    }
      
    public static void main(String[] args){
        
        new Client().setVisible(true);      //to make UI visible to user and performs all actions inside the Client() constructor.
        
        String msgin = "";
        //reads message until there is flow of stream
        while(true){    
            try{
                    msgin = din.readUTF();
                    a1.setText(a1.getText()+"\n SERVER: "+ msgin);
                    a1.setForeground(Color.BLACK);  
            }
            catch(Exception e){}         //null point exception or IOException
        }
    }
}    
  

 
