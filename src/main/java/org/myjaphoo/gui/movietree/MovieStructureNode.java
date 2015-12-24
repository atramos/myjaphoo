/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.MovieNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Node, zum strukturieren von movies im movietree.
 * @author lang
 */
public class MovieStructureNode extends AbstractMovieTreeNode {

    private String name;
    private Integer numOfContainingMovies = null;
    private Long size = null;
    /** the dimension or grouping expression for this node. */
    private String groupingExpr;
    /**
     * kanonischer directory pfad, der durch diese struktur-node repräsentiert wird. Kann auch null sein,
     * falls diese node kein directory repräsentiert.
     */
    private String canonicalDir;

    /**
     * lightweight reference to an object that contains certain characteristics for that particular node,
     * e.g. special menu entries, etc.
     */
    private StructureCharacteristics structureCharacteristics = StructureCharacteristicsFactory.NO_CHARACTERISTICS;

    private HashMap<String, Double> aggregatedValues = null;

    public MovieStructureNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getNumOfContainingMovies() {
        if (numOfContainingMovies != null) {
            return numOfContainingMovies.intValue();
        } else {
            int count = 0;
            for (AbstractTreeNode node : getChildren()) {
                if (node instanceof MovieStructureNode) {
                    count += ((MovieStructureNode) node).getNumOfContainingMovies();
                } else if (node instanceof MovieNode) {
                    count++;
                }
            }

            numOfContainingMovies = count;
            return numOfContainingMovies.intValue();
        }
    }

    @Override
    public long getSizeOfContainingMovies() {
        if (size != null) {
            return size;
        } else {
            long count = 0;
            for (AbstractTreeNode node : getChildren()) {
                if (node instanceof MovieStructureNode) {
                    count += ((MovieStructureNode) node).getSizeOfContainingMovies();
                } else if (node instanceof MovieNode) {
                    count += ((MovieNode) node).getMovieEntry().getFileLength();
                }
            }

            size = count;
            return count;
        }
    }


    /**
     * @return the canonicalDir
     */
    public String getCanonicalDir() {
        return canonicalDir;
    }

    /**
     * @param canonicalDir the canonicalDir to set
     */
    public void setCanonicalDir(String canonicalDir) {
        this.canonicalDir = canonicalDir;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public String getGroupingExpr() {
        return groupingExpr;
    }

    public void setGroupingExpr(String groupingExpr) {
        this.groupingExpr = groupingExpr;
    }

    public StructureCharacteristics getStructureCharacteristics() {
        return structureCharacteristics;
    }

    public void setStructureCharacteristics(StructureCharacteristics structureCharacteristics) {
        this.structureCharacteristics = structureCharacteristics;
    }

    public Set<String> getAllAggregatedKeys() {
        if (aggregatedValues == null) {
            return Collections.emptySet();
        } else {
            return aggregatedValues.keySet();
        }
    }

    public Double getAggregatedValue(String key) {
        if (aggregatedValues == null) {
            return null;
        } else {
            return aggregatedValues.get(key);
        }
    }

    public void putAggregatedValue(String key, Double value) {
        if (aggregatedValues == null) {
            aggregatedValues = new HashMap<>();
        }
        aggregatedValues.put(key, value);
    }
}
