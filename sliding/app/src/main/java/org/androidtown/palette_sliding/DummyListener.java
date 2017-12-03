package org.androidtown.palette_sliding;

import java.util.Random;

/**
 * Created by chm31 on 2017-11-26.
 */

public class DummyListener extends Thread implements BackgroundListener {
    CallbackEvent ev;
    boolean exec;
    LinkedQueue<String> msgList;

    //for Debug***
    Random r = new Random();
    //************
    public DummyListener(Connection c, CallbackEvent ev) {
        this.ev = ev;
        this.exec = true;
        this.msgList = new LinkedQueue<String>();
    }
    @Override
    public void run() {
        while(exec) {
            if(this.msgList.isEmpty()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //for Debug***
                int i = r.nextInt(4);
                if(i==1) {
                    System.out.println("message added : "+this.msgList.isEmpty());
                    msgList.add("[DUMMY]message"+r.nextInt(100));
                    msgList.DEBUG_printAll();
                }
                //************
            }
            else {
                ev.run(msgList.pop());
            }
        }
    }
    public void start() {
        super.start();
    }
    @Override
    public void callback(CallbackEvent ev) {
        this.ev = ev;
    }
}
