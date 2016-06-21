/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013, 2014, 2015 Caprica Software Limited.
 */
package videoProcessing;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import java.nio.ByteBuffer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Dynamically resizable player put in a more modular state to better fit into
 * existing javafx8 applications.
 * <p>
 * Originally contributed by Vladislav Kisel,
 * https://github.com/caprica/vlcj-javafx/pull/9, incorporated by caprica with
 * minor changes, https://github.com/caprica/vlcj-javafx, used for kickercam
 * application by Felix Lüpke.
 * </p>
 */
public class ResizableJavaFXPlayer {

    /**
     * video source
     */
    private String videoSource;
    /**
     * place where video is displayed
     */
    private ImageView imageView;
    /**
     * needed for rendering video by hand
     */
    private DirectMediaPlayerComponent mediaPlayerComponent;
    /**
     * specifies if video is playing or not
     */
    private boolean playing = false;
    /**
     * (*.mjpeg) video is split into images and written here
     */
    private WritableImage writableImage;
    /**
     * contains {@link #imageView imageView} <br>
     * can be added to common javaFX Panes
     */
    private Pane playerHolder;

    private WritablePixelFormat<ByteBuffer> pixelFormat;

    private FloatProperty videoSourceRatioProperty;

    public ResizableJavaFXPlayer(String videoSource, double width, double height) {
        this.videoSource = videoSource;
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files/VideoLAN/VLC");
        String libName = RuntimeUtil.getLibVlcLibraryName();
        Native.loadLibrary(libName, LibVlc.class);

        mediaPlayerComponent = new CanvasPlayerComponent();
        playerHolder = new Pane();
        videoSourceRatioProperty = new SimpleFloatProperty(0.4f);
        pixelFormat = PixelFormat.getByteBgraPreInstance();
        initializeImageView();
        this.playerHolder.resize(width, height);
    }

    public Pane getPane() {
        return this.playerHolder;
    }

    public void play() {
        this.mediaPlayerComponent.getMediaPlayer().prepareMedia(this.videoSource);
        this.mediaPlayerComponent.getMediaPlayer().start();
        this.playing = true;
    }

    public void pause() {
        this.mediaPlayerComponent.getMediaPlayer().pause();
        this.playing = false;
    }

    /**
     * !!!DANGEROUS!!! <br>
     * Only for testing purposes <br>
     * frees ressources used by player <br>
     * Player won't be able to play video after calling this method.
     */
    public void killPlayer() {
        this.mediaPlayerComponent.getMediaPlayer().release();
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void setDisplaySize(double width, double height) {
        this.playerHolder.resize(width, height);
    }
    
    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    private void initializeImageView() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        writableImage = new WritableImage((int) visualBounds.getWidth(), (int) visualBounds.getHeight());

        imageView = new ImageView(writableImage);
        playerHolder.getChildren().add(imageView);

        playerHolder.widthProperty().addListener((observable, oldValue, newValue) -> {
            fitImageViewSize(newValue.floatValue(), (float) playerHolder.getHeight());
        });

        playerHolder.heightProperty().addListener((observable, oldValue, newValue) -> {
            fitImageViewSize((float) playerHolder.getWidth(), newValue.floatValue());
        });

        videoSourceRatioProperty.addListener((observable, oldValue, newValue) -> {
            fitImageViewSize((float) playerHolder.getWidth(), (float) playerHolder.getHeight());
        });
    }

    private void fitImageViewSize(float width, float height) {
        Platform.runLater(() -> {
            float fitHeight = videoSourceRatioProperty.get() * width;
            if (fitHeight > height) {
                imageView.setFitHeight(height);
                double fitWidth = height / videoSourceRatioProperty.get();
                imageView.setFitWidth(fitWidth);
                imageView.setX((width - fitWidth) / 2);
                imageView.setY(0);
            } else {
                imageView.setFitWidth(width);
                imageView.setFitHeight(fitHeight);
                imageView.setY((height - fitHeight) / 2);
                imageView.setX(0);
            }
        });
    }

    private class CanvasPlayerComponent extends DirectMediaPlayerComponent {

        public CanvasPlayerComponent() {
            super(new CanvasBufferFormatCallback());
        }

        PixelWriter pixelWriter = null;

        private PixelWriter getPW() {
            if (pixelWriter == null) {
                pixelWriter = writableImage.getPixelWriter();
            }
            return pixelWriter;
        }

        @Override
        public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
            if (writableImage == null) {
                return;
            }
            Platform.runLater(() -> {
                Memory nativeBuffer = mediaPlayer.lock()[0];
                try {
                    ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                    getPW().setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
                } finally {
                    mediaPlayer.unlock();
                }
            });
        }
    }

    private class CanvasBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            Platform.runLater(() -> videoSourceRatioProperty.set((float) sourceHeight / (float) sourceWidth));
            return new RV32BufferFormat((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
        }
    }
}
