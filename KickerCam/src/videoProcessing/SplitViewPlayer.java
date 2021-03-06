/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoProcessing;

import javafx.scene.layout.Pane;

/**
 *
 * @author flikkes
 */
public class SplitViewPlayer {

    /**
     * maximum number of players that may be instanciated at the same time to
     * play video
     */
    private final int MAX_PLAYER_INSTANCES = 4;
    /**
     * list of video sources <br>
     * <em>maximum number</em> of sources to be used equals
     * {@link #MAX_PLAYER_INSTANCES}, <br>
     * if less, only {@link #sources sources.length} players will be
     * instanciated
     */
    private String[] sources;
    /**
     * specifies width and height of the split view as a whloe, <br>
     * {@link ResizableJavaFXPlayer#playerHolder player panes} sizes depend on {
     *
     * @see #MAX_PLAYER_INSTANCES number of players}
     */
    private double width, height;
    /**
     * {@link ResizableJavaFXPlayer Players} that play the specified
     * {@link #sources sources}<br>
     *
     * @see #MAX_PLAYER_INSTANCES MAX_PLAYER_INSTANCES
     */
    private ResizableJavaFXPlayer[] players;
    /**
     * <b>true:</b> all initialized {@link ResizableJavaFXPlayer Players} are
     * currently playing<br>
     * <b>false:</b> all initialized {@link ResizableJavaFXPlayer Players} are
     * currently not playing
     */
    private boolean playing = false;
    /**
     * contains the {@link ResizableJavaFXPlayer#playerHolder Pane} of each
     * {@link ResizableJavaFXPlayer Player} that has been initialized
     */
    private Pane[] panes;

    public SplitViewPlayer(String[] sources, double width, double height) {
        this.sources = sources;
        this.width = width;
        this.height = height;

        int arraySize;

        if (this.sources.length < this.MAX_PLAYER_INSTANCES) {
            arraySize = sources.length;
        } else {
            arraySize = this.MAX_PLAYER_INSTANCES;
        }
        this.players = new ResizableJavaFXPlayer[arraySize];
        this.panes = new Pane[arraySize];

        // initialize all Players from Array
        for (int i = 0; i < arraySize; i++) {
            // width and height relative to MAX_PLAYER_INSTANCES to fit in best
            ResizableJavaFXPlayer rp = new ResizableJavaFXPlayer(
                    sources[i],
                    this.width / (this.MAX_PLAYER_INSTANCES / 2),
                    this.height / (this.MAX_PLAYER_INSTANCES / 2)
            );

            Pane playerPane = rp.getPane();
            this.players[i] = rp;
            this.panes[i] = playerPane;

            // set location of each player pane, depending on position in array
            switch (i) {
                case 0:
                    playerPane.relocate(0, 0);
                    break;
                case 1:
                    playerPane.relocate(
                            0, this.height / (this.MAX_PLAYER_INSTANCES / 2)
                    );
                    break;
                case 2:
                    playerPane.relocate(
                            this.width / (this.MAX_PLAYER_INSTANCES / 2), 0
                    );
                    break;
                case 3:
                    playerPane.relocate(
                            this.width / (this.MAX_PLAYER_INSTANCES / 2),
                            this.height / (this.MAX_PLAYER_INSTANCES / 2)
                    );
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * see {
     *
     * @see #panes}
     * @return an array of {@link Pane Panes} where video content will be drawn
     * to
     */
    public Pane[] getPanes() {
        return this.panes;
    }

    /**
     * calls
     * {@link #players each players} {@link ResizableJavaFXPlayer#play() play}
     * method
     */
    public void playSplitView() {
        for (ResizableJavaFXPlayer rp : this.players) {
            rp.play();
            this.playing = true;
        }
    }

    /**
     * calls
     * {@link #players each players} {@link ResizableJavaFXPlayer#pause() pause}
     * method
     */
    public void pauseSplitView() {
        for (ResizableJavaFXPlayer rp : this.players) {
            rp.pause();
            this.playing = false;
        }
    }

    /**
     *
     * @return {@link #playing}
     */
    public boolean isPlaying() {
        return this.playing;
    }
    
    /**
     * Sets new {@link #sources}.<br>
     * Takes as much url-strings as {@link #MAX_PLAYER_INSTANCES} allows and
     * initializes a {@link ResizableJavaFXPlayer} for each of them.<br>
     * Due to the possibility of a decrease/an increase of players every player 
     * is relocated individually inside the the {@link #panes panes}.
     * @param sources a string array with sources to play from
     */
    public void setSources(String[] sources) {
        if (sources.length <= this.MAX_PLAYER_INSTANCES) {
            this.sources = sources;
            int arraySize = sources.length;
            this.players = new ResizableJavaFXPlayer[arraySize];
            this.panes = new Pane[arraySize];

            // initialize all Players from Array
            for (int i = 0; i < arraySize; i++) {
                // width and height relative to MAX_PLAYER_INSTANCES to fit in best
                ResizableJavaFXPlayer rp = new ResizableJavaFXPlayer(
                        sources[i],
                        this.width / (this.MAX_PLAYER_INSTANCES / 2),
                        this.height / (this.MAX_PLAYER_INSTANCES / 2)
                );

                Pane playerPane = rp.getPane();
                this.players[i] = rp;
                this.panes[i] = playerPane;

                // set location of each player pane, depending on position in array
                switch (i) {
                    case 0:
                        playerPane.relocate(0, 0);
                        break;
                    case 1:
                        playerPane.relocate(
                                0, this.height / (this.MAX_PLAYER_INSTANCES / 2)
                        );
                        break;
                    case 2:
                        playerPane.relocate(
                                this.width / (this.MAX_PLAYER_INSTANCES / 2), 0
                        );
                        break;
                    case 3:
                        playerPane.relocate(
                                this.width / (this.MAX_PLAYER_INSTANCES / 2),
                                this.height / (this.MAX_PLAYER_INSTANCES / 2)
                        );
                        break;
                    default:
                        break;
                }
            }
        } else {
            System.out.println("Array too big...");
        }
    }
}
