package com.example;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Controller implements Initializable {
    @FXML
    private AnchorPane clockBase;

    @FXML
    private Pane hourHand;

    @FXML
    private Pane minuteHand;

    @FXML
    private Pane secondHand;

    @FXML
    private Label timeLabel;

    @FXML
    void shutdown(MouseEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Clock clock = new Clock(clockBase, hourHand, minuteHand, secondHand, timeLabel);

        clock.StartClock();
    }
}
