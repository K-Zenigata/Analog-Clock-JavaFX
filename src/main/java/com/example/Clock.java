package com.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Clock {

    private AnchorPane clockPane;
    private Label clockTimeLabel;
    private Label[] clockDialLabel = new Label[12];
    private Pane hHand;
    private Pane mHand;
    private Pane sHand;

    private final int CLOCK_CENTER = 160;

    public Clock(AnchorPane clockBase, Pane hourHand, Pane minuteHand, Pane secondHand, Label timeLabel) {
        this.clockPane = clockBase;
        this.hHand = hourHand;
        this.mHand = minuteHand;
        this.sHand = secondHand;
        this.clockTimeLabel = timeLabel;

        SetScale();
        SetDial();

        // これを記述しないと、最初にLabelという文字が表示される
        updateTimeLabel(); // 最初に時刻を更新して表示
    }

    public void StartClock(){

        // 1秒ごとに時刻を更新するアニメーションを設定
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimeLabel()));
        timeline.setCycleCount(Animation.INDEFINITE); // 無限に繰り返す
        timeline.play();
    }

    // 文字盤の設定 目盛り
    private void SetScale() {
        // 中心から目盛りまでの距離
        double startR = CLOCK_CENTER * 0.91;
        double miniStartR = CLOCK_CENTER * 0.94;
        double endR = CLOCK_CENTER * 0.95;

        for (int i = 0; i < 60; i++) {

            double sr = i % 5 == 0 ? startR : miniStartR;

            double radian = i * Math.PI / 30;

            double sx = sr * Math.cos(radian) + CLOCK_CENTER;
            double sy = sr * Math.sin(radian) + CLOCK_CENTER;

            double ex = endR * Math.cos(radian) + CLOCK_CENTER;
            double ey = endR * Math.sin(radian) + CLOCK_CENTER;

            // Line
            Line line = new Line();
            line.setStartX(sx);
            line.setStartY(sy);
            line.setEndX(ex);
            line.setEndY(ey);
            line.setStrokeWidth(5);
            line.setStroke(Color.rgb(165, 165, 165));
            line.setOpacity(1);

            clockPane.getChildren().add(line);
        }
    }

    // 文字盤の設定 ダイアル
    private void SetDial() {

        // 中心からダイアルまでの距離
        double r = CLOCK_CENTER * 0.81;

        // 文字盤の数値のサイズ、縦横同じ
        final int DIAL_SIZE = 38;

        for (int i = 0; i < 12; i++) {

            int num = i == 0 ? 12 : i;

            // こいつを忘れずに・・・ぬるぽ
            clockDialLabel[i] = new Label();

            String iStr = Integer.valueOf(num).toString();
            clockDialLabel[i].setText(iStr);
            clockDialLabel[i].setPrefWidth(DIAL_SIZE);
            clockDialLabel[i].setPrefHeight(DIAL_SIZE);
            clockDialLabel[i].setFont(new Font("Arial", 28));
            clockDialLabel[i].setTextFill(Color.rgb(130, 130, 130));
            clockDialLabel[i].setAlignment(Pos.CENTER);

            // これ、-3をしないと12が3時の所に来ます
            double a = i - 3;
            double radian = a * Math.PI / 6;

            // ダイアルのサイズの半分ずらす。
            double x = r * Math.cos(radian) + CLOCK_CENTER - DIAL_SIZE / 2;
            double y = r * Math.sin(radian) + CLOCK_CENTER - DIAL_SIZE / 2;

            clockDialLabel[i].setLayoutX(x);
            clockDialLabel[i].setLayoutY(y);

            clockPane.getChildren().add((clockDialLabel[i]));
        }

    }

    private void SetHand(double sAngle, double mAngle, double hAngle) {

        // 回転トランスフォームを作成し、中心点を指定
        // 回転角度, 中心点のX座標, 中心点のY座標
        Rotate hRotate = new Rotate(hAngle, 9, 95);
        hHand.getTransforms().clear();
        hHand.getTransforms().add(hRotate);

        Rotate mRotate = new Rotate(mAngle, 5, 123);
        mHand.getTransforms().clear();
        mHand.getTransforms().add(mRotate);

        Rotate sRotate = new Rotate(sAngle, 3, 140);
        sHand.getTransforms().clear();
        sHand.getTransforms().add(sRotate);

    }

    private void updateTimeLabel() {

        LocalTime currentTime = LocalTime.now();

        // 時をint型の変数に取得
        int hour = currentTime.getHour();

        // 分をint型の変数に取得
        int minute = currentTime.getMinute();

        // 秒をint型の変数に取得
        int second = currentTime.getSecond();

        // 秒針の角度
        double sAngle = second * 6;

        // 長針の角度
        double plusSeconds = second == 0 ? 0 : (6.0 / 60) * second;
        double mAngle = minute * 6 + plusSeconds;

        // 短針の角度
        hour = hour >= 12 ? hour - 12 : hour;
        double plusMinute = minute == 0 ? 0 : (30.0 / 60) * minute;
        double hAngle = hour * 30 + plusMinute;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        clockTimeLabel.setText(formattedTime);

        SetHand(sAngle, mAngle, hAngle);

    }

}
