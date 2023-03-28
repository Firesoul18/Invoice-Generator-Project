import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ChangeData implements ActionListener {
    JLabel lastlabel = new JLabel();
    JSONObject object;
    JFrame frame = new JFrame("Change Data");
    JTextField bname = new JTextField();
    JTextField upi = new JTextField();
    JTextField address = new JTextField();

    JSONObject obj = null;
    JPanel panel = new JPanel();
    JButton changeitems = new JButton("Change Items");
    JButton save = new JButton("Save");
    ChangeData(JSONObject object)
    {
        this.object = object;
        obj = object;
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,2*(frame.getSize().width),10));
        panel.setLayout(new GridLayout(0,2));
        panel.setBorder(new TitledBorder("Edit Account"));
        panel.add(new JLabel("Change Business Name: "));
        panel.add(bname);
        panel.add(new JLabel("Change Address: "));
        panel.add(address);
        panel.add(new JLabel("Change UPI: "));
        panel.add(upi);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(changeitems);
        panel.add(save);
        frame.setBounds(100,100,500,500);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
        frame.add(lastlabel);

        save.addActionListener(this);
        changeitems.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==save) {
            if (bname.getText() != null && !bname.getText().equals(""))
                obj.replace("business", bname.getText());
            if (upi.getText() != null && !upi.getText().equals(""))
                obj.replace("UPI", upi.getText());
            if (address.getText() != null && !upi.getText().equals(""))
                obj.replace("address", address.getText());
            FileWriter fw = null;
            try {
                JSONObject loginobj = (JSONObject) new JSONParser().parse(new FileReader(".\\Data.JSON"));
                JSONArray loginarray = (JSONArray) loginobj.get("login");
                JSONObject mainobj = null;
                for (Object o : loginarray) {
                    if ((JSONObject) o == object) ;
                    mainobj = (JSONObject) o;
                }
                loginarray.remove(mainobj);
                loginarray.add(obj);
                loginobj.replace("login", loginarray);
                fw = new FileWriter(".\\Data.JSON");
                fw.write(loginobj.toJSONString());
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            frame.dispose();
            new Main(obj);
        }
        else if(e.getSource()==changeitems)
        {
            try {
                new ItemsSource((JSONObject) (new JSONParser().parse(new FileReader(".\\Data.JSON"))),obj.get("uname").toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        }
    }
}