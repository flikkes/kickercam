/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoProcessing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author flikkes
 */
public class VideoDownloader extends Thread {

    private long time;
    private URL source = null;
    private InputStream inputStream = null;
    private String currentTempFileName;

    public VideoDownloader(String source) {
        try {
            this.source = new URL(source);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VideoDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//TODO save video with correct framerate information

    @Override
    public void run() {
        this.time = System.currentTimeMillis();
        try {
            this.inputStream = this.source.openStream();
            this.currentTempFileName = "temp" + this.time + ".mjpeg";
            File file = new File(this.currentTempFileName);
            FileUtils.copyToFile(this.inputStream, file);
        } catch (IOException ex) {
            Logger.getLogger(VideoDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }

    }

    public InputStream getInputStream() {
        return this.inputStream;
    }
}
