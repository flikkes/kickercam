/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kickercam;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import videoProcessing.ResizableJavaFXPlayerTest;
import videoProcessing.SplitViewPlayer;

/**
 *
 * @author flikkes
 */
public class FXMLDocumentController implements Initializable {

    private ResizableJavaFXPlayerTest player;
    private SplitViewPlayer splitViewPlayer;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button playButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.player = new ResizableJavaFXPlayerTest("http://10.10.10.11:8081/", 480, 320);
        String[] sources = {"http://10.10.10.11:8081/", "http://10.10.10.11:8081/", "http://10.10.10.11:8081/", "http://10.10.10.11:8081/"};
        this.splitViewPlayer = new SplitViewPlayer(sources, 480, 320);
        

    }

    @FXML
    private void displayVideo() {
        this.borderPane.getChildren().clear();
        this.borderPane.getChildren().add(this.player.getPane());
        if (this.player.isPlaying()) {
            this.player.pause();
            this.playButton.setText("Play");

        } else {
            this.splitViewPlayer.pauseSplitView();
            this.player.play();
            this.playButton.setText("Pause");
        }
    }

    @FXML
    private void toggleSplitView() {
        Pane[] splitPanes = this.splitViewPlayer.getPanes();
        this.borderPane.getChildren().clear();
//        this.borderPane.getChildren().add(this.splitViewPlayer.getSplitView());
        for (Pane p : splitPanes) {
            this.borderPane.getChildren().add(p);
        }
        if (this.splitViewPlayer.isPlaying()) {
            this.splitViewPlayer.pauseSplitView();
        } else {
            this.player.pause();
            this.splitViewPlayer.playSplitView();
        }
    }
}
