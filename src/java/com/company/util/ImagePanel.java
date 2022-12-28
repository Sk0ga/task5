package com.company.util;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }


}
