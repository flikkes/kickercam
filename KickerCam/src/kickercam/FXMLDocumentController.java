/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kickercam;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import videoProcessing.ResizableJavaFXPlayer;
import videoProcessing.SplitViewPlayer;

/**
 *
 * @author flikkes
 */
public class FXMLDocumentController implements Initializable {

    private ResizableJavaFXPlayer player;
    private SplitViewPlayer splitViewPlayer;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button playButton, splitViewButton;

    String testURL = "http://10.10.10.11:8081/";
    
    List<String> sources = new ArrayList<>();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.player = new ResizableJavaFXPlayer(testURL, 480, 320);
        String[] sources = {
            testURL,
            testURL,
            testURL,
            testURL};
        this.splitViewPlayer = new SplitViewPlayer(sources, 480, 320);

    }

    @FXML
    private void toggleSingleVideo() {
        this.borderPane.getChildren().clear();
        this.borderPane.getChildren().add(this.player.getPane());
        if (this.player.isPlaying()) {
            this.player.pause();
            this.playButton.setText("Play");

        } else {
            this.splitViewPlayer.pauseSplitView();
            this.player.play();
            this.splitViewButton.setText("Play Split View");
            this.playButton.setText("Pause");
        }
    }

    @FXML
    private void toggleSplitView() {
        this.borderPane.getChildren().clear();
        for (Pane p : this.splitViewPlayer.getPanes()) {
            this.borderPane.getChildren().add(p);
        }
        if (this.splitViewPlayer.isPlaying()) {
            this.splitViewPlayer.pauseSplitView();
            this.splitViewButton.setText("Play Split View");
        } else {
            this.player.pause();
            this.splitViewPlayer.playSplitView();
            this.playButton.setText("Play");
            this.splitViewButton.setText("Pause Split View");
        }
    }
    
    @FXML
    private void toggleNextTable() {
        
    }
    
    @FXML
    private void togglePreviousTable() {
        
    }
    
    @FXML
    private void addCamSatellite() {
        
    }
    
    @FXML
    private void addVideoClip() {
        
    }
}
