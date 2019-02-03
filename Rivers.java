import java.util.*;

public class Rivers
{
    private Map map;
    private Flow[][] verts;
    private Flow[][] hors;
    private ArrayList<ArrayList<Flow>> flowsByAltitude;
    private Flow nullFlow;
    private Random r;
    
    public Rivers(Map _map)
    {
        map = _map;
        verts = new Flow[map.getTileDimensions()][map.getTileDimensions()];
        hors = new Flow[map.getTileDimensions()][map.getTileDimensions()];
        flowsByAltitude = new ArrayList<ArrayList<Flow>>();
        r = new Random();
        
        for(int i = 0; i <= 100; i++)
        {
            flowsByAltitude.add(new ArrayList<Flow>());
        }
        
        for(int x = 0; x<map.getTileDimensions(); x++)
        {
            for(int y = 0; y<map.getTileDimensions(); y++)
            {
                hors[x][y] = new Flow(x,y,false);
                verts[x][y] = new Flow(x,y,true);
            }
        }
        
        for(int i = 0; i<map.getTileDimensions(); i++)
        {
            ArrayList<Flow> theRing = ring(i);
            
            for(int j = 0; j<theRing.size(); j++)
            {
                
                flowsByAltitude.get(theRing.get(j).getAltitude()).add(theRing.get(j));
            }
        }
        
        for(int i = 0; i<=100; i++)
        {
            for(int j = 0; j<flowsByAltitude.get(i).size(); j++)
            {
                flowsByAltitude.get(i).get(j).findFlowsTo();
            }
        }
        
        for(int x = 0; x<map.getTileDimensions(); x++)
        {
            for(int y = 0; y<map.getTileDimensions(); y++)
            {
                Flow current = hors[x][y];
                //see what I did there?
                
                if(current.eventuallyFlowsToSea(map.getTileDimensions()))
                {
                    int sourceStrength = current.getSourceStrength();
                    
                    while(!current.isSeaside())
                    {
                        current.setStrength(current.getStrength() + sourceStrength);
                        current = current.getFlowsTo();
                    }
                }
                
                current = verts[x][y];
                
                if(current.eventuallyFlowsToSea(map.getTileDimensions()))
                {
                    int sourceStrength = current.getSourceStrength();
                    
                    while(!current.isSeaside())
                    {
                        current.setStrength(current.getStrength() + sourceStrength);
                        current = current.getFlowsTo();
                    }
                }
            }
        }
        
        /*for(int x = 0; x<map.getTileDimensions(); x++)
        {
            for(int y = 0; y<map.getTileDimensions(); y++)
            {
                if(hors[x][y].getFlowsTo() != nullFlow)
                {
                    if(hors[x][y].eventuallyFlowsToSea(100))
                    {
                        hors[x][y].getFlowsTo().setStrength(1000);
                    }
                    /*else
                    {
                        hors[x][y].getFlowsTo().setStrength(2000);
                    }*
                }
                
                if(verts[x][y].getFlowsTo() != nullFlow)
                {
                    if(verts[x][y].eventuallyFlowsToSea(100))
                    {
                        verts[x][y].getFlowsTo().setStrength(1000);
                    }
                    /*else
                    {
                        verts[x][y].getFlowsTo().setStrength(2000);
                    }*
                }
            }
        }*/
        
        for(int x = 0; x<map.getTileDimensions(); x++)
        {
            for(int y = 0; y<map.getTileDimensions(); y++)
            {
                map.getSquareLand(x,y).setLeftRiver((int)(Math.sqrt(verts[x][y].getStrength())/5.0));
                map.getSquareLand(x,y).setTopRiver((int)(Math.sqrt(hors[x][y].getStrength())/5.0));
                //map.getSquareLand(x,y).setLeftRiver((verts[x][y].getStrength() > 50)?5:0);
                //map.getSquareLand(x,y).setTopRiver((hors[x][y].getStrength() > 50)?5:0);
                
                if(y!=map.getTileDimensions()-1)
                {
                    map.getSquareLand(x,y).setBottomRiver((int)(Math.sqrt(hors[x][y+1].getStrength())/5.0));
                    //map.getSquareLand(x,y).setBottomRiver((hors[x][y+1].getStrength() > 50)?5:0);
                }
                
                if(x!=map.getTileDimensions()-1)
                {
                    map.getSquareLand(x,y).setRightRiver((int)(Math.sqrt(verts[x+1][y].getStrength())/5.0));
                    //map.getSquareLand(x,y).setRightRiver((verts[x+1][y].getStrength() > 50)?5:0);
                }
            }
        }
    }
    
    private ArrayList<Flow> ring(int distance)
    {
        ArrayList<Flow> theRing = new ArrayList<Flow>();
        
        for(int x = map.getTileDimensions()/2 - distance; x <= map.getTileDimensions()/2 + distance; x++)
        {
            for(int y = map.getTileDimensions()/2 - distance; y <= map.getTileDimensions()/2 + distance; y++)
            {
                if(x>=0 && x<map.getTileDimensions() && y>=0 && y<map.getTileDimensions() && ((int)(Math.sqrt(Math.pow((double)(x - map.getTileDimensions()/2),2) + Math.pow((double)(y - map.getTileDimensions()/2), 2)))) == distance)
                {
                    if(r.nextInt(2)==1)
                    {
                        theRing.add(hors[x][y]);
                        theRing.add(verts[x][y]);
                    }
                    else
                    {
                        theRing.add(verts[x][y]);
                        theRing.add(hors[x][y]);
                    }
                }
            }
        }
        
        return theRing;
    }
    
    private class Flow
    {
        private int sourceStrength;
        private int strength;
        private Flow flowsTo;
        int x;
        int y;
        private boolean isVert;
        private ArrayList<Flow> adjacentFlows;
        
