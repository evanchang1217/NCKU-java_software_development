package package1;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    private Clip clip;

    public void playSound(String soundFilePath) {
        try {
            // 获取音频输入流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));

            // 获取音频剪辑
            clip = AudioSystem.getClip();

            // 打开音频剪辑并加载音频输入流
            clip.open(audioInputStream);

            // 开始播放音效并循环
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // 播放音效
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 停止播放音效的方法
    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

}
