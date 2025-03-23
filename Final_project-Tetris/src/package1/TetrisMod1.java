package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

class TetrisMod1 extends JPanel implements KeyListener {

    private Tetris parentFrame;  // 用於從遊戲介面回到 mainMenu

    public int score = 0, level = 1,line=0;
    public int[][] map = new int[10][20];
    private int blockType;
    private int turnState;
    private int x, y, hold, next, change;
    private int flag = 0;
    Image[] color = new Image[7];
    // [] S、Z、L、J、I、O、T [] Rotate [] 4*4
    Block block = new Block();
    // Accessing shapes array
    int[][][] shapes = block.shapes;
    JLabel ScoreValueLabel;
    JLabel LevelValueLabel;

    Timer downTimer; // 用於計算 downShift 內部何時要繪圖上去
    Timer delEffect; //用於計算刪除動畫

    private boolean isKeyListenerEnabled = true; //用於不給輸入

    JLabel HoldLabel, NextLabel, LineLabel, ScoreLabel, gameOverLabel;

    private boolean isGameOver = false;

////////////////////////
    SoundPlayer soundPlayer = new SoundPlayer();

    //更新timer
    int newtimer=530;
    Timer timer = new Timer(newtimer, new TimerListener());

    public TetrisMod1(Tetris parentFrame) {

        this.parentFrame = parentFrame; //接收frame 藉以回到mainMenu

        this.setLayout(null);
        this.setBackground(Color.BLACK);
        initializeLabels();
        color = block.color;
        initMap();
        newBlock();
        hold = -1;
        next = (int) (Math.random() * 7);

        timer.start();
    }

    public void initializeLabels(){
        // Add labels
        HoldLabel = new JLabel("   HOLD");
        HoldLabel.setForeground(Color.BLACK);
        HoldLabel.setBounds(11, 11, 149, 50);  //hold block put in 30,60
        HoldLabel.setFont(new Font("Arial", Font.BOLD, 35)); // Set font size and bold
        HoldLabel.setOpaque(true); // Set opaque to true
        HoldLabel.setBackground(Color.LIGHT_GRAY); // Set background color
        this.add(HoldLabel);

        NextLabel = new JLabel("   NEXT");
        NextLabel.setForeground(Color.BLACK);
        NextLabel.setBounds(521, 11, 149, 50); // next block put in 480,60
        NextLabel.setFont(new Font("Arial", Font.BOLD, 35)); // Set font size and bold
        NextLabel.setOpaque(true); // Set opaque to true
        NextLabel.setBackground(Color.LIGHT_GRAY); // Set background color
        this.add(NextLabel);


        LineLabel = new JLabel("   LEVEL");
        LineLabel.setForeground(Color.LIGHT_GRAY);
        LineLabel.setBounds(530, 505, 150, 50); // next block put in 480,60
        LineLabel.setFont(new Font("Arial", Font.BOLD, 25)); // Set font size and bold
        this.add(LineLabel);

        LevelValueLabel = new JLabel("   " + level);
        LevelValueLabel.setForeground(Color.LIGHT_GRAY);
        LevelValueLabel.setBounds(560, 555, 150, 50); // next block put in 480,60
        LevelValueLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Set font size and bold
        this.add(LevelValueLabel);

        ScoreLabel = new JLabel("SCORE");
        ScoreLabel.setForeground(Color.LIGHT_GRAY);
        ScoreLabel.setBounds(20, 505, 150, 50); // next block put in 480,60
        ScoreLabel.setFont(new Font("Arial", Font.BOLD, 25)); // Set font size and bold
        this.add(ScoreLabel);

        ScoreValueLabel = new JLabel("" + score);
        ScoreValueLabel.setForeground(Color.LIGHT_GRAY);
        ScoreValueLabel.setBounds(80, 555, 150, 50); // next block put in 480,60
        ScoreValueLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Set font size and bold
        this.add(ScoreValueLabel);
    }

