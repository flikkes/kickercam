/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author flikkes
 */
public class CamSatellite {

    private String name;
    private String videoSource;

    public CamSatellite(String name, String videoSource) {
        this.name = name;
        this.videoSource = videoSource;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getVideoSource() {
        return this.videoSource;
    }
}
