import java.util.*;

public class Language
{
    ArrayList<String> protowords;
    String soundChanges;
    boolean headInitial;
    boolean objectVerb;
    int extraPosition;
    boolean spacedCompounds;
    
    ArrayList<String> english;
    
    public static void main(String[] args)
    {}
    
    public Language(boolean _headInitial, boolean _objectVerb, int _extraPosition, boolean _spacedCompounds, ArrayList<String> _protowords, String _soundChanges)
    {
        headInitial = _headInitial;
        objectVerb = _objectVerb;
        extraPosition = _extraPosition;
        protowords = _protowords;
        soundChanges = _soundChanges;
        spacedCompounds = _spacedCompounds;
        
        english  = new ArrayList<String>(Arrays.asList("mountain","sea","river","plain","king","human","nation","city","language","speak","see","take","give"));
    }
    
    public String translate(String _subject, String _verb, String _object, String _extras)
    {
        String subject = _subject;
        String verb = _verb;
        String object = _object;
        String extras = _extras;
        String[] sentenceComponents = {subject,verb,object,extras};
        
        return "I'm sick of coding";
    }
}