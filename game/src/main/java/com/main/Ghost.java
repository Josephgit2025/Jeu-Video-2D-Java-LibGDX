package com.main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Ghost extends Entity{
    private int speed = 10;

    public Ghost(int posX, int posY){
        super("/com/main/assets/ghost.png", posX, posY);
        
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void update(EventHandler event){
        if (event.isPressed(KeyCode.W) || event.isPressed(KeyCode.UP)){
            moveUp();
        }
        if (event.isPressed(KeyCode.S) || event.isPressed(KeyCode.DOWN)){
            moveDown();
        }
        if (event.isPressed(KeyCode.D) || event.isPressed(KeyCode.RIGHT)){
            moveRight();
        }
        if (event.isPressed(KeyCode.A) || event.isPressed(KeyCode.LEFT)){
            moveLeft();
        }
    }
    
    private void moveUp(){
        if (this.getPosY() - speed < 0){
            this.setSpritePosY(0);
            System.out.println("PosY = " + this.getPosY());
        } else{
            this.setSpritePosY(this.getPosY() - speed);
            System.out.println("PosY = " + this.getPosY());
        }
    }

    private void moveDown(){
        if (this.getPosY() + speed > App.getHeight()){
            this.setSpritePosY((int)App.getHeight());
            System.out.println("PosY = " + this.getPosY());
        } else{
            this.setSpritePosY(this.getPosY() + speed);
            System.out.println("PosY = " + this.getPosY());
        }
    }

    private void moveLeft(){
        if (this.getPosX() - speed < 0){
            this.setSpritePosX(0);
            System.out.println("PosX = " + this.getPosX());
        } else{
            this.setSpritePosX(this.getPosX() - speed);
            System.out.println("PosX = " + this.getPosX());

        }
    }

    private void moveRight(){
        if (this.getPosX() + speed > App.getWidth()){
            this.setSpritePosX((int)App.getWidth());
            System.out.println("PosX = " + this.getPosX());

        } else{
            this.setSpritePosX(this.getPosX() + speed);
            System.out.println("PosX = " + this.getPosX());

        }
    }
}
