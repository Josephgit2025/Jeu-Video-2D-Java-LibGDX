package com.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class EventHandler {
    private Set<KeyCode> pressedKeys = new HashSet<>();
    
    public EventHandler(Scene scene){
        this.pollEvents(scene);
    }

    public void pollEvents(Scene scene){
        scene.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            pressedKeys.add(event.getCode());
            handleKeyPressed(event.getCode());
        });
        scene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
            handleKeyReleased(event.getCode());
        });
    }

    private void handleKeyPressed(KeyCode key){}

    private void handleKeyReleased(KeyCode key){}

    public boolean isPressed(KeyCode key){
        return pressedKeys.contains(key);
    }

    public Set<KeyCode> getPressedKeys(){
        return new HashSet<>(pressedKeys);
    }    public void setSpritePosX(int posX){
        
    }
}
