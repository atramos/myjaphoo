/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.myjaphoo.model.StructureType;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Contains the information to a certain "view" on the media data.
 *
 * @author lang
 */
@Embeddable
public class DataView implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Column(length = 4048)
    private String filterExpression = "";

    @Column(length = 4048)
    private String preFilterExpression = "";

    /**
     * the current selected token; may be null, or may be the root of the tokens, which means, that no token is selected.
     */
    @Transient
    transient private Token currentToken;
    @Transient
    transient private MetaToken currentMetaToken;
    @Column(length = 4048)
    private String currentSelectedDir = null;
    /**
     * soll im thumbnail fenster auch alle movies der darunter liegenden
     * ordner enthalten sein?
     */
    private boolean listChildMovies = false;
    /**
     * sollen leere verzeichnisse im tree verkürzt werden?
     */
    private boolean pruneTree = false;
    @Enumerated(EnumType.STRING)
    private StructureType structType = StructureType.DIRECTORY;
    @Column(length = 4000)
    private String userDefinedStruct = StructureType.DIRECTORY.buildUserDefinedEquivalentExpr();
    private boolean userDefinedStructureActivated = true;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;

    @Enumerated(EnumType.STRING)
    private ThumbMode thumbmode;

    public DataView() {
    }

    /**
     * Migration von geladenen Alt-Daten:
     * In Zukunft gibts nur noch userdefined structures, keine
     * struct-types mehr.
     * struct-types werden einfach nur noch in userdefined structures
     * überführt.
     * Diese Methode macht das für geladene Alt-daten aus der DB.
     */
    @PostLoad
    public void migrate31() {
        if (!userDefinedStructureActivated) {
            userDefinedStruct = structType.buildUserDefinedEquivalentExpr();
            userDefinedStructureActivated = true;
        }
    }

    @Override
    public Object clone() {
        try {
            DataView cloned = (DataView) super.clone();
            return cloned;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("internal error!");
        }
    }


    public boolean isContentequals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        DataView other = (DataView) obj;

        return new EqualsBuilder().append(filterExpression, other.filterExpression)
                .append(preFilterExpression, other.preFilterExpression)
                .append(currentSelectedDir, other.currentSelectedDir)
                .append(listChildMovies, other.listChildMovies)
                .append(pruneTree, other.pruneTree)
                .append(isUserDefinedStructureActivated(), other.isUserDefinedStructureActivated())
                .append(userDefinedStruct, other.userDefinedStruct).isEquals();
    }

    /**
     * @return the filter
     */
    public boolean isFilter() {
        return !StringUtils.isEmpty(filterExpression) || !StringUtils.isEmpty(preFilterExpression);
    }

    /**
     * @return the filterPattern
     */
    public String getFilterExpression() {
        return filterExpression;
    }

    public String getCombinedFilterExpression() {
        // if necessary combine them:
        String combinedExpr = "";
        boolean preFilterIsEmpty = StringUtils.isEmpty(preFilterExpression);
        boolean filterIsEmpty = StringUtils.isEmpty(filterExpression);
        if (!preFilterIsEmpty && !filterIsEmpty) {
            combinedExpr = getPreFilterExpression() + " and " + getFilterExpression();
        } else if (preFilterIsEmpty && !filterIsEmpty) {
            combinedExpr = getFilterExpression();
        } else if (!preFilterIsEmpty && filterIsEmpty) {
            combinedExpr = getPreFilterExpression();
        }
        return combinedExpr;
    }

    /**
     * @param filterPattern the filterPattern to set
     */
    public void setFilterExpression(String filterPattern) {
        this.filterExpression = filterPattern;
    }

    /**
     * @return the currentSelectedDir
     */
    public String getCurrentSelectedDir() {
        return currentSelectedDir;
    }

    /**
     * @param currentSelectedDir the currentSelectedDir to set
     */
    public void setCurrentSelectedDir(String currentSelectedDir) {
        this.currentSelectedDir = currentSelectedDir;
    }

    /**
     * @param currentToken the currentToken to set
     */
    public void setCurrentToken(Token currentToken) {
        this.currentToken = currentToken;
    }

    /**
     * @return the listChildMovies
     */
    public boolean isListChildMovies() {
        return listChildMovies;
    }

    /**
     * @param listChildMovies the listChildMovies to set
     */
    public void setListChildMovies(boolean listChildMovies) {
        this.listChildMovies = listChildMovies;
    }

    /**
     * @return the pruneTree
     */
    public boolean isPruneTree() {
        return pruneTree;
    }

    /**
     * @param pruneTree the pruneTree to set
     */
    public void setPruneTree(boolean pruneTree) {
        this.pruneTree = pruneTree;
    }

    /**
     * @return the currentToken
     */
    public Token getCurrentToken() {
        return currentToken;
    }

    /**
     * @param userDefinedStructure the userDefinedStructure to set
     */
    public void setUserDefinedStructure(List<GroupingDim> userDefinedStructure) {
        this.setUserDefinedStruct(StringUtils.join(userDefinedStructure, ";"));
    }

    /**
     * @return the userDefinedStructureActivated
     */
    public boolean isUserDefinedStructureActivated() {
        return userDefinedStructureActivated;
    }

    /**
     * @param userDefinedStructureActivated the userDefinedStructureActivated to set
     */
    public void setUserDefinedStructureActivated(boolean userDefinedStructureActivated) {
        this.userDefinedStructureActivated = userDefinedStructureActivated;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUserDefinedStruct() {
        return userDefinedStruct;
    }

    /**
     * @param userDefinedStruct the userDefinedStruct to set
     */
    public void setUserDefinedStruct(String userDefinedStruct) {
        this.userDefinedStruct = userDefinedStruct;
        userDefinedStructureActivated = true;
    }

    public void setCurrentMetaToken(MetaToken currentMetaToken) {
        this.currentMetaToken = currentMetaToken;
    }

    public MetaToken getCurrentMetaToken() {
        return currentMetaToken;
    }

    /**
     * @return the thumbmode
     */
    public ThumbMode getThumbmode() {
        return thumbmode;
    }

    /**
     * @param thumbmode the thumbmode to set
     */
    public void setThumbmode(ThumbMode thumbmode) {
        this.thumbmode = thumbmode;
    }

    public String getPreFilterExpression() {
        return preFilterExpression;
    }

    public void setPreFilterExpression(String preFilterExpression) {
        this.preFilterExpression = preFilterExpression;
    }
}