        public Flow(int _x, int _y, boolean _isVert)
        {
            x = _x;
            y = _y;
            isVert = _isVert;
            strength = 0;
            flowsTo = nullFlow;
            adjacentFlows = new ArrayList<Flow>();
        }
        
        public int getStrength()
        {
            return strength;
        }
        
        public void setStrength(int _strength)
        {
            strength = _strength;
        }
        
        public Flow getFlowsTo()
        {
            return flowsTo;
        }
        
        public void setFlowsTo(Flow _flowsTo)
        {
            flowsTo = _flowsTo;
        }
        
        public int getAltitude()
        {
            if(isVert)
            {
                if(x==0)
                {
                    return map.getSquareLand(x,y).getAltitude();
                }
                else if(x==map.getTileDimensions()-1)
                {
                    return map.getSquareLand(x-1,y).getAltitude();
                }
                else
                {
                    return (map.getSquareLand(x-1,y).getAltitude() + map.getSquareLand(x,y).getAltitude())/2;
                }
            }
            else
            {
                if(y==0)
                {
                    return map.getSquareLand(x,y).getAltitude();
                }
                else if(y==map.getTileDimensions())
                {
                    return map.getSquareLand(x,y-1).getAltitude();
                }
                else
                {
                    return (map.getSquareLand(x,y-1).getAltitude() + map.getSquareLand(x,y).getAltitude())/2;
                }
            }
        }
        
        public int getSourceStrength()
        {
            if(isVert)
            {
                if(x==0)
                {
                    return map.getSquareLand(x,y).getHumidity();
                }
                else if(x==map.getTileDimensions())
                {
                    return map.getSquareLand(x-1,y).getHumidity();
                }
                else
                {
                    return (map.getSquareLand(x-1,y).getHumidity() + map.getSquareLand(x,y).getHumidity())/2;
                }
            }
            else
            {
                if(y==0)
                {
                    return map.getSquareLand(x,y).getHumidity();
                }
                else if(y==map.getTileDimensions())
                {
                    return map.getSquareLand(x,y-1).getHumidity();
                }
                else
                {
                    return (map.getSquareLand(x,y-1).getHumidity() + map.getSquareLand(x,y).getHumidity())/2;
                }
            }
        }
        
        public boolean isSeaside()
        {
            if(isVert)
            {
                if(x==0)
                {
                    return map.getSquareLand(x,y).getType()=="Sea";
                }
                else if(x==map.getTileDimensions())
                {
                    return map.getSquareLand(x-1,y).getType()=="Sea";
                }
                else
                {
                    return (map.getSquareLand(x-1,y).getType()=="Sea" || map.getSquareLand(x,y).getType()=="Sea");
                }
            }
            else
            {
                if(y==0)
                {
                    return map.getSquareLand(x,y).getType()=="Sea";
                }
                else if(y==map.getTileDimensions())
                {
                    return map.getSquareLand(x,y-1).getType()=="Sea";
                }
                else
                {
                    return (map.getSquareLand(x,y-1).getType()=="Sea" || map.getSquareLand(x,y).getType()=="Sea");
                }
            }
        }
        
        public boolean eventuallyFlowsToSea(int limit)
        {
            if(limit==0){return false;}
            else if(isSeaside()){return true;}
            else if(flowsTo == nullFlow){return false;}
            else {return flowsTo.eventuallyFlowsToSea(limit-1);}
        }
        
        public void findAdjacentFlows()
        {
            if(!isSeaside())
            {
                if(isVert)
                {
                    try
                    {
                        adjacentFlows.add(verts[x][y-1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(verts[x][y+1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(hors[x-1][y]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(hors[x][y+1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(hors[x-1][y+1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    adjacentFlows.add(hors[x][y]);
                }
                else
                {
                    try
                    {
                        adjacentFlows.add(hors[x-1][y]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(hors[x+1][y]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(verts[x][y-1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(verts[x+1][y]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    try
                    {
                        adjacentFlows.add(verts[x+1][y-1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    adjacentFlows.add(verts[x][y]);
                }
            }
        }
        
        public void findFlowsTo()
        {
            findAdjacentFlows();
            int optimal = getAltitude();
            ArrayList<Flow> possible = new ArrayList<Flow>();
            
            for(int i = 0; i<adjacentFlows.size(); i++)
            {
                if((adjacentFlows.get(i).getAltitude()<optimal) && (adjacentFlows.get(i).eventuallyFlowsToSea(100)))
                //if(adjacentFlows.get(i).getAltitude()<optimal)
                {
                    optimal = adjacentFlows.get(i).getAltitude();
                    possible.clear();
                    possible.add(adjacentFlows.get(i));
                }
                else if((adjacentFlows.get(i).getAltitude()==optimal) && (adjacentFlows.get(i).eventuallyFlowsToSea(100)))
                //else if(adjacentFlows.get(i).getAltitude()==optimal)
                {
                    optimal = adjacentFlows.get(i).getAltitude();
                    possible.add(adjacentFlows.get(i));
                }
            }
            
            if(possible.size()>1)
            {
                ArrayList<Flow> likely = new ArrayList<Flow>();
                for(int i = 0; i<possible.size(); i++)
                {
                    for(int j = 0; j<adjacentFlows.size(); j++)
                    {
                        if(adjacentFlows.get(j).getFlowsTo() == possible.get(i))
                        {
                            likely.add(possible.get(i));
                        }
                    }
                }
                if(likely.size()>0){possible = likely;}
            }
            
            if(possible.size()>0)
            {
                flowsTo = possible.get(r.nextInt(possible.size()));
            }
        }
    }
}