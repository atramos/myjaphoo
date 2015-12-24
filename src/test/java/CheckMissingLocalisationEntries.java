import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Helper class to find missing localisation entries in property files.
 * 
 * Scans all *.properties files within the project and all its corresponding 
 * french properties.
 * Prints out all property entries where both entries are identical, or where
 * the french entry is missing.
 * 
 * These are probably entries where a french translation is missing.
 * 
 * @author lang
 */
public class CheckMissingLocalisationEntries {

    private static Logger logger = LoggerFactory.getLogger(CheckMissingLocalisationEntries.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        CheckMissingLocalisationEntries cmle = new CheckMissingLocalisationEntries();
        cmle.check();
    }

    private void check() {
        String[] extensions = {"properties"};


        Collection<File> allPropFiles = org.apache.commons.io.FileUtils.listFiles(new File("src"), extensions, true);
        for (File file : allPropFiles) {
            if (!file.getAbsolutePath().endsWith("_fr_CA.properties")) {
                checkPropFile(file);
            }
        }
    }

    private void checkPropFile(File defaultFile) {
        String frenchFileName = StringUtils.replace(defaultFile.getAbsolutePath(), ".properties", "_fr_CA.properties");
        File frenchFile = new File(frenchFileName);
        if (frenchFile.exists()) {
            try {
                checkEntries(defaultFile, frenchFile);
            } catch (Exception ex) {
                logger.error("error checking file " + defaultFile.getAbsolutePath(), ex);
            }
        } else {
            logger.warn("no french localisation file for " + defaultFile.getAbsolutePath());
        }
    }

    private void checkEntries(File file, File frenchFile) throws FileNotFoundException, IOException {
        Properties pDefault = new Properties();
        pDefault.load(new FileReader(file));

        Properties pFrench = new Properties();
        pFrench.load(new FileReader(frenchFile));

        // check all entries
        for (String propName : pDefault.stringPropertyNames()) {
            String defaultPropVal = pDefault.getProperty(propName);
            String frenchPropVal = pFrench.getProperty(propName);
            if (StringUtils.isEmpty(frenchPropVal)) {
                logger.error("Missing french Entry: " + propName + " in file " + frenchFile.getAbsolutePath());
            } else if (StringUtils.equals(defaultPropVal, frenchPropVal)) {
                logger.error("Missing french Entry (entry is equal to default entry): " + propName + " in file " + frenchFile.getAbsolutePath());
            }
        }
    }
}
