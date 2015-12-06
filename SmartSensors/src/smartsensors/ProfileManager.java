package smartsensors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ProfileManager
{   
    public static <T> Boolean saveProfile(T prof, String filePath)
    {
        try
        {
           FileOutputStream fileOut = new FileOutputStream(filePath);
           ObjectOutputStream out = new ObjectOutputStream(fileOut);
           out.writeObject(prof);
           out.close();
           fileOut.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static <T> T loadProfile(String filePath)
    {
        T profile = null;
        try
        {
           FileInputStream fileIn = new FileInputStream(filePath);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           profile = (T)in.readObject();
           in.close();
           fileIn.close();
        }
        catch(IOException i) {i.printStackTrace(); return null;}
        catch(ClassNotFoundException c) {c.printStackTrace(); return null;}
        
        return profile;
    }
}
