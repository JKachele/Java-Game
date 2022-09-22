package com.jkachele.game;

import java.awt.*;
import java.util.LinkedList;

public class Handler {

    LinkedList<GameObject> objects = new LinkedList<>();

    public void tick() {
        for (GameObject obj : objects) {
            obj.tick();
        }
    }

    public void render(Graphics g) {
        for (GameObject obj : objects) {
            obj.render(g);
        }
    }

    public void addObject(GameObject obj) {
        this.objects.add(obj);
    }

    public void removeObject(GameObject obj) {
        this.objects.remove(obj);
    }
}
