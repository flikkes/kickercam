/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores a list of {@link CamSatellite Cam Satellites}
 */
public class CamSatelliteCollection {

    private List<CamSatellite> camSatellites = new ArrayList<>();
    private List<String> sources = new ArrayList<>();
    private int nextIndex = 0;

    public void addCamSatellite(CamSatellite sat) {
        if (!this.sources.contains(sat.getVideoSource())) {
            this.camSatellites.add(sat);
            this.sources.add(sat.getVideoSource());
        }
    }

    public CamSatellite next() {
        CamSatellite returnValue = this.camSatellites.get(this.nextIndex);

        if (this.nextIndex == (this.camSatellites.size() - 1)) {
            this.nextIndex = 0;
        } else {
            this.nextIndex++;
        }

        return returnValue;
    }

    public CamSatellite prev() {
        int prevIndex;
        CamSatellite returnValue;

        if (this.camSatellites.size() < 2) {
            return this.next();
        }

        if (this.nextIndex - 2 < 0) {
            prevIndex = this.camSatellites.size() - 2;
        } else {
            prevIndex = this.nextIndex - 2;
        }

        if (this.nextIndex == 0) {
            this.nextIndex = this.camSatellites.size() - 1;
        } else {
            this.nextIndex--;
        }

        returnValue = this.camSatellites.get(prevIndex);

        return returnValue;
    }

    public ObservableList<CamSatellite> getObservableList() {
        return FXCollections.observableArrayList(this.camSatellites);
    }
}
