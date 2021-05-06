package rzaeditor.pageobjects;

import java.util.Arrays;
import java.util.HashMap;

public abstract class WireObject extends PageObject {
    public static HashMap<String, PageObject> objClasses = new HashMap<String, PageObject>();   
    static{
        //objClasses.put("Wire", value)
    }
    public void read(String s){
        String[] split = s.split("\n");
        for (int i = 0; i < split.length; i++) {
            String s0 = split[i];
            
            String[] split1 = s0.split("\t");
            String objClassName = split1[0];
            
            String[] objArgs = new String[split1.length-1];
            for(int j=1; j<split1.length; j++){
                objArgs[j-1]=split1[j];
            }
            
            
        }
    }
}
