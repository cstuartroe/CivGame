import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.lang.Math.*;

public class SquareLand extends JPanel
{
    private int x;
    private int y;
    private int altitude;
    private Map map;
    private int humidity;
    private int temperature;
    private Color backgroundColor;
    private String type;
    private BufferedImage background;
    private int topRiver;
    private int bottomRiver;
    private int leftRiver;
    private int rightRiver;
    private ArrayList<Species> species;
    Species tree;
    Species plant;
    Species carb;
    Species brotein;
    Species herbivore;
    Species carnivore;
    Species livestock;
    Species fish;
    
    public SquareLand(int _x, int _y, int _altitude, Map _map)
    {
        x = _x;
        y = _y;
        altitude = _altitude;
        map = _map;
        type = "Plain";
        backgroundColor = new Color(255,255,255);
        //background = new BufferedImage(20,20,BufferedImage.TYPE_INT_ARGB);
        topRiver = 0;
        bottomRiver = 0;
        leftRiver = 0;
        rightRiver = 0;
        species = new ArrayList<Species>();
        calculateStats();
        
        addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e)
            {
                addFocus();
            }
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });
    }
    
    private void calculateStats()
    {
        if(altitude<0){altitude=0;}
        if(altitude>99){altitude=99;}
        temperature = y*80/map.getTileDimensions() + 30 - altitude;
        if(temperature<0){temperature=0;}
        int humidityFromLatitude;
        if(y*100/map.getTileDimensions() >= 75)
        {
            humidityFromLatitude = (y*100/map.getTileDimensions() - 75)*2;
        }
        else if(y*100/map.getTileDimensions() >= 50)
        {
            humidityFromLatitude = (75 - y*100/map.getTileDimensions())*2;
        }
        else
        {
            humidityFromLatitude = 50;
        }
        humidity = 30 + humidityFromLatitude - altitude;
        if(humidity<0){humidity=0;}
        if(type=="Sea"){humidity=100;}
        calculateColor();
    }
    
    public void calculateResources()
    {
        tree = calculateResources(Species.trees, 170, false);
        carb = calculateResources(Species.carbs, 175, false);
        brotein = calculateResources(Species.broteins, 175, false);
        herbivore = calculateResources(Species.herbivores, 175, false);
        carnivore = calculateResources(Species.carnivores, 180, false);
        livestock = calculateResources(Species.livestocks, 175, false);
    }
    
    
    public Species calculateResources(Species[] list, int suitabilityThreshold, boolean aquatic)
    {
        if((type=="Sea")==aquatic)
        {
            Species target = null;
            
            for(Species s : list)
            {
                if(s.suitability(temperature,humidity,altitude) >= suitabilityThreshold)
                {
                    target = s;
                    suitabilityThreshold = target.suitability(temperature,humidity,altitude);
                }
            }
            
            if(target!=null)
            {
                species.add(target);
            }
            
            return target;
        }
        
        else{return null;}
    }
    
    public void calculateColor()
    {
        int red, grn, blu;
        if(map.getView() == "Satellite")
        {
            if(type=="Peak")
            {
                backgroundColor = new Color(85,85,85);
            }
            else if(type=="Sea")
            {
                backgroundColor = new Color(0,0,85);
            }
            else
            {
                red = 255 - temperature*85/100 - humidity*170/100;
                grn = 255 - temperature*85/100 - humidity*85/100;
                blu = 255 - temperature*255/100 - humidity*150/100;
                if(blu<0){blu=0;}
                if(red>255){red=255;}
                if(red<0){red=0;}
                try
                {
                    backgroundColor = new Color(red, grn, blu);
                }
                catch(IllegalArgumentException e)
                {
                    System.out.println("Error: red was " + Integer.toString(red));
                    System.out.println("Error: temp was " + Integer.toString(temperature));
                    System.out.println("Error: hum was " + Integer.toString(humidity));
                }
            }
        }
        
        else if(map.getView() == "Topographic")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            else
            {
                //red = altitude*5/2;
                //grn = (int)Math.sqrt((double)altitude)*15;
                //blu = 120;
                grn = 250 - Math.abs(altitude-50)*5;
                blu = 250 - altitude*5/2;
                red = altitude*5/2;
                backgroundColor = new Color(red, grn, blu);
            }
        }
        
        else if(map.getView() == "Humidity")
        {
            
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            else
            {
                grn = 250 - Math.abs(humidity-50)*5;
                red = 250 - humidity*5/2;
                blu = humidity*5/2;
                backgroundColor = new Color(red, grn, blu);
            }
        }
        else if(map.getView() == "Temperature")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            else
            {
                grn = 250 - Math.abs(temperature-50)*5;
                blu = 250 - temperature*5/2;
                red = temperature*5/2;
                backgroundColor = new Color(red, grn, blu);
            }
        }
        else if(map.getView() == "Forest Type")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            
            else if(getTree()==Species.spruce)
            {
                backgroundColor = new Color(0,170,136);
            }
            
            else if(getTree()==Species.pine)
            {
                backgroundColor = new Color(0,187,68);
            }
            
            else if(getTree()==Species.poplar)
            {
                backgroundColor = new Color(255,238,0);
            }
            
            else if(getTree()==Species.birch)
            {
                backgroundColor = new Color(204,204,153);
            }
            
            else if(getTree()==Species.elm)
            {
                backgroundColor = new Color(255,85,153);
            }
            
            else if(getTree()==Species.oak)
            {
                backgroundColor = new Color(136,0,0);
            }
            
            else if(getTree()==Species.bamboo)
            {
                backgroundColor = new Color(204,136,0);
            }
            
            else if(getTree()==Species.acacia)
            {
                backgroundColor = new Color(255,255,170);
            }
            
            else if(getTree()==Species.teak)
            {
                backgroundColor = new Color(204,102,170);
            }
            
            else if(getTree()==Species.ebony)
            {
                backgroundColor = new Color(0,0,68);
            }
            else
            {
                backgroundColor = new Color(136,136,136);
            }
        }
        
        else if(map.getView() == "Carb Type")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            
            else if(getCarb()==Species.taro)
            {
                backgroundColor = new Color(204,102,170);
            }
            else if(getCarb()==Species.breadfruit)
            {
                backgroundColor = new Color(153,255,85);
            }
            else if(getCarb()==Species.sorghum)
            {
                backgroundColor = new Color(136,0,0);
            }
            else if(getCarb()==Species.barley)
            {
                backgroundColor = new Color(255,255,170);
            }
            else if(getCarb()==Species.wheat)
            {
                backgroundColor = new Color(255,136,0);
            }
            else if(getCarb()==Species.rice)
            {
                backgroundColor = new Color(255,255,255);
            }
            else if(getCarb()==Species.millet)
            {
                backgroundColor = new Color(255,238,0);
            }
            else
            {
                backgroundColor = new Color(136,136,136);
            }
        }
        
        else if(map.getView() == "Brotein Type")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            
            else if(getBrotein()==Species.cowpea)
            {
                backgroundColor = new Color(204,102,170);
            }
            else if(getBrotein()==Species.soybean)
            {
                backgroundColor = new Color(153,255,85);
            }
            else if(getBrotein()==Species.favabean)
            {
                backgroundColor = new Color(255,255,170);
            }
            else if(getBrotein()==Species.lentil)
            {
                backgroundColor = new Color(255,136,0);
            }
            else if(getBrotein()==Species.chickpea)
            {
                backgroundColor = new Color(255,238,0);
            }
            else
            {
                backgroundColor = new Color(136,136,136);
            }
        }
        
        else if(map.getView() == "Herbivore Type")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            
            else if(getHerbivore()==Species.deer)
            {
                backgroundColor = new Color(204,136,0);
            }
            else if(getHerbivore()==Species.rabbit)
            {
                backgroundColor = new Color(153,255,85);
            }
            else if(getHerbivore()==Species.elephant)
            {
                backgroundColor = new Color(0,0,68);
            }
            else if(getHerbivore()==Species.gazelle)
            {
                backgroundColor = new Color(255,255,170);
            }
            else if(getHerbivore()==Species.goose)
            {
                backgroundColor = new Color(255,255,255);
            }
            else if(getHerbivore()==Species.monkey)
            {
                backgroundColor = new Color(255,136,0);
            }
            else if(getHerbivore()==Species.boar)
            {
                backgroundColor = new Color(255,238,0);
            }
            else if(getHerbivore()==Species.moose)
            {
                backgroundColor = new Color(0,170,136);
            }
            else if(getHerbivore()==Species.otter)
            {
                backgroundColor = new Color(204,204,153);
            }
            else if(getHerbivore()==Species.hyrax)
            {
                backgroundColor = new Color(0,187,68);
            }
            else
            {
                backgroundColor = new Color(136,136,136);
            }
        }
        
        else if(map.getView() == "Carnivore Type")
        {
            
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            
            else if(getCarnivore()==Species.lion)
            {
                backgroundColor = new Color(255,238,0);
            }
            else if(getCarnivore()==Species.wolf)
            {
                backgroundColor = new Color(0,0,68);
            }
            else if(getCarnivore()==Species.brownbear)
            {
                backgroundColor = new Color(204,136,0);
            }
            else if(getCarnivore()==Species.polarbear)
            {
                backgroundColor = new Color(255,255,255);
            }
            else if(getCarnivore()==Species.leopard)
            {
                backgroundColor = new Color(255,136,0);
            }
            else if(getCarnivore()==Species.honeybadger)
            {
                backgroundColor = new Color(255,255,170);
            }
            else if(getCarnivore()==Species.coyote)
            {
                backgroundColor = new Color(204,204,153);
            }
            else
            {
                backgroundColor = new Color(136,136,136);
            }
        }
        
        else if(map.getView() == "Livestock Type")
        {
            if(type=="Sea")
            {
                backgroundColor = new Color(0,0,0);
            }
            
            else if(getLivestock()==Species.camel)
            {
                backgroundColor = new Color(255,255,170);
            }
            else if(getLivestock()==Species.reindeer)
            {
                backgroundColor = new Color(204,136,0);
            }
            else if(getLivestock()==Species.horse)
            {
                backgroundColor = new Color(153,255,85);
            }
            else if(getLivestock()==Species.goat)
            {
                backgroundColor = new Color(204,204,153);
            }
            else if(getLivestock()==Species.buffalo)
            {
                backgroundColor = new Color(0,0,68);
            }
            else
            {
                backgroundColor = new Color(136,136,136);
            }
            
        }
        setBackground(backgroundColor);
    }
    
    public int getMapX()
    {
        return x;
    }
    
    public int getMapY()
    {
        return y;
    }
    
    public int getAltitude()
    {
        return altitude;
    }
    
    public void setAltitude(int _altitude)
    {
        altitude = _altitude;
        calculateStats();
    }
    
    public void spreadAltitude(SquareLand other)
    {
        float coefficient = (float)(Math.pow(.4,distanceTo(other)));
        altitude = (int)(((float)altitude)*(1.0-coefficient) + ((float)other.getAltitude())*coefficient);
        calculateStats();
    }
    
    public int getHumidity()
    {
        return humidity;
    }
    
    public void spreadHumidity(SquareLand other)
    {
        
        float coefficient = (float)(Math.pow(.7 - (float)altitude/150.0,distanceTo(other)));
        humidity = (int)Math.round(((float)humidity)*(1.0-coefficient) + ((float)other.getHumidity())*coefficient);
        if(humidity>99){humidity=99;}
        if(type=="Sea"){humidity=100;}
        calculateColor();
        
    }
    
    public int getTemperature()
    {
        return temperature;
    }
    
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }
    
    public Color colorMix(Color center, Color horizontal, Color vertical, Color corner, int xFromCenter, int yFromCenter)
    {
        xFromCenter = xFromCenter*3/4;
        yFromCenter = yFromCenter*3/4;
        int rx = (center.getRed()*(20-xFromCenter)*(20-yFromCenter) + horizontal.getRed()*(xFromCenter)*(20-yFromCenter) + vertical.getRed()*(20-xFromCenter)*(yFromCenter) + corner.getRed()*(xFromCenter)*(yFromCenter))/400;
        int gx = (center.getGreen()*(20-xFromCenter)*(20-yFromCenter) + horizontal.getGreen()*(xFromCenter)*(20-yFromCenter) + vertical.getGreen()*(20-xFromCenter)*(yFromCenter) + corner.getGreen()*(xFromCenter)*(yFromCenter))/400;
        int bx = (center.getBlue()*(20-xFromCenter)*(20-yFromCenter) + horizontal.getBlue()*(xFromCenter)*(20-yFromCenter) + vertical.getBlue()*(20-xFromCenter)*(yFromCenter) + corner.getBlue()*(xFromCenter)*(yFromCenter))/400;
        return new Color(rx,gx,bx);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        /*int left = 1;
         int right = 1;
         int up = 1;
         int down = 1;
         
         if(y==0)
         {
         up = 0;
         }
         if(x==0)
         {
         left = 0;
         }
         if(y==map.getTileDimensions()-1)
         {
         down = 0;
         }
         if(x==map.getTileDimensions()-1)
         {
         right = 0;
         }
         
         Color ul = map.getSquareLand(x-left,y-up).getBackgroundColor();
         Color uc = map.getSquareLand(x,y-up).getBackgroundColor();
         Color ur = map.getSquareLand(x+right,y-up).getBackgroundColor();
         Color cr = map.getSquareLand(x+right,y).getBackgroundColor();
         Color lr = map.getSquareLand(x+right,y+down).getBackgroundColor();
         Color lc = map.getSquareLand(x,y+down).getBackgroundColor();
         Color ll = map.getSquareLand(x-left,y+down).getBackgroundColor();
         Color cl = map.getSquareLand(x-left,y).getBackgroundColor();
         Color cc = backgroundColor;
         
         for(int i = 0; i<5; i++)
         {
         for(int j = 0; j<5; j++)
         {
         try
         {
         background.setRGB(i,j,colorMix(cc,cl,uc,ul,9-i,9-j).getRGB());
         }
         catch(NullPointerException e)
         {
         background.setRGB(i,j,(new Color(127,127,127)).getRGB());
         }
         }
         for(int j = 15; j<20; j++)
         {
         try
         {
         background.setRGB(i,j,colorMix(cc,cl,lc,ll,9-i,j-10).getRGB());
         }
         catch(NullPointerException e)
         {
         background.setRGB(i,j,(new Color(127,127,127)).getRGB());
         }
         }
         }
         
         for(int i = 15; i<20; i++)
         {
         for(int j = 0; j<5; j++)
         {
         try
         {
         background.setRGB(i,j,colorMix(cc,cr,uc,ur,i-10,9-j).getRGB());
         }
         catch(NullPointerException e)
         {
         background.setRGB(i,j,(new Color(127,127,127)).getRGB());
         }
         }
         for(int j = 15; j<20; j++)
         {
         try
         {
         background.setRGB(i,j,colorMix(cc,cr,lc,lr,i-10,j-10).getRGB());
         }
         catch(NullPointerException e)
         {
         background.setRGB(i,j,(new Color(127,127,127)).getRGB());
         }
         }
         }
         
         g.drawImage(background, 0, 0, 20, 20, null);*/
        
        g.setColor(new Color(0,0,85));
        g.fillRect(0,0,20,topRiver);
        g.fillRect(0,20-bottomRiver,20,bottomRiver);
        g.fillRect(0,0,leftRiver,20);
        g.fillRect(20-rightRiver,0,rightRiver,20);
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String s)
    {
        type = s;
        calculateStats();
    }
    
    public ArrayList<SquareLand> ring(int distance)
    {
        ArrayList<SquareLand> theRing = new ArrayList<SquareLand>();
        
        for(int i = x - distance; i <= x + distance; i++)
        {
            for(int j = y - distance; j <= y + distance; j++)
            {
                if(i>=0 && i<map.getTileDimensions() && j>=0 && j<map.getTileDimensions() && (SquareLand.this.distanceTo(map.getSquareLand(i,j)) == distance))
                {
                    theRing.add(map.getSquareLand(i,j));
                }
            }
        }
        
        return theRing;
    }
    
    public int distanceTo(SquareLand other)
    {
        return (int)(Math.sqrt(Math.pow((double)(x - other.getMapX()),2) + Math.pow((double)(y - other.getMapY()), 2)));
    }
    
    public int distanceToRef()
    {
        for(int i = 1; i<200; i++)
        {
            for(int j = 0; j<SquareLand.this.ring(i).size(); j++)
            {
                if(map.refs.contains(SquareLand.this.ring(i).get(j)))
                {
                    return i;
                }
            }
        }
        return 200;
    }
    
    public void addFocus()
    {
        this.setBorder(BorderFactory.createLineBorder(new Color(255,0,0)));
        map.setFocus(this);
    }
    
    public void removeFocus()
    {
        this.setBorder(null);
    }
    
    public void setTopRiver(int strength)
    {
        humidity+=strength;
        if(humidity>100){humidity=100;}
        calculateColor();
        topRiver = (strength>3)?1:0;
    }
    
    public void setBottomRiver(int strength)
    {
        humidity+=strength;
        if(humidity>100){humidity=100;}
        calculateColor();
        bottomRiver = (strength>3)?1:0;
    }
    
    public void setLeftRiver(int strength)
    {
        humidity+=strength;
        if(humidity>100){humidity=100;}
        calculateColor();
        leftRiver = (strength>3)?1:0;
    }
    
    public void setRightRiver(int strength)
    {
        humidity+=strength;
        if(humidity>100){humidity=100;}
        calculateColor();
        rightRiver = (strength>3)?1:0;
    }
    
    public Species getTree()
    {
        return tree;
    }
    
    public Species getCarb()
    {
        return carb;
    }
    
    public Species getBrotein()
    {
        return brotein;
    }
    
    public Species getHerbivore()
    {
        return herbivore;
    }
    
    public Species getCarnivore()
    {
        return carnivore;
    }
    
    public Species getLivestock()
    {
        return livestock;
    }
    
    public ArrayList<Species> getSpecies()
    {
        return species;
    }
}