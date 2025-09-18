package org.wineeenottt;

public class Main {

    public static void main(String[] args) {
        System.setProperty("FCGI_PORT", "25016");
        var fcgiServer = new FCGIServer();
        fcgiServer.start();
    }
}