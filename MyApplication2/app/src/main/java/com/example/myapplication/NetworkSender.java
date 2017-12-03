package com.example.myapplication;

/**
 * Created by 전혜민 on 2017-11-21.
 */

public class NetworkSender extends Thread implements BackgroundSender {
    //instance variables
    private Connection connection;

    //static variable
    private static LinkedQueue<String> msgList = new LinkedQueue<String>();
    private static boolean runningState;//True : Sender running
    //Constructor
    public NetworkSender(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void send(String s) {

        msgList.add(s);
        if(runningState) {
            return;
        }

        NetworkSender.runningState = true;
        new Sender().start();
    }

    private class Sender extends Thread{
        public Sender() {
            super();
        }
        @Override
        public void run() {
            //System.out.println(connection.toString());
            while(!msgList.isEmpty())
                connection.writer().println(msgList.pop());
            runningState = false;
        }
    }
}
