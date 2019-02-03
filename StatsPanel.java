import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.lang.Math.*;

public class StatsPanel extends JPanel
{
    private JPanel geography, floraAndFauna, occupations;
    private JLabel geographyTitle, coordinates, altitude, humidity, temperature;
    private JLabel floraAndFaunaTitle;
    private JLabel occupationsTitle;
    private Map map;
    private SquareLand inFocus;
    
    public StatsPanel(Map _map)
    {
        setLayout(new GridLayout(1,5));
        setBackground(new Color(127,127,127));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        map = _map;
        
        addComponentListener(new ComponentAdapter()
                                      {
            public void componentResized(ComponentEvent e)
            {
                recalculateDimensions();
            }
        });
        
        geography = new JPanel();
        geography.setLayout(new BoxLayout(geography, BoxLayout.Y_AXIS));
        geography.setOpaque(false);
        add(geography);
        
        geographyTitle = new JLabel("Geography");
        geographyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        geography.add(geographyTitle);
        
        coordinates = new JLabel("Coordinates:");
        coordinates.setAlignmentX(Component.CENTER_ALIGNMENT);
        geography.add(coordinates);
        
        altitude = new JLabel("Altitude:");
        altitude.setAlignmentX(Component.CENTER_ALIGNMENT);
        geography.add(altitude);
        
        humidity = new JLabel("Humidity:");
        humidity.setAlignmentX(Component.CENTER_ALIGNMENT);
        geography.add(humidity);
        
        temperature = new JLabel("Average Temperature:");
        temperature.setAlignmentX(Component.CENTER_ALIGNMENT);
        geography.add(temperature);
        
        floraAndFauna = new JPanel();
        floraAndFauna.setLayout(new BoxLayout(floraAndFauna, BoxLayout.Y_AXIS));
        floraAndFauna.setOpaque(false);
        add(floraAndFauna);
        
        floraAndFaunaTitle = new JLabel("Flora And Fauna");
        floraAndFaunaTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        floraAndFauna.add(floraAndFaunaTitle);
        
        occupations = new JPanel();
        occupations.setLayout(new BoxLayout(occupations, BoxLayout.Y_AXIS));
        occupations.setOpaque(false);
        add(occupations);
        
        occupationsTitle = new JLabel("Occupations");
        occupationsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        occupations.add(occupationsTitle);
        
        revalidate();
        repaint();
    }
    
    public void updateFocus(SquareLand _inFocus)
    {
        inFocus = _inFocus;
        
        String longitude = "W";
        if(inFocus.getMapX() > map.getTileDimensions()/2)
        {
            longitude = "E";
        }
        coordinates.setText("Coordinates: " + Integer.toString(60 - inFocus.getMapY()*45/map.getTileDimensions()) + "' N " + Math.abs((inFocus.getMapX() - map.getTileDimensions()/2)*60/map.getTileDimensions()) + "'" + longitude);
        
        if(inFocus.getType()=="Sea")
        {
            altitude.setText("Altitude: 0m");
        }
        else
        {
            altitude.setText("Altitude: " + Integer.toString(inFocus.getAltitude()*50 - 1000) + "m");
        }
        
        humidity.setText("Moisture: " + Integer.toString(inFocus.getHumidity()) + "%");
        
        if(inFocus.getTemperature()==0)
        {
            temperature.setText("Average Temperature: less than -5C");
        }
        else
        {
            temperature.setText("Average Temperature: " + Integer.toString(inFocus.getTemperature()*4/10 - 5) + "C");
        }
        
        floraAndFauna.removeAll();
        floraAndFauna.add(floraAndFaunaTitle);
        
        for(Species s : inFocus.getSpecies())
        {
            JLabel speciesLabel = new JLabel(s.isDomestic() ? s.getWildName() : s.getName());
            speciesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            floraAndFauna.add(speciesLabel);
        }
        
        revalidate();
        repaint();
    }
    
    public void recalculateDimensions()
    {
        revalidate();
        repaint();
    }
}