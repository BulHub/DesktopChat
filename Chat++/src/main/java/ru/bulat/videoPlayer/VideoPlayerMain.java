package ru.bulat.videoPlayer;

import javax.media.bean.playerbean.MediaPlayer;
import javax.swing.*;
import java.awt.*;

public class VideoPlayerMain extends JFrame{

    public static void goToVideoPlayer(){

    }

    private VideoPlayerMain(String path){
        super("Простой видео плеер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(640,480));
        MediaPlayer player = new MediaPlayer();
        player.setMediaLocation(path);
        player.setPlaybackLoop(false);//Повтор видео
        player.prefetch();
        add(player);
        setVisible(true);
    }

    public static void main(String []args){
        VideoPlayerMain ve = new VideoPlayerMain("C:\\Users\\sereg\\Desktop\\My_work\\BulHub\\DesktopChat\\Chat++\\src\\main\\resources\\video\\1.mp4");
    }
}