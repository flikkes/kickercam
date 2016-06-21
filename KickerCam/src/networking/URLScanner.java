/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author flikkes
 */
public class URLScanner {

    public static void main(String[] args) {
        URLScanner uRLScanner = new URLScanner();
        List<String> urls = null;
        try {
            urls = uRLScanner.scanRange("http://10.10.10.", 0, 20, 8081);
        } catch (OutOfIpRangeException ex) {
            Logger.getLogger(URLScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String url : urls) {
            System.out.println(url);
        }
    }

    public boolean scanURL(String url) {
        HttpURLConnection urlCon;
        try {
            urlCon = (HttpURLConnection) new URL(url).openConnection();
            urlCon.setConnectTimeout(2000);
            urlCon.connect();
            System.out.println("SUCCESS: " + url + " exists");
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: " + url + " timeout reached");
            return false;
        }
    }

    /**
     * Checks a range of URLs for reachability. Only works with URLs that
     * consist of an ip.
     *
     * @param fixedURLPart
     * @param from
     * @param to
     * @param port optional, use -1 if no port is needed
     * @return a list of strings with all URLs that could be reached
     * @throws networking.URLScanner.OutOfIpRangeException
     */
    public List<String> scanRange(String fixedURLPart, int from, int to, int port) throws OutOfIpRangeException {
        List<String> activeURLs = new ArrayList<>();

        if (from > to || from < 0 || to > 254) {
            throw new OutOfIpRangeException();
        }
        for (int i = from; i < to+1; i++) {
            String urlToCheck;
            if (port == -1) {
                urlToCheck = fixedURLPart + i;
            } else {
                urlToCheck = fixedURLPart + i + ":" + port;
                System.out.println(urlToCheck);
            }
            if (this.scanURL(urlToCheck)) {
                activeURLs.add(urlToCheck);
            }
        }

        return activeURLs;
    }

    private static class OutOfIpRangeException extends Exception {

        public OutOfIpRangeException() {
            super("IP range is smaller that 0 or higher than 254");
        }
    }
}
