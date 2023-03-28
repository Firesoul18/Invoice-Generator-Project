import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.IOException;

public class AddItems implements ActionListener {
    JFrame frame = new JFrame("Add All Items");
    JPanel panel = new JPanel();
    JLabel name = new JLabel("Item");
    JLabel price = new JLabel("Price(per piece/per kg/liter");
    JTextField jtf = new JTextField();
    JTextField jtf2 = new JTextField();
    JLabel lastlabel = new JLabel("Press Done When All Items Are Added");
    JButton next = new JButton("Next");
    JButton done = new JButton("Done");
    JLabel firstlabel = new JLabel("Enter All Items");
    JSONObject obj = null;
    JSONArray array = null;
    JSONObject mainobj = null;
    JSONArray items = new JSONArray();
    FileReader fr;

    {
        try {
            fr = new FileReader(".\\Data.JSON");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    AddItems(String mail) {
        try {
            obj = (JSONObject) new JSONParser().parse(fr);
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        array = (JSONArray) obj.get("login");
        for (Object n : array)
            if (((JSONObject) n).get("email").toString().equals(mail)) {
                mainobj = (JSONObject) n;
                if(mainobj.containsKey("items"))
                    items = (JSONArray) mainobj.get("items");
                System.out.println("\n\nmainobj------->"+mainobj);
                System.out.println(mail);
            }
        frame.setBounds(200, 200, 500, 500);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 2 * frame.getSize().width, 20));
        frame.setVisible(true);
        frame.add(firstlabel);
        frame.add(panel);
        panel.setLayout(new GridLayout(3, 2));
        panel.add(name);
        panel.add(jtf);
        panel.add(price);
        panel.add(jtf2);
        panel.add(next);
        panel.add(done);
        frame.add(lastlabel);
        next.addActionListener(this);
        done.addActionListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == next) {
            if (jtf.getText().equals("") && jtf2.getText().equals("") && jtf.getText() != null && jtf.getText() != null)
                lastlabel.setText("Please Enter Valid Values");
            else {
                lastlabel.setText("");
                JSONObject item = new JSONObject();
                Boolean added = true;
                try {
                    item.put(jtf.getText(), Integer.parseInt(jtf2.getText()));
                    Boolean notalready = true;
                    for (Object o : items) {
                        if (((JSONObject) o).containsKey(jtf.getText())) {
                            ((JSONObject) o).replace(jtf.getText(), Integer.parseInt(jtf2.getText()));
                            notalready = false;
                        }
                    }
                    if (!items.contains(item) && notalready) {
                        items.add(item);
                        lastlabel.setText("Item Added");
                    } else {
                        items.remove(item);
                        items.add(item);
                        lastlabel.setText("Item Replaced");
                    }
                } catch (NumberFormatException | NullPointerException exception) {
                    exception.printStackTrace();
                    added = false;
                    lastlabel.setText("Please Enter Valid Values");
                }
            }
            jtf.setText("");
            jtf2.setText("");
        } else if (e.getSource() == done) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(".\\Data.JSON");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            mainobj.put("items", items);
            array.remove(mainobj);
            array.add(mainobj);
            obj.put("login", array);
            try {
                fw.write(obj.toJSONString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            frame.dispose();
            new LoginPage();

        }
    }
}