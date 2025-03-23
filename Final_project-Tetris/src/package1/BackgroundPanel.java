package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BackgroundPanel extends JPanel implements ActionListener{  //用於繪製mainMenu的背景
    private Image backgroundImage;
    private MainMenu menu;

    public BackgroundPanel(MainMenu mainMenu) {
    	menu = mainMenu;
        // 加载背景图片
        backgroundImage = new ImageIcon("back.png").getImage();
      //mod1
        JButton bt1 = new JButton("CLASSIC");
        bt1.setFont(new Font("", Font.BOLD, 40));
        bt1.setForeground(Color.WHITE);
        //startButton.setBackground(Color.DARK_GRAY);
        bt1.setBackground(new Color(30, 144, 255));

        bt1.addActionListener(this);

        bt1.setFocusPainted(false);  //禁用按钮的默认焦点绘制效果

        bt1.addMouseListener(new java.awt.event.MouseAdapter() { //鼠标悬停在按钮上时，改变背景颜色
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bt1.setBackground(Color.green.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bt1.setBackground(new Color(30, 144, 255));
            }

        });


        JLabel titleLabel = new JLabel("TETRIS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 140));
        titleLabel.setForeground(new Color(220, 220, 220)); // 比较深的灰色
        titleLabel.setBounds(100, 100, 500, 150); // 设置位置和大小 放置在中間
        this.add(titleLabel);

        //mod2
        JButton bt2 = new JButton("40LINES");

        bt2.setFont(new Font("", Font.BOLD, 40));
        bt2.setForeground(Color.WHITE);
        //startButton.setBackground(Color.DARK_GRAY);
        bt2.setBackground(Color.gray);

        bt2.addActionListener(this);

        bt2.setFocusPainted(false);  //禁用按钮的默认焦点绘制效果

        bt2.addMouseListener(new java.awt.event.MouseAdapter() { //鼠标悬停在按钮上时，改变背景颜色
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bt2.setBackground(Color.green.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bt2.setBackground(Color.gray);
            }

        });


        bt1.setLocation(225, 350);
        bt1.setSize(250, 100);

        bt2.setLocation(225, 500);
        bt2.setSize(250, 100);


        this.add(bt1);
        this.add(bt2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    
    public void actionPerformed(ActionEvent e) { //給所有button使用的
    	menu.dispose(); //把現在這個 mainMenu 丟掉
        Tetris tetris = new Tetris(e.getActionCommand());  //進入遊戲

        // 使窗口居中顯示
        tetris.setLocationRelativeTo(null);

        tetris.setVisible(true);
    }
}
