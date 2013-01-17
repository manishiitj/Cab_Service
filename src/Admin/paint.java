/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;


/**
 *
 * @author BEDI
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
public class paint extends JComponent{
    static int counter=0;
    public void paint(Graphics g)
    {
        int ycord=90;
        super.paint(g);
        g.setColor(Color.black);
        g.drawLine(0,0,200,200);
    }
}
