import java.lang.*;

public class Species
{
    private String name;
    private String wildName;
    private boolean isAnimal;
    private boolean isDomestic;
    private int preferredTemp;
    private int preferredHumidity;
    
    public Species(String _name, boolean _isAnimal, boolean _isDomestic, int _preferredTemp, int _preferredHumidity)
    {
        name = _name;
        wildName = "Wild " + name;
        isAnimal = _isAnimal;
        isDomestic = _isDomestic;
        preferredTemp = _preferredTemp;
        preferredHumidity = _preferredHumidity;
    }
    
    public Species(String _name, boolean _isAnimal, String _wildName, int _preferredTemp, int _preferredHumidity)
    {
        name = _name;
        wildName = _wildName;
        isAnimal = _isAnimal;
        isDomestic = true;
        preferredTemp = _preferredTemp;
        preferredHumidity = _preferredHumidity;
    }
    
    public int suitability(int actualTemp, int actualHumidity, int actualAltitude)
    {
        return 200 - Math.abs(actualTemp - preferredTemp) - Math.abs(actualHumidity - preferredHumidity);
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getWildName()
    {
        return wildName;
    }
    
    public boolean isDomestic()
    {
        return isDomestic;
    }
    
    public static Species bamboo = new Species("Bamboo", false,false,70,100);
    public static Species pine = new Species("Pine", false,false,50,60);
    public static Species spruce = new Species("Spruce", false,false,30,70);
    public static Species elm = new Species("Elm", false,false,60,90);
    public static Species birch = new Species("Birch", false,false,40,90);
    public static Species oak = new Species("Oak", false,false,65,65);
    public static Species poplar = new Species("Poplar", false,false,40,75);
    public static Species acacia = new Species("Acacia", false,false,80,50);
    public static Species teak = new Species("Teak", false,false,90,70);
    public static Species ebony = new Species("Ebony", false,false,90,90);
    
    static Species[] trees = {bamboo, pine, spruce, elm, birch, oak, poplar, acacia, teak, ebony};
    
    public static Species taro = new Species("Taro", false,true,90,80);
    public static Species breadfruit = new Species("Breadfruit", false,true,90,90);
    public static Species sorghum = new Species("Sorghum", false,true,70,60);
    public static Species barley = new Species("Barley", false,true,40,80);
    public static Species wheat = new Species("Wheat", false,"Wild Emmer",50,70);
    public static Species rice = new Species("Rice", false,true,70,90);
    public static Species millet = new Species("Millet", false,true,50,40);
    
    static Species[] carbs = {taro, breadfruit, sorghum, barley, wheat, rice, millet};
    
    public static Species chickpea = new Species("Chickpea", false,true,70,70);
    public static Species cowpea = new Species("Black-Eyed Pea", false,"Cowpea",80,40);
    public static Species favabean = new Species("Fava Bean", false,"Vetch",60,70);
    public static Species soybean = new Species("Soybean", false,true,80,80);
    public static Species lentil = new Species("Lentil", false,true,40,60);
    
    static Species[] broteins = {chickpea, cowpea, favabean, soybean, lentil};
    
    public static Species deer = new Species("Deer", true,false,40,70);
    public static Species rabbit = new Species("Rabbit", true,false,50,70);
    public static Species elephant = new Species("Elephant", true,false,80,70);
    public static Species gazelle = new Species("Gazelle", true,false,80,40);
    public static Species goose = new Species("Goose", true,false,70,85);
    public static Species monkey = new Species("Monkey", true,false,90,90);
    public static Species boar = new Species("Boar", true,false,60,80);
    public static Species moose = new Species("Moose", true,false,20,75);
    public static Species otter = new Species("Otter", true,false,35,85);
    public static Species hyrax = new Species("Hyrax", true,false,70,50);
    
    static Species[] herbivores = {deer, rabbit, elephant, gazelle, goose, monkey, boar, moose, otter, hyrax};
    
    public static Species lion = new Species("Lion", true,false,80,40);
    public static Species wolf = new Species("Wolf", true,false,55,70);
    public static Species brownbear = new Species("Brown Bear", true,false,40,70);
    public static Species polarbear = new Species("Polar Bear", true,false,10,100);
    public static Species leopard = new Species("Leopard", true,false,80,85);
    public static Species honeybadger = new Species("Honey Badger", true,false,80,30);
    public static Species coyote = new Species("Coyote", true,false,70,50);
    
    static Species[] carnivores = {lion, wolf, brownbear, polarbear, leopard, honeybadger, coyote};
    
    public static Species camel = new Species("Camel", true,true,80,20);
    public static Species reindeer = new Species("Reindeer", true,true,10,80);
    public static Species horse = new Species("Horse", true,true,40,40){
        @Override
        public int suitability(int actualTemp, int actualHumidity, int actualAltitude)
        {
            if(actualAltitude<30)
            {
                return 200 - Math.abs(actualTemp - 40) - Math.abs(actualHumidity - 40);
            }
            else{return 0;}
        }
    };
    public static Species goat = new Species("Goat", true,"Ibex",50,35){
        @Override
        public int suitability(int actualTemp, int actualHumidity, int actualAltitude)
        {
            if(actualAltitude>=30)
            {
                return 200 - Math.abs(actualTemp - 50) - Math.abs(actualHumidity - 35);
            }
            else{return 0;}
        }
    };
    public static Species buffalo = new Species("Buffalo", true,true,80,80);
    
    static Species[] livestocks = {camel, reindeer, horse, goat, buffalo};
}