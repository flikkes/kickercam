/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author flikkes
 */
public class CamSatellite {

    private StringProperty name;
    private StringProperty videoSource;

    public CamSatellite(String name, String videoSource) {
        this.nameProperty().set(name);
        this.videoSourceProperty().set(videoSource);
    }
    
    public String getName() {
        return this.nameProperty().get();
    }
    
    public String getVideoSource() {
        return this.videoSourceProperty().get();
    }
    
    public StringProperty nameProperty() { 
         if (this.name == null) this.name = new SimpleStringProperty(this, "name");
         return this.name; 
     }
    
    public StringProperty videoSourceProperty() { 
         if (this.videoSource == null) this.videoSource = new SimpleStringProperty(this, "videoSource");
         return this.videoSource; 
     }
}
