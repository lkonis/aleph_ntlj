package com.konis.aleph;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class PlayPanel extends JLayeredPane {
    private Image backgroundImage;

    /**
     * Some code to initialize the background image
     * Here, we use the constructor to load the image. This
     * can vary depending on the use case of the panel
     * @param fileName
     * @param height for rescaling
     * @param width for rescaling
     * @throws IOException
     */
    public PlayPanel(String fileName, int width, int height) throws IOException{
        super();
        backgroundImage = ImageIO.read(new File(fileName));
        backgroundImage = backgroundImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public void replaceBackgroundImage (String fileName) throws IOException{
        int width = getWidth();
        int height = getHeight();
        if (width==0)
            return;
        backgroundImage = ImageIO.read(new File(fileName));
        backgroundImage = backgroundImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
    /**
     * Draw the background image.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
    }

    @Override
    public void removeAll() {
        getComponentCount();
        for (Component c: getComponents())
        {
            remove(c);
        }
    }
}
