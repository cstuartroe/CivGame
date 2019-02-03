import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CivGame extends JFrame
{
    private JPanel infoPanel;
    private Map map;
    private StatsPanel statsPanel;
    private MetaMap metaMap;
    private JLabel viewLabel;
    
    public static void main(String[] args)
    {
        new CivGame();
    }
    
    public CivGame()
    {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setSize(700,500);
        setResizable(true);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 255, 255));
        addComponentListener(new ComponentAdapter()
                                      {
            public void componentResized(ComponentEvent e)
            {
                recalculateDimensions();
            }
        });
        
        infoPanel = new JPanel();        
        infoPanel.setBackground(new Color(127,127,127));
        infoPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        add(infoPanel);
        
        viewLabel = new JLabel();
        viewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(viewLabel);
        
        /*JButton satellite = new JButton("Satellite");
        satellite.setAlignmentX(Component.CENTER_ALIGNMENT);
        satellite.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                map.setView(0);
            }
        });
        infoPanel.add(satellite);
        
        JButton topographic = new JButton("Topographic");
        topographic.setAlignmentX(Component.CENTER_ALIGNMENT);
        topographic.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                map.setView(1);
            }
        });
        infoPanel.add(topographic);
        
        JButton humidity = new JButton("Humidity");
        humidity.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidity.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                map.setView(2);
            }
        });
        infoPanel.add(humidity);
        
        JButton temperature = new JButton("Temperature");
        temperature.setAlignmentX(Component.CENTER_ALIGNMENT);
        temperature.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                map.setView(3);
            }
        });
        infoPanel.add(temperature);*/
        
        map = new Map(this);
        add(map);
        
        statsPanel = new StatsPanel(map);
        add(statsPanel);
        map.setStatsPanel(statsPanel);
        
        metaMap = new MetaMap(map);
        add(metaMap);
        map.setMetaMap(metaMap);
        
        recalculateDimensions();
        
        setVisible(true);
        
        map.construct();
    }
    
    private void recalculateDimensions()
    {
        infoPanel.setBounds(0,getContentPane().getWidth()/5,getContentPane().getWidth()/5, getContentPane().getHeight() - getContentPane().getWidth()/5);
        map.setBounds(getContentPane().getWidth()/5,0,getContentPane().getWidth() - getContentPane().getWidth()/5, getContentPane().getHeight() - getContentPane().getHeight()/5);
        statsPanel.setBounds(getContentPane().getWidth()/5, getContentPane().getHeight() - getContentPane().getHeight()/5, getContentPane().getWidth() - getContentPane().getWidth()/5, getContentPane().getHeight()/5);
        metaMap.setBounds(0,0,getContentPane().getWidth()/5,getContentPane().getWidth()/5);
    }
    
    public void setViewLabelText(String _viewLabelText)
    {
        viewLabel.setText(_viewLabelText);
    }
}