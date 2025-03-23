package package1;

import javax.swing.*;

public class Tetris extends JFrame {
    public final int WIDTH = 700, HEIGHT = 800;
    public Tetris(String command) { //command 用來判斷要創建哪一個panel
        this.setTitle("Tetris Test");
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (command.equals("CLASSIC")) {
            //把現在已經創建好的 Tetris extends Jframe 物件 傳到 TetrisPanel 中，這樣可以在 TetrisPanel 中刪掉這個遊戲JFrame
            TetrisMod1 panel = new TetrisMod1(this);
            panel.setBounds(0, 0, WIDTH, HEIGHT);
            add(panel);
            addKeyListener(panel);
        }
        else if (command.equals("40LINES")) {
            //把現在已經創建好的 Tetris extends Jframe 物件 傳到 TetrisPanel 中，這樣可以在 TetrisPanel 中刪掉這個遊戲JFrame
            TetrisPanel40Lines panel = new TetrisPanel40Lines(this);
            panel.setBounds(0, 0, WIDTH, HEIGHT);
            add(panel);
            addKeyListener(panel);
        }
    }

    public void returnToMainMenu() { //把現在這個 Tetris extends Jframe 物件刪掉，並新創見一個mainMenu
        this.dispose();
        MainMenu mainMenu = new MainMenu();
        // 使窗口居中顯示
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
    }
}