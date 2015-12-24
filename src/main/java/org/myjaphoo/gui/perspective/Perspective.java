/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.perspective;

import java.util.ResourceBundle;

import static org.myjaphoo.gui.perspective.View.*;

/**
 * @author mla
 */
public enum Perspective {

    Normal(
            InfoView,
            LogView,
            ThumbView,
            MovieTreeView,
            ChronicView,
            TokenView,
            ChangelogView,
            BookmarkView,
            PreviewView,
            MetaTokenView,
            PropertiesView,
            GroupByView,
            ExifView,
            MessageView,
            DatabaseConnectionView,
            FilterView,
            ErrorView,
            FileView,
            SystemPropsView,
            GroovyShellView),

    PictureViewing(FilterView, ThumbView, MovieTreeView, PreviewView),

    Research(FilterView, ThumbView,
            MovieTreeView,
            TokenView,
            BookmarkView
    ),

    MetaTokenEditing(FilterView, TokenView, MetaTokenView),

    Scripting(FilterView, LogView, ThumbView, MovieTreeView, TokenView, GroovyShellView),

    All(View.values());

    /**
     * @return the visibleViews
     */
    public View[] getVisibleViews() {
        return visibleViews;
    }


    /**
     * alle sichtbaren views für diese Perspektive; alle anderen sind angedockt.
     */
    private View[] visibleViews;

    private Perspective(View... visibleViews) {
        this.visibleViews = visibleViews;
    }

    public String getGuiName() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/perspective/resources/Perspective");
        return localeBundle.getString(name());
    }

}
