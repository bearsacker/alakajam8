package com.guillot.engine.gui;

import java.awt.Font;
import java.io.InputStream;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import com.guillot.engine.utils.FileLoader;


public class GUI {

    public final static String DEFAULT_FONT = "fonts/8bits.ttf";

    private static GUI instance = new GUI();

    private GameContainer container;

    private Input input;

    private int mouseX;

    private int mouseY;

    private boolean[] keysPressed;

    private boolean[] mouseButtonsDown;

    private boolean[] mouseButtonsReleased;

    private View currentView;

    private TrueTypeFont font;

    public static GUI get() {
        return instance;
    }

    private GUI() {
        try {
            Font awtFont = FileLoader.fontFromResource(DEFAULT_FONT);
            awtFont = awtFont.deriveFont(24f);
            font = new TrueTypeFont(awtFont, false);

            keysPressed = new boolean[256];
            mouseButtonsDown = new boolean[Mouse.getButtonCount()];
            mouseButtonsReleased = new boolean[Mouse.getButtonCount()];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TrueTypeFont getFont() {
        return this.font;
    }

    public TrueTypeFont loadFont(String font, float size) {
        return loadFont(font, true, size);
    }

    public TrueTypeFont loadFont(String font, boolean antiAlias, float size) {
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream(font);
            Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont2 = awtFont2.deriveFont(size);
            inputStream.close();

            return (new TrueTypeFont(awtFont2, antiAlias));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void switchView(View v) {
        try {
            if (currentView != null) {
                currentView.stop();
            }

            currentView = v;
            currentView.start();
        } catch (Exception e) {
            e.printStackTrace();
            switchView(new ViewException(e));
        }
    }

    public void paint(Graphics g) {
        g.scale(2f, 2f);

        try {
            if (currentView != null) {
                currentView.paintComponents(g);
            }
        } catch (Exception e) {
            switchView(new ViewException(e));
        }
    }

    public void update(GameContainer container) {
        if (this.container != container) {
            this.container = container;
        }

        if (input == null) {
            input = this.container.getInput();
            input.addControllerListener(Controller.get());
        }

        mouseX = input.getMouseX();
        mouseY = input.getMouseY();

        for (int i = 0; i < keysPressed.length; i++) {
            keysPressed[i] = input.isKeyPressed(i);
        }

        for (int i = 0; i < mouseButtonsReleased.length; i++) {
            mouseButtonsReleased[i] = false;

            if (input.isMouseButtonDown(i)) {
                mouseButtonsDown[i] = true;
            } else {
                if (mouseButtonsDown[i]) {
                    mouseButtonsReleased[i] = true;
                    mouseButtonsDown[i] = false;
                }
            }
        }

        try {
            if (currentView != null) {
                currentView.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            switchView(new ViewException(e));
        }

        Controller.get().update();
    }

    public void close() {
        try {
            this.currentView.stop();
        } catch (Exception e) {
            e.printStackTrace();
            switchView(new ViewException(e));
        }
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getLastKeyPressed() {
        for (int i = 0; i < keysPressed.length; i++) {
            if (keysPressed[i]) {
                return i;
            }
        }

        return -1;
    }

    public boolean isKeyPressed(int code) {
        return keysPressed[code];
    }

    public boolean isMouseButtonReleased(int button) {
        return mouseButtonsReleased[button];
    }

    public GameContainer getContainer() {
        return container;
    }

    public Input getInput() {
        return input;
    }

    public void associate(Button button) {
        for (int i = 0; i < input.getControllerCount(); i++) {
            /*
             * input. if (input.isControlPressed(i, button.getIdentifier()) && !input.getAssociated().contains(i)) { id = i;
             * input.getAssociated().add(id); logger.info("XBox360 controller associated with id " + id);
             * 
             * int j = 0; for (String axisName : input.getAxisNames(id)) { Axis axis = Axis.getFromName(axisName); if (axis != null) {
             * this.axis.add(axis); logger.info("\tAxis " + j + ": " + axis); } j++; } }
             */
        }
    }
}
