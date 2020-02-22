package com.guillot.engine.gui;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;

public class Dialog extends SubView {

    public final static String DEFAULT_DIALOG_BUTTON_TEXT = "Close";

    protected Image background;

    protected TextBox textBox;

    protected Button buttonClose;

    public Dialog(View parent, int width, String text, String buttonText) throws Exception {
        super(parent);

        background = new Image("gui/default_filter.png", 0, 0);
        background.coverZone(WIDTH, HEIGHT, false);
        background.setOpacity(0.5f);

        textBox = new TextBox(WIDTH / 2 - width / 2, y, width, text);
        textBox.setHeight(textBox.getHeight() + 60);
        textBox.setY(HEIGHT / 2 - textBox.getHeight() / 2);

        buttonClose = new Button(buttonText, WIDTH / 2 - 100 / 2, textBox.getY() + textBox.getHeight() - 50, 100, 30);
        buttonClose.setEvent(new Event() {

            public void perform() throws Exception {
                setVisible(false);
            }
        });

        add(background);
        add(textBox);
        add(buttonClose);
    }

    public Dialog(View parent, int width, String text) throws Exception {
        this(parent, width, text, DEFAULT_DIALOG_BUTTON_TEXT);
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

    public String getText() {
        return textBox.getText();
    }

    public void setText(String text) {
        textBox.setText(text);
        textBox.setHeight(textBox.getHeight() + 60);

        textBox.setY(HEIGHT / 2 - textBox.getHeight() / 2);
        buttonClose.setY(textBox.getY() + textBox.getHeight() - 50);
    }
}
