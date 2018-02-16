package com.sandbox50572;

public class Coordinator implements Runnable {

    //конструктор
    public Coordinator() {

    }

    public void run() {

        //TODO добавить TimeControl
        //создание объекта
        Runnable workJsoup = new WorkJsoup();
        //создание дочернего потока
        Thread threadNext = new Thread(workJsoup);
        //стартуем новыи поток
        threadNext.start();
    }
}
