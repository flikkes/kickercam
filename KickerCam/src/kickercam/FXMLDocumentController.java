/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kickercam;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.CamSatellite;
import model.CamSatelliteCollection;
import networking.URLScanner;
import videoProcessing.ResizableJavaFXPlayer;
import videoProcessing.SplitViewPlayer;

/**
 *
 * @author flikkes
 */
public class FXMLDocumentController implements Initializable {

    private ResizableJavaFXPlayer player;
    private SplitViewPlayer splitViewPlayer;
    private CamSatelliteCollection camSatelliteCollection;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button playButton, splitViewButton;

    @FXML
    private TextField urlTextField, portTextField, fromTextField, toTextField;

    @FXML
    private TableView tableView;

    String testURL = "http://10.10.10.11:8081/";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.camSatelliteCollection = new CamSatelliteCollection();

//        URLScanner uRLScanner = new URLScanner();
//        List<String> sources = null;
//        String[] sourcesArray;
//        try {
//            sources = uRLScanner.scanRange("http://10.10.10.", 0, 20, 8081);
//        } catch (Exception ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        if (sources != null) {
//            sourcesArray = new String[sources.size()];
//            for (int i = 0; i < sources.size(); i++) {
//                this.camSatelliteCollection.addCamSatellite(new CamSatellite("Table " + (i + 1), sources.get(i)));
//                sourcesArray[i] = sources.get(i);
//            }
//            this.splitViewPlayer = new SplitViewPlayer(sourcesArray, 480, 320);
//            this.player = new ResizableJavaFXPlayer(this.camSatelliteCollection.next().getVideoSource(), 480, 320);
//        }
    }

    @FXML
    private void toggleSingleVideo() {
        if (this.player != null) {
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
    }

    @FXML
    private void toggleSplitView() {
        if (this.splitViewPlayer != null) {
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
    }

    @FXML
    private void toggleNextTable() {
        boolean toggle = this.player.isPlaying();
        if (toggle) {
            toggleSingleVideo();
        }
        this.player.setVideoSource(this.camSatelliteCollection.next().getVideoSource());
        if (toggle) {
            toggleSingleVideo();
        }
    }

    @FXML
    private void togglePreviousTable() {
        boolean toggle = this.player.isPlaying();
        if (toggle) {
            toggleSingleVideo();
        }
        this.player.setVideoSource(this.camSatelliteCollection.prev().getVideoSource());
        if (toggle) {
            toggleSingleVideo();
        }
    }
    /**
     * Uses {@link URLScanner} to search for active {@link CamSatellite Cam Satellites} in the Network.
     * 
     */
    @FXML
    private void scanForCamSatellites() {
        URLScanner uRLScanner = new URLScanner();
        List<String> sources = null;
        String[] sourcesArray;
        String urlText = this.urlTextField.getText();
        int fromIpRange = Integer.parseInt(this.fromTextField.getText());
        int toIpRange = Integer.parseInt(this.toTextField.getText());
        int port = Integer.parseInt(this.portTextField.getText());
        
        try {
            sources = uRLScanner.scanRange("http://" + urlText + ".", fromIpRange, toIpRange, port);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (sources != null && sources.size() > 0) {
            sourcesArray = new String[sources.size()];
            for (int i = 0; i < sources.size(); i++) {
                
                CamSatellite sat = new CamSatellite("Table " + (i + 1), sources.get(i));
                this.camSatelliteCollection.addCamSatellite(sat);
                sourcesArray[i] = sources.get(i);
            }
            
            ObservableList<CamSatellite> observableCamSatelliteList = this.camSatelliteCollection.getObservableList();
            
            TableColumn<CamSatellite, String> nameColumn = new TableColumn<>();
            TableColumn<CamSatellite, String> urlColumn = new TableColumn<>();

            nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
            urlColumn.setCellValueFactory(new PropertyValueFactory("videoSource"));
            
            this.tableView.setItems(observableCamSatelliteList);
            this.tableView.getColumns().setAll(nameColumn, urlColumn);

            if (this.splitViewPlayer == null) {
                this.splitViewPlayer = new SplitViewPlayer(sourcesArray, 480, 320);
            } else {
                this.splitViewPlayer.setSources(sourcesArray);
            }

            if (this.player == null) {
                this.player = new ResizableJavaFXPlayer(this.camSatelliteCollection.next().getVideoSource(), 480, 320);
            } else {
                this.player.setVideoSource(this.camSatelliteCollection.next().getVideoSource());
            }

        }
    }

    @FXML 
    private void addVideoClip() {

    }
}
