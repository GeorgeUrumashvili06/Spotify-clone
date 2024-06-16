package com.example.spotifyapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HelloController {
    @FXML
    private ListView<String> listView;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Button seekForwardButton;

    @FXML
    private Button seekBackwardButton;

    private MediaPlayer mediaPlayer;

    private final List<String> songList = new ArrayList<>();
    private final List<String> songPathList = new ArrayList<>();
    private boolean isPaused = false;

    @FXML
    private void initialize() {
        addDefaultSongs();

        listView.getItems().addAll(songList);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> playSong(newValue));

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        seekForwardButton.setOnAction(event -> seekForward());
        seekBackwardButton.setOnAction(event -> seekBackward());
    }

    private void addDefaultSongs() {
        songList.add("A$AP Rocky - Everyday (Official Audio) ft. Rod Stewart, Miguel, Mark Ronson.mp3");
        songPathList.add(getClass().getResource("/musics/A$AP Rocky - Everyday (Official Audio) ft. Rod Stewart, Miguel, Mark Ronson.mp3").toString());

        songList.add("Laura Branigan - Self Control (Official Music Video).mp3");
        songPathList.add(getClass().getResource("/musics/Laura Branigan - Self Control (Official Music Video).mp3").toString());

        songList.add("Luna Flowers, Sabana - FlowerWild (Music Video).mp3");
        songPathList.add(getClass().getResource("/musics/Luna Flowers, Sabana - FlowerWild (Music Video).mp3").toString());

        songList.add("M83 'Midnight City' Official video.mp3");
        songPathList.add(getClass().getResource("/musics/M83 'Midnight City' Official video.mp3").toString());
    }

    @FXML
    private void playSong(String song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        int songIndex = songList.indexOf(song);
        if (songIndex == -1) {
            System.err.println("Song not found: " + song);
            return;
        }

        String songPath = songPathList.get(songIndex);

        try {
            Media media = new Media(songPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> playNextSong());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void playSelectedSong() {
        String selectedSong = listView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            if (isPaused) {
                mediaPlayer.play();
                isPaused = false;
            } else {
                playSong(selectedSong);
            }
        }
    }

    @FXML
    private void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    @FXML
    private void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    @FXML
    private void seekBackward() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
        }
    }

    @FXML
    private void seekForward() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
        }
    }

    @FXML
    private void addSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String fileName = file.getName();
            String filePath;
            try {
                filePath = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return;
            }
            songList.add(fileName);
            songPathList.add(filePath);
            listView.getItems().add(fileName);
        }
    }

    @FXML
    private void removeSong() {
        String selectedSong = listView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            int selectedIndex = songList.indexOf(selectedSong);
            if (selectedIndex != -1) {
                songList.remove(selectedIndex);
                songPathList.remove(selectedIndex);
                listView.getItems().remove(selectedSong);
                stopSong();
            }
        }
    }

    @FXML
    private void moveSongUp() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            Collections.swap(songList, selectedIndex, selectedIndex - 1);
            Collections.swap(songPathList, selectedIndex, selectedIndex - 1);
            listView.getItems().setAll(songList);
            listView.getSelectionModel().select(selectedIndex - 1);
        }
    }

    @FXML
    private void moveSongDown() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < listView.getItems().size() - 1) {
            Collections.swap(songList, selectedIndex, selectedIndex + 1);
            Collections.swap(songPathList, selectedIndex, selectedIndex + 1);
            listView.getItems().setAll(songList);
            listView.getSelectionModel().select(selectedIndex + 1);
        }
    }

    private void playNextSong() {
        int currentSongIndex = listView.getSelectionModel().getSelectedIndex();
        int nextSongIndex = (currentSongIndex + 1) % songList.size();
        listView.getSelectionModel().select(nextSongIndex);
        playSong(songList.get(nextSongIndex));
     }
}

