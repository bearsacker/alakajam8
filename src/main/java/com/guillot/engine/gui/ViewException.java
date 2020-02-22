package com.guillot.engine.gui;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;

import org.newdawn.slick.Color;


public class ViewException extends View {

    private Exception exception;

    public ViewException(Exception e) {
        this.exception = e;
    }

    @Override
    public void start() throws Exception {
        String text = "";
        StackTraceElement[] trace = exception.getStackTrace();
        for (StackTraceElement e : trace) {
            text += "\n" + e.toString();
        }

        add(new Text("Exception: " + exception.getMessage(), 30, 30, Color.red));
        add(new Text(text, 30, 50));

        Button b = new Button("Close", WIDTH - 220, HEIGHT - 50, 200, 30);

        b.setEvent(new Event() {

            public void perform() {
                GUI.get().getContainer().exit();
            }
        });

        add(b);
    }

    @Override
    public void stop() throws Exception {

    }
}
