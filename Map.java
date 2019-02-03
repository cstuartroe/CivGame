import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.lang.Math.*;

public class Map extends JPanel
{
    int size;
    private SquareLand[][] landArray;
    private Arrow upArrow;
    private Arrow leftArrow;
    private Arrow rightArrow;
    private Arrow downArrow;
    private int viewWindowX;
    private int viewWindowY;
    private Random generator;
    public ArrayList<SquareLand> refs;
    private CivGame civGame;
    private MetaMap metaMap;
    private int view;
    private SquareLand inFocus;
    private StatsPanel statsPanel;
    private String[] views = {
        "Satellite", "Topographic", "Humidity", "Temperature", "Forest Type", "Carb Type", "Brotein Type", "Herbivore Type", "Carnivore Type", "Livestock Type"
    };
    
    public static void main(String[] args)
    {
        new CivGame();
    }
    
    public Map(CivGame _civGame)
    {
        size=150;
        landArray = new SquareLand[size][size];
        setLayout(null);
        viewWindowX = 0;
        viewWindowY = 0;
        generator = new Random();
        setCivGame(_civGame);
        
        metaMap = new MetaMap(this);
        view = 0;
        
        upArrow = new Arrow(0, -25);
        
        leftArrow = new Arrow(-25, 0);
        
        rightArrow = new Arrow(25, 0);
        
        downArrow = new Arrow(0, 25);
        
        add(upArrow);
        add(leftArrow);
        add(rightArrow);
        add(downArrow);
        
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                landArray[i][j] = new SquareLand(i,j,19,this);
            }
        }
    }
    
    private class TileBuffer implements Runnable
    {
        private int row;
        
        public TileBuffer(int _row)
        {
            row = _row;
        }
        
        public void run()
        {
            for(int column = 0; column<size; column++)
            {
                add(landArray[row][column]);
            }
            Map.this.recalculateDimensions();
            revalidate();
            repaint();
        }
    }
    
    public void construct()
    {
        civGame.setViewLabelText("Buffering Tiles");
        
        for(int row = 0; row<size; row++)
        {
            for(int column = 0; column<size; column++)
            {
                add(landArray[row][column]);
            }
            Map.this.recalculateDimensions();
            revalidate();
            repaint();
            //(new Thread(new TileBuffer(row))).start();
            civGame.setViewLabelText("Buffering Tiles " + Integer.toString((row+1)*100/size) + "%");
        }
        
        civGame.setViewLabelText("Generating Topography");
        
        Map.this.recalculateDimensions();
        inFocus = landArray[0][0];
        
        int refCounter = 0;
        refs = new ArrayList<SquareLand>();
        for(int i = 0; i<20; i++)
        {
            SquareLand newRef = landArray[generator.nextInt(size/2) + generator.nextInt(size/2)][generator.nextInt(size/2) + generator.nextInt(size/2)];
            int refHeight = 20 + generator.nextInt(80) - generator.nextInt(20);
            newRef.setAltitude(refHeight);
            refs.add(newRef);
            revalidate();
            repaint();
            metaMap.repaint();
        }
        
        while(refCounter < refs.size())
        {
            for(int distance = 0; distance<10; distance++)
            {
                ArrayList<SquareLand> theRing = refs.get(refCounter).ring(distance);
                for(int j = 0; j<theRing.size(); j++)
                {
                    theRing.get(j).spreadAltitude(refs.get(refCounter));
                }
            }
            if(Math.abs(refs.get(refCounter).getAltitude() - 20)>1 && (refs.get(refCounter).distanceToRef())>1)
            {
                for(int i = 0; i<(Math.abs(refs.get(refCounter).getAltitude() - 20))/2; i++)
                {
                    int newX = refs.get(refCounter).getMapX() - 10 + generator.nextInt(21);
                    int newY = refs.get(refCounter).getMapY() - 10 + generator.nextInt(21);
                    if(newX>=0 && newX<size && newY>=0 && newY<size)
                    {
                        SquareLand newRef = landArray[newX][newY];
                        int currentAltitude = refs.get(refCounter).getAltitude();
                        int newAltitude = currentAltitude;
                        try
                        {
                            newAltitude = currentAltitude + generator.nextInt(20) - generator.nextInt(currentAltitude*1/8);
                            /*- (refs.get(refCounter).distanceTo(landArray[size/2][size/2]))*75/size))*/
                        }
                        catch(IllegalArgumentException e)
                        {
                            System.out.println(refs.get(refCounter).distanceTo(landArray[size/2][size/2]));
                        }
                        newRef.setAltitude(newAltitude);
                        refs.add(newRef);
                    }
                }
            }
            
            refCounter++;
            revalidate();
            repaint();
            metaMap.repaint();
        }
        
        /*for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                landArray[i][j].setAltitude(35);
            }
        }
        
        for(int distance = 0; distance<size; distance++)
        {
            ArrayList<SquareLand> theRing = landArray[size/2][size/2].ring(distance);
            for(int j = 0; j<theRing.size(); j++)
            {
                SquareLand newRef = theRing.get(j);
                int newAltitude = newRef.getAltitude() - distance*50/size;
                if(newAltitude<0){newAltitude=0;}
                theRing.get(j).setAltitude(newAltitude);
            }
            metaMap.repaint();
        }*/
        
        civGame.setViewLabelText("Creating Geographic Features");
        
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                landArray[i][j].setType("Peak");
                ArrayList<SquareLand> theRing = landArray[i][j].ring(1);
                for(int ringCounter = 0; ringCounter<theRing.size(); ringCounter++)
                {
                    if((theRing.get(ringCounter).getAltitude()) + 4 >= landArray[i][j].getAltitude())
                    {
                        landArray[i][j].setType("Plain");
                    }
                }
            }
            revalidate();
            repaint();
            metaMap.repaint();
        }
        
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                if(landArray[i][j].getAltitude()<20)
                {
                    int surroundingSeas = 0;
                    for(int distance = 0; distance<5; distance++)
                    {
                        ArrayList<SquareLand> theRing = landArray[i][j].ring(distance);
                        for(int ringCounter = 0; ringCounter<theRing.size(); ringCounter++)
                        {
                            if(theRing.get(ringCounter).getAltitude()<20)
                            {
                                surroundingSeas++;
                            }
                        }
                    }
                    if(surroundingSeas>15)
                    {
                        landArray[i][j].setType("Sea");
                    }
                }
            }
            revalidate();
            repaint();
            metaMap.repaint();
        }
        
        refCounter=0;
        
        while(refCounter<refs.size())
        {
            for(int distance = 0; distance<10; distance++)
            {
                ArrayList<SquareLand> theRing = refs.get(refCounter).ring(distance);
                for(int j = 0; j<theRing.size(); j++)
                {
                    theRing.get(j).spreadHumidity(refs.get(refCounter));
                }
            }
            
            refCounter++;
            revalidate();
            repaint();
            metaMap.repaint();
        }
        
        new Rivers(this);
        
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                landArray[i][j].calculateResources();
            }
        }
        
        civGame.setViewLabelText(views[view]);
        revalidate();
        repaint();
        metaMap.repaint();
        metaMap.addViewToggle();
    }
    
    public class Arrow extends JPanel implements Runnable
    {
        int x;
        int y;
        boolean active;
        
        public Arrow(int _x, int _y)
        {
            x = _x;
            y = _y;
            setOpaque(false);
            addMouseListener(new MouseListener(){
                public void mouseEntered(MouseEvent e)
                {
                    active = true;
                }
                public void mouseExited(MouseEvent e)
                {
                    active = false;
                }
                public void mouseReleased(MouseEvent e){}
                public void mousePressed(MouseEvent e){}
                public void mouseClicked(MouseEvent e){}
            });
            (new Thread(Arrow.this)).start();
        }
        
        public void run()
        {
            while(1==1)
            {
                if(active)
                {
                    Map.this.viewWindowX += Arrow.this.x;
                    Map.this.viewWindowY += Arrow.this.y;
                    if(Map.this.viewWindowX < 0){Map.this.viewWindowX = 0;}
                    if(Map.this.viewWindowX > 20*size - Map.this.getWidth()){Map.this.viewWindowX = 20*size - Map.this.getWidth();}
                    if(Map.this.viewWindowY < 0){Map.this.viewWindowY = 0;}
                    if(Map.this.viewWindowY > 20*size - Map.this.getHeight()){Map.this.viewWindowY = 20*size - Map.this.getHeight();}
                    Map.this.recalculateDimensions();
                    metaMap.repaint();
                }
                try
                {
                    Thread.sleep(50);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void recalculateDimensions()
    {
        upArrow.setBounds(0,0,this.getWidth(),20);
        leftArrow.setBounds(0,0,20,this.getHeight());
        rightArrow.setBounds(this.getWidth() - 20,0,20,this.getHeight());
        downArrow.setBounds(0,this.getHeight()-20,this.getWidth(),20);
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                landArray[i][j].setBounds(i*20 - viewWindowX,j*20 - viewWindowY,20,20);
            }
        }
        
    }
    
    public void recalculateColor()
    {
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                landArray[i][j].calculateColor();
            }
        }
        revalidate();
        repaint();
        metaMap.repaint();
    }
    
    public int getTileDimensions()
    {
        return size;
    }
    
    public SquareLand getSquareLand(int i, int j)
    {
        return landArray[i][j];
    }
    
    public void setFocus(SquareLand _inFocus)
    {
        inFocus.removeFocus();
        inFocus = _inFocus;
        statsPanel.updateFocus(inFocus);
    }
    
    public SquareLand getFocus()
    {
        return inFocus;
    }
    
    public int getViewWindowX()
    {
        return viewWindowX;
    }
    
    public int getViewWindowY()
    {
        return viewWindowY;
    }
    
    public void setCivGame(CivGame _civGame)
    {
        civGame = _civGame;
    }
    
    public void setMetaMap(MetaMap _metaMap)
    {
        metaMap = _metaMap;
    }
    
    public void setStatsPanel(StatsPanel _statsPanel)
    {
        statsPanel = _statsPanel;
    }
    
    public String getView()
    {
        return views[view];
    }
    
    public void setView(int _view)
    {
        view = _view%4;
        recalculateColor();
    }
    
    public void iterateView()
    {
        view += 1;
        view %= views.length;
        recalculateColor();
        civGame.setViewLabelText(views[view]);
    }
}