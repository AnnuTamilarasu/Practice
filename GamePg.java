import javax.swing.*;
import java.awt.*;

public class GamePg extends JPanel {
    JButton[] nums = new JButton[9];
    JPanel numPanel;
    JLabel background;
    String bgImgPath = "C:/Users/anany/Downloads/Untitled design (8).png";
    JPanel bgPanel;
    Sudoku puz = new Sudoku();
    int selectedNumber = -1; // tracks current selection

    public GamePg(){
        this.setLayout(null);

        // background panel FIRST
        bgPanel = new JPanel(null);
        bgPanel.setBounds(0, 0, 1500, 750);
        this.add(bgPanel);

        // add background image
        bg(bgImgPath);

        // add Sudoku board on top
        this.add(puz);
        puz.setVisible(true);

        // add number bar on top
        numBar();

        displayTime();

        // make sure background stays behind everything
        this.setComponentZOrder(bgPanel, this.getComponentCount() - 1);
        this.setVisible(true);
    }

    public void bg(String path) {
        ImageIcon bgIcon = new ImageIcon(path);
        background = new JLabel(bgIcon);
        background.setBounds(0, -25, 1500, 800);
        bgPanel.add(background);
    }

    public void numBar(){
        numPanel = new JPanel();
        numPanel.setBounds(300,600,500,50);
        numPanel.setLayout(new GridLayout(1,9));
        numPanel.setBackground(Color.WHITE);

        for(int i=1; i<=9; i++){
            JButton btn = new JButton(Integer.toString(i));
            btn.addActionListener(e -> {
                selectedNumber = Integer.parseInt(btn.getText());
                puz.setSelectedNumber(selectedNumber);
            });
            numPanel.add(btn);
            nums[i-1] = btn;
        }

        this.add(numPanel);
    }

    public void displayTime() {
        JLabel t = new JLabel("00:00");
        t.setBounds(50, 20, 100, 30);
        this.add(t);
        final int[] startTime = {0};
            startTime[0]= -2;
        Timer time = new Timer(1000, e -> {
            startTime[0]++;
            int min = startTime[0] / 60;
            int sec = startTime[0] % 60;

            t.setText(String.format("%02d:%02d", min, sec));
        });
        time.start();
    }
}