    public void newBlock() {
        flag = 0;
        blockType = next;
        change = 1;
        next = (int) (Math.random() * 7);
        turnState = 0;
        x = 4;
        y = 0;
        if (gameOver(x, y) == 1) {
            isGameOver = true;

            //score=0;
            //line=0;
            //level=1;
            //newtimer=530;
            //initMap();
            //String str = score + "";
            //ScoreValueLabel.setText(str);
            //add(ScoreValueLabel);

        }
        repaint();
    }

    public void setBlock(int x, int y, int type, int state) {
        flag = 1;
        for (int i = 0; i < 16; i++) {
            if (shapes[type][state][i] == 1) {
                map[x + i % 4][y + i / 4] = type + 1;
            }
        }
    }

    public int gameOver(int x, int y) { //1 -> over
        if (blow(x, y, blockType, turnState) == 0){
            return 1;
        }
        return 0;
    }

    public int blow(int x, int y, int type, int state) { //0 -> cannot put
        for (int i = 0; i < 16; i++) {
            if (shapes[type][state][i] == 1) {
                if (x + i % 4 >= 10 || y + i / 4 >= 20 || x + i % 4 < 0 || y + i / 4 < 0)
                    return 0;
                if (map[x + i % 4][y + i / 4] > 0)
                    return 0;
            }
        }
        return 1;
    }

    public void rotate() {
        int tmpState = turnState;
        tmpState = (tmpState + 1) % 4;
        if (blow(x, y, blockType, tmpState) == 1) {
            turnState = tmpState;
        }
        repaint();
    }

    public void r_shift() {
        int canShift = 0;
        if (blow(x + 1, y, blockType, turnState) == 1) {
            x++;
            canShift = 1;
        }
        repaint();
        //return canShift;
    }

    public void l_shift() {
        if (blow(x - 1, y, blockType, turnState) == 1) {
            x--;
        }
        repaint();
    }

    public int down_shift(int isSpace) {

        if(isSpace == 1 && downTimer != null && downTimer.isRunning()) //輸入是空白建
            downTimer.stop();


        int canDown = 0;
        if (blow(x, y + 1, blockType, turnState) == 1) {
            y++;
            canDown = 1;
        }
        repaint();

        if ((downTimer == null || !downTimer.isRunning()) && isSpace == 0 && blow(x, y + 1, blockType, turnState) == 0) { //計時器未啟動的情況下
            //Sleep(500);


            downTimer = new Timer(1200, new ActionListener() { //更改延遲
                public void actionPerformed(ActionEvent e) {

                    if (blow(x, y + 1, blockType, turnState) == 0) {  //如果時間到了而且所在位置是不合法的
                        setBlock(x, y, blockType, turnState);
                        newBlock();
                        delLine();

                    }
                    downTimer.stop(); // 停止计时器
                }
            });
            downTimer.start();

        }
        else if (isSpace == 1 && blow(x, y + 1, blockType, turnState) == 0) { //按空白鍵的情況不用計時
            //Sleep(500);
            setBlock(x, y, blockType, turnState);
            newBlock();
            delLine();
            canDown = 0;
        }
        return canDown;
    }

