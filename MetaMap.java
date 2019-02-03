import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.lang.Math.*;

public class MetaMap extends JPanel
{
    Map map;
    BufferedImage metaMapContent;
    
    public MetaMap(Map _map)
    {
        map = _map;
        
        setLayout(null);
        setBackground(new Color(127,127,127));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        
        metaMapContent = new BufferedImage(map.getTileDimensions(),map.getTileDimensions(),BufferedImage.TYPE_INT_ARGB);
    }
    
    public void addViewToggle()
    {
        addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e)
            {
                map.iterateView();
            }
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        for(int i = 0; i<map.getTileDimensions(); i++)
        {
            for(int j = 0; j<map.getTileDimensions(); j++)
            {
                try
                {
                metaMapContent.setRGB(i,j,map.getSquareLand(i,j).getBackgroundColor().getRGB());
                }
                catch(NullPointerException e)
                {
                    metaMapContent.setRGB(i,j,(new Color(127,127,127)).getRGB());
                }
            }
        }
        
        g.drawImage(metaMapContent, 5, 5, MetaMap.this.getWidth() - 10, (MetaMap.this.getWidth() - 10), null);
        g.setColor(new Color(255,0,0));
        g.drawRect(5 + ((MetaMap.this.getWidth() - 10)*map.getViewWindowX())/(20*map.getTileDimensions()), 5 + ((MetaMap.this.getWidth() - 10)*map.getViewWindowY())/(20*map.getTileDimensions()), ((MetaMap.this.getWidth() - 10)*map.getWidth())/(20*map.getTileDimensions()), ((MetaMap.this.getWidth() - 10)*map.getHeight())/(20*map.getTileDimensions()));
    }
}