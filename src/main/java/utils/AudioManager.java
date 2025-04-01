/**
 * @author: Emily Júnia, Micael Pereira, Nífane Borges, Vinícius Alves Amorim
 * @description: Gerenciador de efeitos sonoros e música de fundo
 */


package utils;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.net.URL;

public class AudioManager {
    private Clip backgroundMusic;
    
    // Inicializa o gerenciador de áudio
    public AudioManager() {
        // Pré-carrega a música de fundo
        try {
            URL musicURL = getClass().getResource("/resources/audio/background_music.wav");
            if (musicURL != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicURL);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
                
                // Configura para loop contínuo
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erro ao carregar música de fundo: " + e.getMessage());
        }
    }
    
    // Toca a música de fundo
    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.start();
        }
    }
    
    // Toca um efeito sonoro de botão
    public void playButtonSound() {
        try {
            URL soundURL = getClass().getResource("/resources/audio/button_click.wav");
            if (soundURL != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                // Auto-fecha o clip após a reprodução
                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erro ao reproduzir efeito sonoro: " + e.getMessage());
        }
    }
}
