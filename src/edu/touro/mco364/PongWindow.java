package edu.touro.mco364;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class PongWindow extends JFrame {

    private final int BALL_WIDTH = 20,
            PADDLE_WIDTH = 10,
            PADDLE_HEIGHT = 60,
            PADDLE_SPEED = 8;

    private int ballSpeedHorizontal = 1,
            ballSpeedVertical = 1,
            leftScore = 0,
            rightScore = 0;

    private String scoreString = "Point! Current score: Left - %d, Right - %d";

    private Point ball = new Point(300, 400),
            leftPaddle = new Point(25, 370),
            rightPaddle = new Point(565, 370);

    private PongInnerPanel pongInnerPanel = new PongInnerPanel();

    public PongWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFocusable(true);
        setupBallTimer();
        addListeners();
        setContentPane(pongInnerPanel);
        pack();
        setVisible(true);
    }

    private void addListeners() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyChar() == 'w') {
                    rightPaddle.translate(0, -PADDLE_SPEED);
                    repaint();
                }
                else if(keyEvent.getKeyChar() == 's') {
                    rightPaddle.translate(0, PADDLE_SPEED);
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        addMouseWheelListener(mouseWheelEvent -> {
            leftPaddle.translate(0, PADDLE_SPEED * mouseWheelEvent.getWheelRotation());
            repaint();
        });
    }

    private void setupBallTimer() {
        Timer ballTimer = new Timer(7, e -> {
            ball.translate(ballSpeedHorizontal, ballSpeedVertical);

            if(ballCollisionHorizontal()) {
                ballSpeedHorizontal = -ballSpeedHorizontal;
            }
            if(ballCollisionVertical()) {
                ballSpeedVertical = -ballSpeedVertical;
            }

            int edge = ballReachedEdge();

            if(edge > 0) {
                givePoint(edge);
                reset();
            }

            repaint();
        });

        ballTimer.start();
    }

    private void reset() {
        ball.x = 300;
        ball.y = 400;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void givePoint(int side) {
        switch (side) {
            case 1 :
                rightScore++;
                System.out.println(String.format(scoreString, leftScore, rightScore));
                break;
            case 2 :
                leftScore++;
                System.out.println(String.format(scoreString, leftScore, rightScore));
                break;
        }
    }

    private int ballReachedEdge() {
        if(ball.x < leftPaddle.x)
            return 1;
        if(ball.x + BALL_WIDTH > rightPaddle.x + PADDLE_WIDTH)
            return 2;
        return 0;
    }

    private boolean ballCollisionHorizontal() {
        if(ball.x <= leftPaddle.x + PADDLE_WIDTH && (leftPaddle.y <= ball.y && ball.y + BALL_WIDTH <= leftPaddle.y + PADDLE_HEIGHT))
            return true;    //Hit left paddle

        if(ball.x + BALL_WIDTH >= rightPaddle.x && (rightPaddle.y <= ball.y && ball.y + BALL_WIDTH <= rightPaddle.y + PADDLE_HEIGHT))
            return true;    //Hit right paddle

        if(ball.x <= 0)
            return true;

        if(ball.x + BALL_WIDTH >= 600)
            return true;
        return false;
    }

    private boolean ballCollisionVertical() {
        return ball.y <= 0 || ball.y + BALL_WIDTH >= 800;
    }

    class PongInnerPanel extends JPanel{

        PongInnerPanel() {
            setPreferredSize(new Dimension(600, 800));
            setBackground(new Color(160, 30, 240)); //PURPLE!!
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            g.setColor(new Color(100, 40, 130)); //MORE PURPLE!!!
            g.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_WIDTH);
            g.fillRect(leftPaddle.x, leftPaddle.y, PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillRect(rightPaddle.x, rightPaddle.y, PADDLE_WIDTH, PADDLE_HEIGHT);

        }
    }
}
