package com.jkachele.game;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable {

    public static final int WIDTH = 640;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final String TITLE = "Java Game";

    private Thread thread;
    private boolean isRunning = false;

    private final Handler handler;

    public Main() {
        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));
        new Window(WIDTH, HEIGHT, TITLE, this);
    }

    public synchronized void start() {
        //starts a new thread to run the game
        thread = new Thread(this);
        thread.start();
        isRunning = true;   
    }

    public synchronized void stop() {
        //stops the current thread
        try {
            thread.join();
            isRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //main game loop
        this.requestFocus();
        long lastTime = System.nanoTime();          //time since last iteration of loop. used to compute delta
        double amountOfTicks = 60.0;                //max FPS of game
        double ns = 1000000000 / amountOfTicks;     //number of nanoseconds per frame
        double delta = 0;                           //The 'progress' that must be elapsed until the next frame.
        long timer = System.currentTimeMillis();
        int frames = 0;                             //The number of frames elapsed. used for FPS calculations.
        while (isRunning) {
            long time = System.nanoTime();          //The current time. Used to know when to display next the FPS.
            delta += (time - lastTime) / ns;
            lastTime = time;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (isRunning)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        //updates the game state
        handler.tick();
    }

    private void render() {
        //renders the game
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Main();
    }
}
