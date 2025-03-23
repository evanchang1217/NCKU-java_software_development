package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame{
    public final int WIDTH = 700, HEIGHT = 800;



    public MainMenu() {
        this.setTitle("Tetris");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        BackgroundPanel panel = new BackgroundPanel(this);
        panel.setLayout(null);
        panel.setBounds(0, 0, WIDTH, HEIGHT); // 覆蓋整個window

        

        this.add(panel);

        this.setVisible(true);
    }


    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();


        SoundPlayer backPlayer = new SoundPlayer();
        backPlayer.playSound("src/music.wav");

        // 使窗口居中顯示
        mainMenu.setLocationRelativeTo(null);

        mainMenu.setVisible(true);
    }
}
