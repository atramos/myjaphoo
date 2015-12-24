/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.pictureComparison;

import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.logic.imgCompare.State;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Funktionen, um Ähnliche Bilder zu ermitteln.
 * Cached die information über ähnlichkeit für bilder.
 * @author mla
 */
public class PictureComparison {

    private static Map<Long, SoftReference<State[]>> affinityMapping = new HashMap<Long, SoftReference<State[]>>();
    private static Map<Long, SoftReference<State[]>> affinityMappingV2 = new HashMap<Long, SoftReference<State[]>>();
    private static Map<Long, SoftReference<State[]>> affinityMappingV2_normiert = new HashMap<Long, SoftReference<State[]>>();

    public static int compareAehnlichkeit(MovieEntry m1, MovieEntry m2) {
        return compareStates(getAehnlichkeit(m1), getAehnlichkeit(m2));
    }

    public static int compareAehnlichkeitV2(MovieEntry m1, MovieEntry m2) {
        return compareStates(getAehnlichkeitV2(m1), getAehnlichkeitV2(m2));
    }

    public static int compareAehnlichkeitV3(MovieEntry m1, MovieEntry m2) {
        return compareStates(getAehnlichkeitV2_normiert(m1), getAehnlichkeitV2_normiert(m2));
    }

    private static State[] calcStates_Method2(MovieEntry m) {
        byte[] tumbnailbytes = m.getThumbnail1();
        if (tumbnailbytes != null) {
            InputStream input = new java.io.ByteArrayInputStream(tumbnailbytes);
            try {
                BufferedImage i = javax.imageio.ImageIO.read(input);
                float xfact = 1f / i.getWidth();
                float yfact = 1f / i.getHeight();
                BufferedImage i1 = Picture.getScaledInstance(i, xfact, yfact);
                State[] states = new State[8];
                states[0] = new State(i1);
                xfact = 2f / i.getWidth();
                yfact = 2f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[1] = new State(i1);
                xfact = 4f / i.getWidth();
                yfact = 4f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[2] = new State(i1);
                xfact = 8f / i.getWidth();
                yfact = 8f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[3] = new State(i1);
                xfact = 16f / i.getWidth();
                yfact = 16f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[4] = new State(i1);
                xfact = 32f / i.getWidth();
                yfact = 32f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[5] = new State(i1);
                xfact = 64f / i.getWidth();
                yfact = 64f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[6] = new State(i1);
                xfact = 128f / i.getWidth();
                yfact = 128f / i.getHeight();
                i1 = Picture.getScaledInstance(i, xfact, yfact);
                states[7] = new State(i1);
                return states;
            } catch (IOException ex) {
                LoggerFactory.getLogger(MovieEntry.class.getName()).error("error", ex); //NOI18N
            }
        }
        return null;
    }

    private static State[] getState(Map<Long, SoftReference<State[]>> map, MovieEntry m) {
        SoftReference<State[]> ref = map.get(m.getId());
        if (ref != null) {
            return ref.get();
        } else {
            return null;
        }
    }
    
    private static State[] getAehnlichkeit(MovieEntry m) {
        State[] affinity = getState(affinityMapping, m);
        if (affinity == null) {

            affinity = calcStates(m);

            affinityMapping.put(m.getId(), new SoftReference(affinity));
        }
        return affinity;
    }

    private static State[] calcStates(MovieEntry m) {
        byte[] tumbnailbytes = m.getThumbnail1();
        if (tumbnailbytes != null) {
            InputStream input = new java.io.ByteArrayInputStream(tumbnailbytes);
            try {
                BufferedImage i = javax.imageio.ImageIO.read(input);
                State[] affinity = new State[8];
                affinity[0] = new State(i, 127, 127);
                affinity[1] = new State(i, 64, 64);
                affinity[2] = new State(i, 32, 32);
                affinity[3] = new State(i, 16, 16);
                affinity[4] = new State(i, 8, 8);
                affinity[5] = new State(i, 4, 4);
                affinity[6] = new State(i, 2, 2);
                affinity[7] = new State(i, 1, 1);
                return affinity;
            } catch (IOException ex) {
                LoggerFactory.getLogger(MovieEntry.class.getName()).error("error", ex); //NOI18N
            }
        }
        return null;
    }

    private static State[] getAehnlichkeitV2(MovieEntry m) {
        State[] affinityV2 = getState(affinityMappingV2, m);
        if (affinityV2 == null) {
            affinityV2 = calcStates_Method2(m);
            affinityMappingV2.put(m.getId(), new SoftReference(affinityV2));
        }
        return affinityV2;
    }

    private static State[] getAehnlichkeitV2_normiert(MovieEntry m) {

        State[] affinityV2_normiert = getState(affinityMappingV2_normiert, m);
        if (affinityV2_normiert == null) {
            affinityV2_normiert = calcStates_Method2(m);
            //normieren:
            if (affinityV2_normiert != null) {
                for (State anAffinityV2_normiert : affinityV2_normiert) {
                    anAffinityV2_normiert.normalizeBrightnessMap();
                }
            }
            affinityMappingV2_normiert.put(m.getId(), new SoftReference(affinityV2_normiert));
        }
        return affinityV2_normiert;
    }

    private static int compareStates(State[] s1, State[] s2) {

        for (int i = 0; i < s1.length; i++) {
            int c = compareStates(s1[i], s2[i]);
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    private static int compareStates(State s1, State s2) {

        int w = s1.width;
        if (s2.width < w) {
            w = s2.width;
        }
        int h = s1.height;
        if (s2.height < h) {
            h = s2.height;
        }

        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                int v1 = s1.map[x][y];
                int v2 = s2.map[x][y];
                if (v1 == v2) {
                    continue;
                }
                return v1 - v2;
            }
        }
        return 0;
    }
    
    
    private static int compareStates2(State s1, State s2) {

        int diffCounter ;
        
        int w = s1.width;
        if (s2.width < w) {
            w = s2.width;
        }
        int h = s1.height;
        if (s2.height < h) {
            h = s2.height;
        }

        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                int v1 = s1.map[x][y];
                int v2 = s2.map[x][y];
                if (v1 == v2) {
                    continue;
                }
                return v1 - v2;
            }
        }
        return 0;
    }    
}
