import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import static java.lang.Thread.sleep;

public class ForgetPassword implements ActionListener {
    JFrame frame = new JFrame("Reset Password");
    JPanel panel = new JPanel();
    JLabel label1 = new JLabel("Enter Your Mail Address: ");
    JLabel otp = new JLabel("Enter OTP sent to your mail: ");
    JTextField tf1 = new JTextField();
    JTextField tf2 = new JTextField();
    JButton sendmail = new JButton("Send Mail");
    JButton confirm = new JButton("Confirm");
    JLabel noy = new JLabel();
    JButton resendotp = new JButton("Resend OTP");
    static Integer code = null;
    ForgetPassword()
    {
        frame.setBounds(400,400,400,400);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,2*frame.getSize().width,20));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        panel.setLayout(new GridLayout(5,2));
        panel.add(label1);
        panel.add(tf1);
        panel.add(sendmail);
        panel.add(new JLabel());
        panel.add(otp);
        panel.add(tf2);
        otp.setVisible(false);
        tf2.setVisible(false);
        confirm.setVisible(false);
        resendotp.setVisible(false);
        panel.setBorder(new TitledBorder("OTP Confirmation"));
        panel.add(confirm);
        panel.add(resendotp);
        frame.add(panel);
        frame.add(noy);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        sendmail.addActionListener(this);
        confirm.addActionListener(this);
        resendotp.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e)
    {
        JSONObject obj = null;
        if(e.getSource()==sendmail) {
            String email = tf1.getText();
            try {
                obj = (JSONObject) new JSONParser().parse(new FileReader(".\\Data.JSON"));
            } catch (IOException | ParseException exc) {
                exc.printStackTrace();
            }
            JSONArray ja = (JSONArray) (obj.get("login"));
            Iterator itr = ja.iterator();
            while(itr.hasNext())
            {
                JSONObject all = (JSONObject) itr.next();
                if(!all.get("email").equals(email))
                {
                    noy.setText("Email Not Registered");
                }
                else{
                    noy.setText("");
                    Properties pr = System.getProperties();
                    pr.put("mail.smtp.host","localhost");
                    pr.put("mail.smtp.port","25");
                    Session ss = Session.getDefaultInstance(pr);
                    ss.setDebug(true);
                    try{
                        noy.setText("");
                        code = (int)(Math.random()*10000);
                        MimeMessage message = new MimeMessage(ss);
                        message.setFrom(new InternetAddress("yourpersonalmail@gmail.com"));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                        message.setSubject("Code For Password Reset");
                        message.setSentDate(new Date());
                        message.setText(code.toString(),"UTF-8");
                        Transport.send(message);
                        otp.setVisible(true);
                        tf2.setVisible(true);
                        resendotp.setVisible(true);
                        confirm.setVisible(true);
                        noy.setText("OTP Sent");
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
        }
        else if(e.getSource()==confirm)
        {
            String enteredotp = tf2.getText();
            if(Integer.parseInt(enteredotp)==code)
            {
                noy.setText("OTP is Correct");
                try {
                    sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                new ResetPassword(tf1.getText());
                frame.dispose();
            }
            else noy.setText("Incorrect OTP");
        }
        else if(e.getSource()==resendotp)
            sendmail.doClick();
    }

}
