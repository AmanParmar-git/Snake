package com.company;

import javax.swing.*;
import java.awt.*;
import static com.company.Main.*;

public class Canvas extends JComponent {

    @Override
    public void paint(Graphics g) {
        for(int i = 0; i < Main.height * rectSize; i+=rectSize){
            for(int j = 0; j < Main.width * rectSize; j+=rectSize){
                g.setColor(Color.DARK_GRAY);
                if(i/rectSize == snakeHead.getX() && j/rectSize == snakeHead.getY())
                    g.setColor(Color.white);
                else if(snakeTail.contains(board[i/rectSize][j/rectSize]))
                    g.setColor(Color.green);
                else if(i/rectSize == food.getX() && j/rectSize == food.getY())
                    g.setColor(Color.red);
                else if(showPath && path.contains(board[i/rectSize][j/rectSize]))
                    g.setColor(Color.black);
//                else if(visited.contains(board[i/rectSize][j/rectSize]))
//                    g.setColor(Color.CYAN);

                    g.fillRect(i,j,rectSize,rectSize);
            }
        }

    }
}
