package com.example.myapplication;

/**
 * Created by 전혜민 on 2017-11-21.
 */

public class DummySender implements BackgroundSender {
    //instance variables
    private Connection connection;

    //static variable
    private static LinkedQueue<String> msgList = new LinkedQueue<String>();
    private static boolean runningState;//True : Sender running
    //Constructor
    public DummySender(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void send(String s) {

        msgList.add(s);
        if(runningState) {
            return;
        }

        DummySender.runningState = true;
        new Sender().start();
    }

    private class Sender extends Thread{
        public Sender() {
            super();
        }
        @Override
        public void run() {
           // System.out.println("[DUMMY] address : "+ connection.toString());
           // while(!msgList.isEmpty())
             //   System.out.println("[DUMMY] message sended : "+msgList.pop());
        }
    }
}

