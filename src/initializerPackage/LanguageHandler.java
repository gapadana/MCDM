package initializerPackage;

import java.io.*;
import java.util.Properties;

/**
 * Created by aebra on 7/29/2017.
 */
public class LanguageHandler {

    private static LanguageHandler instance = null;


    Properties fa;
    Properties en;
    private boolean faLoaded = false;
    private boolean enLoaded = false;

    public static final LanguageHandler getInstance(){
        if(instance==null){
            instance = new LanguageHandler();
        }
        return instance;
    }

    private LanguageHandler() {

        File file = new File("./en.properties");
        if(file.exists())
            enLoaded = loadEn();
        file = new File("./fa.properties");
        if(file.exists())
            faLoaded = loadFa();

    }

    private boolean loadEn() {
        return loadProperty(en, "./en.properties");
    }

    private boolean loadFa(){
        return loadProperty(fa, "./fa.properties");
    }

    private boolean loadProperty(Properties properties, String path){
        properties = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            return false;
        }
        try {
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
