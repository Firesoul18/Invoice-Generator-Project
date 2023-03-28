import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Iterator;

public class ItemsSource implements ActionListener {
    JSONObject obj;
    JFrame frame = new JFrame("Select Source");
    JLabel finallabel = new JLabel("Excel File will remove all previous elements and add all new elements.");
    JTextArea instruct = new JTextArea("Excel File Format Must be as following:\n______   ______\n| Items |   | Price |\n______   ______\n| item 1 |   | price |\n______  ______");
    JButton one = new JButton("Enter Manually");
    FileDialog fd = null;
    JButton select = new JButton("Select Excel File");
    String uname;
    String email;
    JPanel panel = new JPanel();
    ItemsSource(JSONObject object, String username)
    {
        uname = username;
        obj=object;
        instruct.setEditable(false);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,4*(frame.getSize().width),10));
        panel.setLayout(new GridLayout(1,2));
        frame.setBounds(100,100,400,400);
        fd = new FileDialog(frame,"Select Excel File",FileDialog.LOAD);
        frame.add(new JLabel("Select How You Want To Add All Items Of Your Shop"));
        frame.add(panel);
        panel.add(one);
        panel.add(select);
        frame.add(finallabel);
        frame.add(new JLabel("Add Elements Manually after Choosing excel file if you want."));
        frame.add(instruct);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        one.addActionListener(this);
        select.addActionListener(this);
        Iterator iitr = ((JSONArray)obj.get("login")).iterator();
        while(iitr.hasNext())
        {
            JSONObject jobj = (JSONObject) iitr.next();
            if(jobj.get("uname").equals(username)){
                email= jobj.get("email").toString();
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==select)
        {
            fd.setVisible(true);
            String name = fd.getFile();
            String path = fd.getDirectory();
            System.out.println(name+ " "+path);
            if(!name.endsWith(".xlsx"))
            finallabel.setText("Only .xlsx files accepted");
            else
            {
                JSONArray loginarray = (JSONArray) obj.get("login");
                JSONObject mainobj = null;
                for(int i =0; i<loginarray.size(); i++) {
                    if (((JSONObject) (loginarray.get(i))).get("uname").equals(uname)) {
                        mainobj = (JSONObject) loginarray.get(i);
                        System.out.println("UName Matched ------>"+uname);
                        System.out.println("mainobj -------->"+loginarray.get(i));
                    }
                }
                JSONArray array = new JSONArray();
                try {
                    FileInputStream fs = new FileInputStream(new File(path,name));
                    XSSFWorkbook workbook = new XSSFWorkbook(fs);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                   for(int i =1; i<sheet.getLastRowNum(); i++)
                    {
                        JSONObject abc = new JSONObject();
                        abc.put(sheet.getRow(i).getCell(0).getStringCellValue(),(int)(sheet.getRow(i).getCell(1).getNumericCellValue()));
                        array.add(abc);
                        System.out.println(sheet.getRow(i).getCell(0).getStringCellValue()+sheet.getRow(i).getCell(1).getNumericCellValue());
                    }
                    if(mainobj.containsKey("items"))
                    mainobj.replace("items",array);
                    else
                        mainobj.put("items",array);
                    loginarray.remove(mainobj);
                    loginarray.add(mainobj);
                    obj.replace("login",loginarray);
                    FileWriter fw = new FileWriter(".\\Data.JSON");
                    fw.write(obj.toJSONString());
                    fw.close();
                    workbook.close();
                    finallabel.setText("Items Added.");
                    new Main(mainobj);

                } catch (Exception ex) {
                   finallabel.setText("Something is wrong with the file...");
                }
            }
        } else if (e.getSource()==one) {
            frame.dispose();
            System.out.println("EMail to call additems()----->"+email);
            new AddItems(email);
        }
    }
}
