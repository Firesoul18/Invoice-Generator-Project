import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.time.LocalDateTime;

class Main implements ActionListener, ItemListener, DocumentListener {
    JFrame openframe = new JFrame("Select File To Open");
    JButton print = new JButton("Print");
    JPanel openpanel = new JPanel();
    JTextArea jta2 = new JTextArea();
    List openlist = new List();
    JTextArea jta = new JTextArea();
    String[] names = null;
    File opened = null;
    JMenuBar menubar = new JMenuBar();
    JMenu menu = new JMenu("Options");
    JMenuItem newbill = new JMenuItem("New Bill");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem change = new JMenuItem("Edit Account");
    JMenuItem exit = new JMenuItem("Exit");
    JLabel cname = new JLabel("Customer Name: ");
    JTextField cusname = new JTextField();
    JLabel cmail = new JLabel("Customers' Email: ");
    JTextField cusmail = new JTextField();
    JFrame frame = new JFrame("Invoice Generator");
    JPanel panel = new JPanel();
    JPanel panel1 = new JPanel();
    List finallist = new List();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel customerpanel = new JPanel();
    JPanel itemslistpanel = new JPanel(new BorderLayout());
    JPanel buttonspanel = new JPanel();
    JButton next = new JButton("Next");
    JLabel mainlabel = new JLabel();
    JLabel mainlabel2 = new JLabel();
    JLabel mainlabel3 = new JLabel();
    JButton clear = new JButton("Clear");
    List list = null;
    List list1 = new List();
    CardLayout card = new CardLayout();
    JPanel []  allpanels = null;
    JLabel[] alllabels = null;
    JTextField[] alltextfields = null;
    JLabel qr = new JLabel();
    JSONObject obj =null;
    LocalDateTime datetime = null;
    JPanel searchitempanel = new JPanel();
    JPanel lp = new JPanel();
    JLabel searchitemlabel = new JLabel("Search");
    JTextField search = new JTextField();
    CardLayout c = new CardLayout();
    File dirname;
    Main(JSONObject object)
    {
        obj = object;
        dirname = new File("All Bills"+"\\"+obj.get("uname").toString());
        if(!dirname.exists())
            dirname.mkdir();
        System.out.println("FILENAME_______>"+dirname.getName());
        System.out.println("Obj in main ------>"+obj);
        buttonspanel.add(clear);
        buttonspanel.add(next);
        frame.setBounds(100,100,400,400);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setJMenuBar(menubar);
        menubar.add(menu);
        menu.add(newbill);
        menu.add(open);
        menu.add(change);
        menu.add(exit);
        frame.setVisible(true);
        panel.setLayout(card);
        frame.add(panel,"Center");
        frame.add(buttonspanel,"South");
        panel.add(panel1,"Panel1");
        panel.add(new JScrollPane(panel2),"Panel2");
        panel.add(new JScrollPane(panel3),"Panel3");
        panel1.setLayout(new GridLayout(2,1));
        panel1.add(customerpanel);
        panel1.add(itemslistpanel);
        openframe.setLayout(new CardLayout());
        customerpanel.setBorder(new TitledBorder("Customer's Details"));
        itemslistpanel.setBorder(new TitledBorder("Select Bought Items"));
        JSONArray array = (JSONArray) object.get("items");
        list = new List();
        list.setMultipleMode(true);
        for(Object o: array)
            list.add(((JSONObject)o).keySet().iterator().next().toString());

        allpanels= new JPanel[list.getItemCount()+1];
        alllabels= new JLabel[list.getItemCount()+1];
        alltextfields = new JTextField[list.getItemCount()+1];
        for(int i=0; i<allpanels.length; i++)
        {
            alltextfields[i]=new JTextField();
            allpanels[i]=new JPanel(new FlowLayout(FlowLayout.LEFT,40,10));
            alllabels[i]= new JLabel();
            alllabels[i].setHorizontalTextPosition(JLabel.CENTER);
        }
        itemslistpanel.setLayout(new BorderLayout());
        lp.setLayout(c);
        itemslistpanel.add(searchitempanel,"North");
        searchitempanel.setLayout(new GridBagLayout());
        itemslistpanel.add(lp,"Center");
        lp.add(list,"list");
        lp.add(list1,"list1");
        customerpanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty=1;
        gbc.weightx=1;
        customerpanel.add(cname,gbc);
        searchitempanel.add(searchitemlabel,gbc);
        search.setColumns(10);
        gbc.gridx = 1;
        customerpanel.add(cusname,gbc);
        searchitempanel.add(search,gbc);
        cusname.setColumns(10);
        gbc.gridx=0;
        gbc.gridy =1;
        customerpanel.add(cmail,gbc);
        gbc.gridx=1;
        customerpanel.add(cusmail,gbc);
        cusmail.setColumns(10);
        gbc.gridx=0;
        gbc.gridy=2;
        customerpanel.add(mainlabel2,gbc);
        clear.addActionListener(this);
        next.addActionListener(this);
        open.addActionListener(this);
        exit.addActionListener(this);
        newbill.addActionListener(this);
        change.addActionListener(this);
        search.getDocument().addDocumentListener(this);
        list1.addItemListener(this);
        panel2.setLayout(new GridLayout(0,1,40,10));
        panel2.add(mainlabel3);
        panel2.add(new JLabel());
        openframe.add(openlist);
        openframe.add(openpanel);
        openpanel.setLayout(new BorderLayout());
        openpanel.add(jta2,"Center");
        openpanel.add(print,"South");
        print.addActionListener(this);
        for(int k=0; k<alllabels.length; k++) {
            allpanels[k].add(alllabels[k]);
            panel2.add(allpanels[k]);
        }
        panel2.add(mainlabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==clear&&clear.getText().equals("Clear"))
        {
            cusmail.setText("");
            cusname.setText("");
            for(int k: list.getSelectedIndexes())
                list.deselect(k);
            for(int k: list1.getSelectedIndexes())
                list1.deselect(k);
            search.setText("");
            c.show(lp,"list");
        }
        else if(e.getSource()==clear&&clear.getText().equals("Remove All"))
        {
            for(int i=1; i< alltextfields.length; i++)
                alltextfields[i].setText("");
        }
        else if(e.getSource()==next&&next.getText().equals("Next"))
        {
            if(!cusname.getText().equals("")&&!cusmail.getText().equals("")) {
                int i = 1;
                for (String s : list.getSelectedItems()) {
                    alllabels[0].setText("Items");
                    alltextfields[0].setText("Number of Items Bought");
                    allpanels[0].add(alltextfields[0]);
                    alltextfields[0].setEditable(false);
                    allpanels[i].add(alltextfields[i]);
                    alllabels[i].setText(s);
                    alltextfields[i].setColumns(3);
                    i++;
                }
                card.next(panel);
                next.setText("Done");
                clear.setText("Remove All");
            }
            else
                mainlabel2.setText("All Fields are Mandatory");
        }
        else if(e.getSource()==next&&next.getText().equals("Done"))
        {
            boolean bool = false;
            for(int i=1; i<list.getSelectedIndexes().length+1; i++)
            {
                if(!alltextfields[i].getText().equals("")||alltextfields[i].getText()!=null)
                {
                    try {
                        finallist.add(alllabels[i].getText() + "  :  " + Integer.parseInt(alltextfields[i].getText()));
                    }
                    catch(Exception ee)
                    {
                        mainlabel3.setText("All Fields Must Have Values and Only Numbers Are Allowed");
                        finallist.removeAll();
                        bool = false;
                        break;
                    }
                    bool = true;
                }
                else {
                    mainlabel3.setText("All Fields Must Have Values and Only Numbers Are Allowed");
                    finallist.removeAll();
                    bool = false;
                    break;
                }
            }
            if(bool)
            {
                int totalcost=0;
                mainlabel3.setText("");
                jta.setText("\t\t"+obj.get("business")+"\n\t"+obj.get("address"));
                jta.append("\n\t--------------------------------------------------------------\n");
                jta.append("\tName: "+cusname.getText()+"\n\n\tMail: "+cusmail.getText()+"\n\n");
                datetime = LocalDateTime.now();
                jta.append("\tDate: "+datetime);
                jta.append("\n\t---------------------------------------------------------------\n");
                for(int i =0; i<finallist.getItemCount(); i++)
                {
                    int costofone =0;
                    JSONArray ineedit = (JSONArray) obj.get("items");
                    for(Object o: ineedit)
                    {
                        try {costofone= Integer.parseInt(((JSONObject) o).get(alllabels[i + 1].getText()).toString());}
                        catch (Exception ignored){}
                    }
                    try{
                        totalcost+=Integer.parseInt(alltextfields[i+1].getText())*costofone;
                        jta.append("\n\t"+finallist.getItem(i)+" : "+Integer.parseInt(alltextfields[i+1].getText())*costofone);
                    }
                    catch (Exception eeee){eeee.printStackTrace();}
                }
                jta.append("\n\t----------------------------------------------------------------\n\tTotal: "+totalcost);
                String url;
                url = new String("upi://pay?pa="+obj.get("UPI")+"&pn=Bill Calculator&tn=&tr=&am="+totalcost+"&cu=INR&purpose=Shopping");
                ImageIcon icon =new ImageIcon();
                ByteArrayOutputStream baos = QRCode.from(url).to(ImageType.PNG).stream();
                byte[] imagearray = baos.toByteArray();
                icon = new ImageIcon(imagearray);
                qr.setIcon(icon);

                panel3.setLayout(new BorderLayout());
                panel3.add(jta,"Center");
                panel3.add(qr,"East");
                card.next(panel);
                next.setText("Print and Save");
                clear.setText("Save");
            }
        }
        else if(e.getSource()==next&&next.getText().equals("Print and Save"))
        {
            try {
                if(!dirname.exists())
                 dirname.mkdir();
                File file = new File("All Bills\\"+dirname.getName()+"\\"+ cusname.getText() + datetime.toLocalDate() + "+" + datetime.getHour() + "-" + datetime.getMinute() +"-"+datetime.getSecond()+ ".txt");
                if(!file.exists())
                    file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(jta.getText().getBytes());
                Desktop.getDesktop().print(file);
                frame.dispose();
                new Main(obj);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if(e.getSource()==clear&&clear.getText().equals("Save"))
        {
            if(!jta.getText().endsWith("Saved.")) {
                try {
                    FileOutputStream fos = new FileOutputStream("All Bills\\"+dirname.getName()+"\\" + cusname.getText() + datetime.toLocalDate() + "+" + datetime.getHour() + "-" + datetime.getMinute() +"-"+datetime.getSecond()+ ".txt");
                    fos.write(jta.getText().getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                frame.dispose();
                new Main(obj);
            }
        }
        else if (e.getSource()==newbill) {
            frame.dispose();
            new Main(obj);
        }
        else if (e.getSource()==exit) {
            System.exit(1);
        }
        else if (e.getSource()==change) {
            new ChangeData(obj);
        }
        else if(e.getSource()==open)
        {
            File file = new File("All Bills\\"+dirname.getName());
            names = file.list();
            openframe.setBounds(100,100,300,300);
            openframe.setVisible(true);
            openframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            for(String name:names)
                openlist.add(name);
            openlist.addItemListener(this);
        }
        else if(e.getSource()==print)
        {
            try {
                Desktop.getDesktop().print(opened);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==openlist) {
            try {
                opened = new File("All Bills\\"+dirname.getName()+"\\"+openlist.getSelectedItem());
                FileInputStream fis = new FileInputStream(opened);
                int ch;
                while ((ch = fis.read()) != -1)
                    jta2.append((char) ch + "");
                jta2.setEditable(false);
                openframe.remove(openlist);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if(e.getSource()==list1)
        {
            for(int x=0; x<list.getItemCount(); x++)
                for (int y=0; y<list1.getItemCount(); y++)
                    if (list1.getItem(y).equals(list.getItem(x)))
                    {
                        if(list1.isIndexSelected(y))
                            list.select(x);
                        else
                            list.deselect(x);
                    }
        }

    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        if(search.getText().length()==0)
            c.show(lp,"list");
        else {
            list1.setMultipleMode(true);
            list1.removeAll();
            for(int x=0; x<list.getItemCount(); x++)
                if(list.getItem(x).toLowerCase().contains(search.getText().toLowerCase())) {
                    list1.add(list.getItem(x));
                }
            for(int x=0; x<list1.getItemCount(); x++)
            {
                for(int y=0; y<list.getSelectedIndexes().length; y++)
                {
                    if(list.getSelectedItems()[y]==list1.getItem(x))
                        list1.select(x);
                }
            }
            c.show(lp, "list1");
        }
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        if(search.getText().length()==0)
            c.show(lp,"list");
        else {
            list1.removeAll();
            for(int x=0; x<list.getItemCount(); x++)
                if(list.getItem(x).toLowerCase().contains(search.getText().toLowerCase()))
                    list1.add(list.getItem(x));
            c.show(lp, "list1");
        }
    }
    @Override
    public void changedUpdate(DocumentEvent e) {}
}
