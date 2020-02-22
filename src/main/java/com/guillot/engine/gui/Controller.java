package com.guillot.engine.gui;

import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.Input;

public class Controller implements ControllerListener {

    private static int BUFFER_SIZE = 32;

    private static Controller instance = new Controller();

    private int leftPressed;

    private int rightPressed;

    private int upPressed;

    private int downPressed;

    private boolean buttonPressed;

    public static Controller get() {
        return instance;
    }

    private Controller() {
        leftPressed = -1;
        rightPressed = -1;
        upPressed = -1;
        downPressed = -1;
    }

    public void update() {
        if (leftPressed > 0) {
            leftPressed--;
        } else if (leftPressed == 0) {
            leftPressed = BUFFER_SIZE;
        }

        if (rightPressed > 0) {
            rightPressed--;
        } else if (rightPressed == 0) {
            rightPressed = BUFFER_SIZE;
        }

        if (upPressed > 0) {
            upPressed--;
        } else if (upPressed == 0) {
            upPressed = BUFFER_SIZE;
        }

        if (downPressed > 0) {
            downPressed--;
        } else if (downPressed == 0) {
            downPressed = BUFFER_SIZE;
        }

        buttonPressed = false;
    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }

    @Override
    public void controllerLeftPressed(int controller) {
        leftPressed = BUFFER_SIZE;
    }

    @Override
    public void controllerLeftReleased(int controller) {
        leftPressed = -1;
    }

    @Override
    public void controllerRightPressed(int controller) {
        rightPressed = BUFFER_SIZE;
    }

    @Override
    public void controllerRightReleased(int controller) {
        rightPressed = -1;
    }

    @Override
    public void controllerUpPressed(int controller) {
        upPressed = BUFFER_SIZE;
    }

    @Override
    public void controllerUpReleased(int controller) {
        upPressed = -1;
    }

    @Override
    public void controllerDownPressed(int controller) {
        downPressed = BUFFER_SIZE;
    }

    @Override
    public void controllerDownReleased(int controller) {
        downPressed = -1;
    }

    @Override
    public void controllerButtonPressed(int controller, int button) {
        buttonPressed = true;
    }

    @Override
    public void controllerButtonReleased(int controller, int button) {
        buttonPressed = false;
    }

    public boolean isLeftPressed() {
        return leftPressed == BUFFER_SIZE;
    }

    public boolean isRightPressed() {
        return rightPressed == BUFFER_SIZE;
    }

    public boolean isUpPressed() {
        return upPressed == BUFFER_SIZE;
    }

    public boolean isDownPressed() {
        return downPressed == BUFFER_SIZE;
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }

}
