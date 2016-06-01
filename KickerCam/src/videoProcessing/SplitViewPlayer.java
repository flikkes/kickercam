/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoProcessing;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author flikkes
 */
public class SplitViewPlayer {

    private final int MAX_PLAYER_INSTANCES = 4;
    private String[] sources;
    private double width, height;
    private ResizableJavaFXPlayerTest[] players;
    private boolean playing = false;
    private Pane[] panes;
//    private BorderPane pane;

    public SplitViewPlayer(String[] sources, double width, double height) {
        this.sources = sources;
        this.width = width;
        this.height = height;
        int arraySize = this.MAX_PLAYER_INSTANCES;
        if (this.sources.length < this.MAX_PLAYER_INSTANCES) {
            arraySize = sources.length;
        }
        this.players = new ResizableJavaFXPlayerTest[arraySize];
        this.panes = new Pane[arraySize];
        for (int i = 0; i < arraySize; i++) {
            ResizableJavaFXPlayerTest rp = new ResizableJavaFXPlayerTest(sources[i], this.width/2, this.height/2);
            this.players[i] = rp;
            Pane playerPane = rp.getPane();
            this.panes[i] = playerPane;
//            this.pane = new BorderPane();
//            this.pane.getChildren().add(playerPane);
            switch (i) {
                case 0:
                    playerPane.relocate(0, 0);
                    break;
                case 1:
                    playerPane.relocate(0, this.height / 2);
                    break;
                case 2:
                    playerPane.relocate(this.width / 2, 0);
                    break;
                case 3:
                    playerPane.relocate(this.width / 2, this.height / 2);
                    break;
                default:
                    break;
            }
            

        }

    }
    
//    public BorderPane getSplitView() {
//        return this.pane;
//    }
    
    public Pane[] getPanes() {
        return this.panes;
    }
    
    public void playSplitView() {
        for (ResizableJavaFXPlayerTest rp : this.players) {
            rp.play();
            this.playing = true;
        }
    }
    
    public void pauseSplitView() {
        for (ResizableJavaFXPlayerTest rp : this.players) {
            rp.pause();
            this.playing = false;
        }
    }
    
    public boolean isPlaying() {
        return this.playing;
    }
}
