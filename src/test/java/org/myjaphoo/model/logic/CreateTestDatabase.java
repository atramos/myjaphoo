/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author lang
 */
public class CreateTestDatabase   {

    private final Logger logger = LoggerFactory.getLogger(CreateTestDatabase.class.getName());
    private MovieEntryJpaController movieController = new MovieEntryJpaController();
    private ThumbnailJpaController thumbController = new ThumbnailJpaController();

    
    URL url = CreateTestDatabase.class.getResource("demothumb.jpg");

    public static void main(String[] args) throws IOException, Exception {
        CreateTestDatabase mp = new CreateTestDatabase();
        mp.testGrowDatabasewithtestdata();
        //mp.list("m:podcast/", new MovieDelegator());
    }
    private int MAXGEN = 50000;

    private long unique = new Date().getTime();

    public void flushDatabase() {
        MyjaphooDB.singleInstance().emClear();
    }

    private String createDemoFileName(int i) {
        return "filename" + i + ".jpg";
    }

    private String createDemoPath(int i) {
        StringBuffer b = new StringBuffer();
        b.append("demorootdir" + unique);
        int level1 = i % 5;
        b.append("/aaaaa_" + level1);
        int level2 = i % 15;
        b.append("/bbbbbbb" + level2);
        int level3 = i % 40;
        b.append("/ccccccc" + level3);
        int level4 = i % 80;
        b.append("/ddddddd" + level4);
        int level5 = i % 150;
        b.append("/eeeeeee" + level5);

        return b.toString();
    }

    public void testGrowDatabasewithtestdata() throws IOException, Exception {
        BufferedImage img = Picture.loadimage(new File(url.getFile()));
        for (int i=0; i<MAXGEN; i++) {
            genMovie(img, i);
            if (i % 100 == 0) {
                flushDatabase();
            }
        }
    }


    public MovieEntry genMovie(BufferedImage img, int i) throws IOException {
        Random r = new Random();

        String path = createDemoPath(r.nextInt());
        MovieEntry movieEntry = new MovieEntry();
        String name = createDemoFileName(i);
        movieEntry.setName(name);
        movieEntry.setCanonicalDir(path);
        movieEntry.setFileLength(150000);
        

        movieEntry.setChecksumCRC32(r.nextLong());
        logger.info("saving ");
        movieController.create(movieEntry);
        Thumbnail t = new Thumbnail();

        t.setThumbnail(Picture.toByte(img));
        t.setMovieEntry(movieEntry);
        thumbController.create(t);
        logger.info("creating thumbs");

        return movieEntry;

    }




}