    void delLine() {
        int idx = 19, access = 0;
        int linecnt=0;
        for (int i = 19; i >= 0; i--) {
            int cnt = 0;
            for (int j = 0; j < 10; j++) {
                if (map[j][i] > 0)
                    cnt++;
            }
            if (cnt ==10) {
                clearshadow();
                access = 1;
                for (int j = 0; j < 10; j++) {
                    map[j][i] = 69;
                }
                linecnt++;
                line++;


                //level
                if(line!=0&&line%3==0){
                    level++;
                    String str = level + "";
                    LevelValueLabel.setText(str);
                    add(LevelValueLabel);

                    newtimer-=40;
                    timer.setDelay(newtimer);
                }


            }
        }

        //刪除動畫暫停一切程式執行
        if(linecnt > 0) {
            repaint();
            timer.stop();
            isKeyListenerEnabled = false;

            delEffect = new Timer(150, new ActionListener() { //更改延遲
                public void actionPerformed(ActionEvent e) {

                    delEffect.stop(); // 停止计时器
                    timer.start();
                    isKeyListenerEnabled = true;
                    repaint();
                }
            });
            delEffect.start();
        }
        if(linecnt>0) {
///////////////////////////
            Thread soundThread = new Thread(() -> {
                soundPlayer.playSound("src/sound.wav");  // 开始播放音效
                try {
                    Thread.sleep(400);  // 播放1秒钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                soundPlayer.stopSound();  // 停止播放音效
            });

            soundThread.start();  // 启动音效播放线程
//////////////////////////////////////
        }

        if(linecnt==1){
            score+=1;
            String str = score + "";
            ScoreValueLabel.setText(str);
            add(ScoreValueLabel);
        }else if(linecnt==2){
            score+=3;
            String str = score + "";
            ScoreValueLabel.setText(str);
            add(ScoreValueLabel);
        }else if(linecnt==3){
            score+=5;
            String str = score + "";
            ScoreValueLabel.setText(str);
            add(ScoreValueLabel);
        }else if(linecnt==4){//tetris4
            score+=10;
            String str = score + "";
            ScoreValueLabel.setText(str);
            add(ScoreValueLabel);
        }
    }

    void initMap() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 20; j++)
                map[i][j] = 0;
    }
    public void clearshadow(){
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                if (map[i][j] == -1) {
                    map[i][j] = 0;
                }
            }
        }
    }

    public void shadow(){
        int xtmp=x;
        int ytmp=y;
        while(shadow_down_shift() == 1)
            x=xtmp;
        y=ytmp;
    }
    public int shadow_down_shift() {
        int canDown = 0;
        if (shadow_blow(x, y + 1, blockType, turnState) == 1) {
            y++;
            canDown = 1;
        }

        if (shadow_blow(x, y + 1, blockType, turnState) == 0) {
            //Sleep(500);
            //System.out.println("good");
            shadow_setBlock(x, y, blockType, turnState);

            canDown = 0;
            //repaint();
        }

        return canDown;
    }

    public int shadow_blow(int x, int y, int type, int state) {
        for(int i = 0; i < 16; i++) {
            if(shapes[type][state][i] == 1) {
                if(x+i%4 >= 10 || y+i/4 >= 20 || x+i%4 < 0 || y+i/4 < 0)
                    return 0;
                if(map[x+i%4][y+i/4] != 0)
                    return 0;
            }
        }
        return 1;
    }
    public void shadow_setBlock(int x, int y, int type, int state) {
        //System.out.println("good");
        for(int i = 0; i < 16; i++) {
            if(shapes[type][state][i] == 1) {
                map[x+i%4][y+i/4] = -1;

            }
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (isGameOver) {
            gameOverLabel = new JLabel("");
            gameOverLabel.setForeground(Color.WHITE);
            gameOverLabel.setFont(new Font("Arial", Font.BOLD, 60));
            gameOverLabel.setBounds(50, 150, 600, 200); // Adjust the position and size as needed
            gameOverLabel.setForeground(Color.RED);
            gameOverLabel.setText("Game Over");
            gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel ReturnToMainLabel = new JLabel("");
            ReturnToMainLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            ReturnToMainLabel.setBounds(50, 200, 600, 200); // Adjust the position and size as needed
            ReturnToMainLabel.setForeground(Color.WHITE);
            ReturnToMainLabel.setText("Press ESC to return to mainMenu");
            ReturnToMainLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel finalScoreLabel = new JLabel();
            finalScoreLabel.setFont(new Font("Arial", Font.ITALIC, 40));
            finalScoreLabel.setBounds(50, 400, 600, 200); // Adjust the position and size as needed
            finalScoreLabel.setForeground(Color.MAGENTA);
            finalScoreLabel.setText("SCORE : " + score );
            finalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

            this.add(gameOverLabel);
            this.add(ReturnToMainLabel);
            this.add(finalScoreLabel);

            this.remove(ScoreValueLabel);
            this.remove(NextLabel);
            this.remove(HoldLabel);
            this.remove(ScoreLabel);
            this.remove(ScoreLabel);
            this.remove(LineLabel);
            this.remove(LevelValueLabel);
            return;
        }

        clearshadow();
        shadow();


        //block for word
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(10, 10, 150 , 180 );
        g.drawRect(520, 10, 150 , 180 );
        //tetris
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                if (map[i][j] == 69) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(i * 30 + 3 * (i + 1) + 180, j * 30 + 3 * (j + 1), 30, 30);
                }
                else if (map[i][j] == 0) {
                    g.setColor(Color.GRAY.darker());
                    g.fillRect(i * 30 + 3 * (i + 1) + 180, j * 30 + 3 * (j + 1), 30, 30);
                } else if(map[i][j] == -1){


                    Image originalImage = color[blockType];
                    BufferedImage bufferedImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
                    //BufferedImage bufferedImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D bGr = bufferedImage.createGraphics();
                    float opacity = 0.3f; // 15% opacity
                    bGr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    bGr.drawImage(originalImage, 0, 0, null);
                    bGr.dispose();

                    // Now draw the BufferedImage onto the panel
                    g.drawImage(bufferedImage, i * 30 + 3 * (i + 1) + 180, j * 30 + 3 * (j + 1), null);
                } else
                    g.drawImage(color[map[i][j] - 1], i * 30 + 3 * (i + 1) + 180, j * 30 + 3 * (j + 1), null);
            }
        }


        if (flag == 0) {
            for (int i = 0; i < 16; i++) {
                if (shapes[blockType][turnState][i] == 1) {
                    g.drawImage(color[blockType], (i % 4 + x) * 33 + 3 + 180, (i / 4 + y) * 33 + 3, null);
                }
            }
        }
        if (hold >= 0) {
            for (int i = 0; i < 16; i++) {
                if (shapes[hold][0][i] == 1) {
                    g.drawImage(color[hold], (i % 4) * 33 + 20, (i / 4) * 33 + 3 + 80, null);
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            if (shapes[next][0][i] == 1) {
                g.drawImage(color[next], (i % 4) * 33 + 530, (i / 4) * 33 + 3 + 80, null);
            }
        }

        // 將map中的69變回0並將方塊下移
        int idx = 19;
        int linecnt=0;
        for (int i = 19; i >= 0; i--) {
            int cnt = 0;
            for (int j = 0; j < 10; j++) {
                if (map[j][i] == 69)
                    cnt++;
            }
            if (cnt ==10) {
                linecnt++;

            } else {
                for (int j = 0; j < 10; j++) {
                    map[j][idx] = map[j][i];
                    if(linecnt > 0)
                        map[j][i] = 0;
                }
                idx--;
            }
        }

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (!isKeyListenerEnabled) { //用於暫停輸入
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                down_shift(0);
                break;
            case KeyEvent.VK_UP:
                rotate();
                break;
            case KeyEvent.VK_X:
                rotate();
                break;
            case KeyEvent.VK_RIGHT:
                r_shift();
                break;
            case KeyEvent.VK_LEFT:
                l_shift();
                break;
            case KeyEvent.VK_SPACE:
                while (down_shift(1) == 1) ;
                break;
            case KeyEvent.VK_C:
                if (hold >= 0 && change == 1) {
                    int tmp;
                    tmp = hold;
                    hold = blockType;
                    blockType = tmp;
                    x = 4;
                    y = 0;
                    change = 0;
                } else if (change == 1) {
                    hold = blockType;
                    newBlock();

                }
                break;
            case KeyEvent.VK_ESCAPE: //type esc to return to mainMenu
                parentFrame.returnToMainMenu();
                break;
        }
    }

    void Sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Unexcepted interrupt");
            System.exit(0);
        }
    }

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            repaint();
            down_shift(0);
        }
    }
}


