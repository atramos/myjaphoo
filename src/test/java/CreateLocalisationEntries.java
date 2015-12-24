import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Helper class to create locatisation ressource files for adding an additional
 * language set to myjaphoo.
 * Simple hard coded class to copy a language set to a new one.
 * 
 * @author lang
 */
public class CreateLocalisationEntries {

    private static Logger logger = LoggerFactory.getLogger(CreateLocalisationEntries.class);

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        CreateLocalisationEntries cmle = new CreateLocalisationEntries();
        cmle.create();
    }

    private void create() throws IOException {
        String[] extensions = {"properties"};


        Collection<File> allPropFiles = org.apache.commons.io.FileUtils.listFiles(new File("src"), extensions, true);
        for (File file : allPropFiles) {
            if (!file.getAbsolutePath().endsWith("_fr_CA.properties")) {
                String newfilename = StringUtils.replace(file.getAbsolutePath(), ".properties", "_de_DE.properties");
                org.apache.commons.io.FileUtils.copyFile(file, new File(newfilename));
            
            }
        }
    }

}
