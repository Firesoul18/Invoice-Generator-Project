import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.parser.*;
import org.json.simple.*;
public class LoginPage extends KeyAdapter implements ActionListener,MouseListener{
    JFrame frame = new JFrame("Bill Generator");
    JPanel panel = new JPanel();
    JLabel noy = new JLabel();
    JTextField jtf = new JTextField();
    JPasswordField jpf = new JPasswordField();
    JButton login = new JButton("Login");
    JButton signup = new JButton("Signup");
    JButton fpass = new JButton("Forgot Password");
    JPanel panel2 = new JPanel();
    JPanel forpass = new JPanel();
    JLabel foreye = new JLabel();
    ImageIcon icon=new ImageIcon(new ImageIcon(".\\eye.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
    ImageIcon icon2 = new ImageIcon(new ImageIcon(".\\eyeclose.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

    LoginPage()
    {
        frame.setBounds(100,100,500,500);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,2*(frame.getSize().width)
                ,30));
        panel.setBorder(BorderFactory.createTitledBorder("Login"));
        panel.setLayout(new GridLayout(5,2));
        panel2.setLayout(new GridLayout(1,2) );
        forpass.setLayout(new BorderLayout());
        panel.add(new JLabel("User-Name: "));
        jtf.setColumns(20);
        panel.add(jtf);
        panel.add(new JLabel("Password"));
        jpf.setEchoChar('*');
        forpass.add(jpf,"Center");
        jpf.setSize(10,0);
        foreye.setIcon(icon);
        forpass.add(foreye,"East");
        panel.add(forpass);
        panel.add(fpass);
        panel.add(login);
        frame.add(panel);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel2.add(new JLabel("Don't Have an account"));
        panel2.add(signup);
        frame.add(panel2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(noy);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        login.addActionListener(this);
        fpass.addActionListener(this);
        signup.addActionListener(this);
        jpf.addKeyListener(this);
        foreye.addMouseListener(this);
    }

    public static void main(String[] args) {
        new LoginPage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==login) {
            boolean userexists =true;
            JSONObject obj = null;
            try {
                obj = (JSONObject) new JSONParser().parse(new FileReader(".\\Data.JSON"));
            } catch (IOException | ParseException ex) {

                ex.printStackTrace();
            }
            String user = jtf.getText();

            JSONArray ja = (JSONArray) (obj.get("login"));
            Iterator itr = ja.iterator();
            JSONObject all =null;
            while(itr.hasNext())
            {
                all =(JSONObject) itr.next();
                System.out.println(all);
                        if(!all.get("uname").equals(user)||!all.get("pass").equals(String.valueOf(jpf.getPassword())))
                        {
                            userexists=false;
                            noy.setText("User Or Password Not Correct");
                        }
                        else {
                            noy.setText("");
                            userexists=true;
                            break;
                        }
            }
            if(userexists) {
                new Main(all);
                frame.dispose();
            }
            }
        else if(e.getSource()==fpass)
        {
            frame.setVisible(false);
            new ForgetPassword();
        }
        else if(e.getSource()==signup)
        {
            frame.setVisible(false);
            new SignUp();
        }
    }
    public void keyPressed(KeyEvent event)
    {
        if(event.getKeyChar()==KeyEvent.VK_ENTER)
        {
            login.doClick();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(foreye.getIcon()==icon) {
            foreye.setIcon(icon2);
            jpf.setEchoChar((char)0);
        }
        else {
            foreye.setIcon(icon);
            jpf.setEchoChar('*');
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
