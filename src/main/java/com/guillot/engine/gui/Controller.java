package com.guillot.engine.gui;

import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.Input;

public class Controller implements ControllerListener {

    private static int BUFFER_SIZE = 32;

    private static Controller instance = new Controller();

    private int[] directions;

    private boolean buttonPressed;

    public static Controller get() {
        return instance;
    }

    private Controller() {
        directions = new int[4];
        for (int i = 0; i < directions.length; i++) {
            directions[i] = -1;
        }
    }

    public void update() {
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] > 0) {
                directions[i]--;
            } else if (directions[i] == 0) {
                directions[i] = BUFFER_SIZE;
            }
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
        directions[0] = BUFFER_SIZE;
    }

    @Override
    public void controllerLeftReleased(int controller) {
        directions[0] = -1;
    }

    @Override
    public void controllerRightPressed(int controller) {
        directions[1] = BUFFER_SIZE;
    }

    @Override
    public void controllerRightReleased(int controller) {
        directions[1] = -1;
    }

    @Override
    public void controllerUpPressed(int controller) {
        directions[2] = BUFFER_SIZE;
    }

    @Override
    public void controllerUpReleased(int controller) {
        directions[2] = -1;
    }

    @Override
    public void controllerDownPressed(int controller) {
        directions[3] = BUFFER_SIZE;
    }

    @Override
    public void controllerDownReleased(int controller) {
        directions[3] = -1;
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
        return directions[0] == BUFFER_SIZE;
    }

    public boolean isRightPressed() {
        return directions[1] == BUFFER_SIZE;
    }

    public boolean isUpPressed() {
        return directions[2] == BUFFER_SIZE;
    }

    public boolean isDownPressed() {
        return directions[3] == BUFFER_SIZE;
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }

}
