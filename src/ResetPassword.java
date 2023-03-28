import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class ResetPassword implements ActionListener, DocumentListener {
    JFrame frame = new JFrame("Reset Password");
    JLabel label = new JLabel("New Password: ");
    JLabel label2 = new JLabel("Re-Enter New Password: ");
    JLabel lastlabel = new JLabel();
    JPasswordField firstpass = new JPasswordField();
    JTextField repass = new JTextField();
    JButton confirm = new JButton("Confirm");
    JButton cancel = new JButton("Cancel");
    JPanel panel = new JPanel();
    static String email;

    ResetPassword(String email) {
        ResetPassword.email = email;
        frame.setBounds(400, 400, 400, 400);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 2 * frame.getSize().width, 20));
        panel.setLayout(new GridLayout(4, 2));
        panel.add(label);
        panel.add(firstpass);
        panel.add(label2);
        panel.add(repass);
        panel.add(cancel);
        panel.add(confirm);
        cancel.addActionListener(this);
        confirm.addActionListener(this);
        repass.getDocument().addDocumentListener(this);
        frame.add(panel);
        frame.add(lastlabel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            new LoginPage();
            frame.dispose();
        } else if (e.getSource() == confirm) {
            if (lastlabel.getText().equals("Matched.")&&repass.getText().length()>=5) {
                JSONObject obj = null;
                try {
                    obj = (JSONObject) new JSONParser().parse(new FileReader(".\\Data.JSON"));
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
                JSONArray ja = (JSONArray) (obj.get("login"));
                Iterator itr = ja.iterator();
                while (itr.hasNext()) {
                    JSONObject all = (JSONObject) itr.next();
                    if (all.get("email").equals(email)) {
                        all.remove("pass");
                        all.put("pass", repass.getText());
                        try {
                            FileWriter fw = new FileWriter(".\\Data.JSON");
                            fw.write(obj.toJSONString());
                            fw.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        lastlabel.setText("Password Changed. Press Cancel to go back");
                        break;
                    }
                }
            }
            else
                lastlabel.setText("Password Must be 5 characters long and enter the same password in both fields");
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (repass.getText().equals(String.valueOf(firstpass.getPassword())))
            lastlabel.setText("Matched.");
        else lastlabel.setText("Both entered passwords are not matching");
    }
    @Override
    public void removeUpdate(DocumentEvent e) {}
    @Override
    public void changedUpdate(DocumentEvent e) {}
}