import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class SignUp implements ActionListener, DocumentListener {
    JFrame frame = new JFrame("Sign Up");
    JTextField uname = new JTextField();
    JTextField bname = new JTextField();
    JButton resendotp = new JButton("Resend OTP");
    JButton confirm = new JButton("Confirm");
    JLabel otp = new JLabel("OTP: ");
    JTextField mail = new JTextField();
    JTextArea address = new JTextArea();
    JPasswordField jpf = new JPasswordField();
    JButton next = new JButton("Next");
    JButton back = new JButton("Login Instead");
    JLabel label = new JLabel("Enter UserName: ");
    JLabel label2 = new JLabel("Enter Business Name: ");
    JLabel label3 = new JLabel("Enter Email-Address: ");
    JLabel label4 = new JLabel("Enter Password: ");
    JLabel label5 = new JLabel("Re enter Password: ");
    JLabel lastlabel = new JLabel();
    JTextField repass = new JTextField();
    JLabel ladd = new JLabel("Enter Business Address");
    boolean iseverythingok=true;
    JPanel otppanel = new JPanel();
    JPanel panel = new JPanel();
    JSONObject obj = null;
    int code;
    JTextField jtf2 = new JTextField();
    JLabel upilabel = new JLabel("UPI Id");
    JTextField upi = new JTextField();
    JSONArray ja=null;
    JButton con = new JButton("Next");

    SignUp() {
        JScrollPane sp = new JScrollPane(address);
        sp.setVerticalScrollBar(new JScrollBar());
        address.setRows(1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 400, 400);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 2 * frame.getSize().width, 20));
        frame.add(panel);
        frame.add(otppanel);
        otppanel.setBorder(new TitledBorder("OTP Verification"));
        otppanel.setVisible(false);
        panel.setBorder(new TitledBorder("Sign Up..."));
        panel.setLayout(new GridLayout(10, 2));
        panel.add(label);
        panel.add(uname);
        panel.add(label2);
        panel.add(bname);
        panel.add(label3);
        panel.add(mail);
        panel.add(ladd);
        panel.add(sp);
        panel.add(label4);
        panel.add(jpf);
        panel.add(label5);
        panel.add(repass);
        panel.add(upilabel);
        panel.add(upi);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(back);
        panel.add(next);
        otppanel.setLayout(new GridLayout(3,2));
        otppanel.add(otp);
        otppanel.add(jtf2);
        otppanel.add(confirm);
        otppanel.add(resendotp);
        resendotp.addActionListener(this);
        confirm.addActionListener(this);
        address.setLineWrap(true);
        address.setWrapStyleWord(true);
        frame.add(lastlabel);
        next.addActionListener(this);
        back.addActionListener(this);
        repass.getDocument().addDocumentListener(this);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(con);
        con.setVisible(false);
        con.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            frame.dispose();
            new LoginPage();
        } else if (e.getSource() == next) {
            if(repass.getText().length()>=5&&repass.getText().equals(String.valueOf(jpf.getPassword())))
            {
                try {
                    obj = (JSONObject) new JSONParser().parse(new FileReader(".\\Data.JSON"));
                } catch (IOException | ParseException ex) {

                    ex.printStackTrace();
                }
                String user = uname.getText();

                ja = (JSONArray) (obj.get("login"));
                Iterator itr = ja.iterator();
                while(itr.hasNext())
                {   iseverythingok = true;
                    JSONObject all = (JSONObject) itr.next();
                    System.out.println("User:" +user+"--------Getkey: "+all.get("uname").toString());
                    if(all.get("uname").equals(user)){
                        lastlabel.setText("Username Already Exists. Enter Another User Name");
                        iseverythingok=false;}
                    else if(all.get("email").equals(mail.getText())){
                        lastlabel.setText("Email Already Registered. Please Enter Another Email");
                        iseverythingok=false;}
                    else if(uname.getText().length()==0||mail.getText().length()==0||address.getText().length()==0) {
                        lastlabel.setText("All Fields are Compulsary");
                        iseverythingok=false;
                    }
                }
                if(iseverythingok){
                    sendOTP();
                    iseverythingok=false;
                }
            }
            else
                lastlabel.setText("Enter at least 5 characters in password and enter same password in both fields");
        }
        else if(e.getSource()==resendotp)
        {sendOTP();}
        else if(e.getSource()==confirm)
        {
            if(code==Integer.parseInt(jtf2.getText())&&!iseverythingok)
            {
                 iseverythingok=true;
                    FileWriter fw = null;
                    JSONObject newobj = new JSONObject();
                    newobj.put("uname",uname.getText());
                    newobj.put("pass",repass.getText());
                    newobj.put("email",mail.getText());
                    newobj.put("UPI",upi.getText());
                    newobj.put("address",address.getText());
                    newobj.put("business",bname.getText());
                    ja.add(newobj);
                    obj.put("login",ja);
                    try {
                        fw = new FileWriter(".\\Data.JSON");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        fw.write(obj.toJSONString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    lastlabel.setText("User Registered Successfully.");
                    try {
                        fw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    con.setVisible(true);
                    con.doClick();
            }
            else
            {
                if(iseverythingok)
                    lastlabel.setText("Registration is already done");
                else
                    lastlabel.setText("Incorrect OTP");
            }
        }
        else if(e.getSource()==con) {
            frame.dispose();
            new ItemsSource(obj, uname.getText());
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (repass.getText().equals(String.valueOf(jpf.getPassword())))
            lastlabel.setText("Matched.");
        else lastlabel.setText("Both entered passwords are not matching");
    }
    @Override
    public void removeUpdate(DocumentEvent e) {}
    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public void sendOTP()
    {
        Properties pr = System.getProperties();
        pr.put("mail.smtp.host","localhost");
        pr.put("mail.smtp.port","25");
        pr.put("mail.smtp.auth","false");
        Session ss = Session.getDefaultInstance(pr);
        ss.setDebug(true);
        try{
            lastlabel.setText("");
            code = (int)(Math.random()*10000);
            MimeMessage message = new MimeMessage(ss);
            message.setFrom(new InternetAddress("yourpersonalmail@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getText()));
            message.setSubject("Code For Registration");
            message.setSentDate(new Date());
            message.setText(code+"","UTF-8");
            Transport.send(message);
            otppanel.setVisible(true);
            lastlabel.setText("Enter OTP sent to your mail account");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

}