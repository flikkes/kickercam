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
import javafx.scene.layout.BorderPane;
import videoProcessing.ResizableJavaFXPlayerTest;

/**
 *
 * @author flikkes
 */
public class FXMLDocumentController implements Initializable {
    
    private ResizableJavaFXPlayerTest player;
    @FXML private BorderPane borderPane;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        VideoStreamer videoStreamer = new VideoStreamer("http://10.10.10.11:8081/", this.mediaView);
//        System.out.println("starting to stream...");
//        videoStreamer.start();
        this.player = new ResizableJavaFXPlayerTest("http://10.10.10.11:8081/");
        this.borderPane.getChildren().add(player.getPane());
        
//        WebcamService webcamService = new WebcamService("http://10.10.10.11:8081/");
//        MediaPlayer ply1 = webcamService.getMediaPlayer();
//        this.imageView.setImage(null);
//        ply1.play();
    }

    @FXML private void displayVideo() {
        this.player.play();
    }
    @FXML private void pauseVideo() {
        this.player.stop();
    }
    
}
