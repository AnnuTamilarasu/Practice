import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HomePg extends JFrame implements ActionListener {
    JButton play, tutorial;
    JPanel bgPanel;
    JLabel background;
    String bgImgPath = "C:/Users/anany/Downloads/Sudoku.png";
    GamePg game = new GamePg();
    public HomePg() {
        setBounds(7, 12, 1510, 785);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        bgPanel = new JPanel(null);
        bgPanel.setBounds(0, 0, 1500, 750);
        add(bgPanel);
        homePanel();
        setVisible(true);
    }
    public void homePanel(){
        bg(bgImgPath);
        play = clearButton(0,0,play,"play");
        tutorial = clearButton(0,50,tutorial,"tutorial");
        play.addActionListener(this);
        tutorial.addActionListener(this);
        bgPanel.add(play);
        bgPanel.add(tutorial);
        bgPanel.setComponentZOrder(background, bgPanel.getComponentCount() - 1);

        setVisible(true);
    }

    public void bg(String path) {
        ImageIcon bgIcon = new ImageIcon(path);
        background = new JLabel(bgIcon);
        background.setBounds(0,-25, 1500, 800);
        bgPanel.add(background);
    }

    public JButton clearButton(int x, int y,JButton button, String text){
        button = new JButton(text);
        button.setBounds(x, y, 120, 50);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setContentAreaFilled(false); // remove fill
        button.setBorderPainted(false);     // remove border
        button.setFocusPainted(false);      // remove focus outline
        button.setForeground(Color.WHITE);  // text color, matches bg
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play ){
            getContentPane().removeAll();
            game.setBounds(0, 0, getWidth(), getHeight());  // make it fill frame
            getContentPane().add(game);
            revalidate();
            repaint();
            System.out.println("Play");
        }else if(e.getSource() == tutorial){
            System.out.println("Tutorial");
        }
    }
}
/*
    public HomePg(){
        homePanel = new JPanel();
        homePanel.setLayout(null);
        homePanel.setBackground(Color.YELLOW);
        homePanel.add(bg());
        homePanel.setVisible(true);
        homePanel.add(new GamePg());
        homePanel.add(clearButton(0,0,play,"play"));
    }

     */