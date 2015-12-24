package org.myjaphoo.gui.util

import groovy.transform.TypeChecked
import org.myjaphoo.MyjaphooApp
import org.myjaphoo.gui.action.OpenFileExplorer
import org.myjaphoo.model.filterparser.idents.Identifiers

import javax.swing.*
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener

/**
 * A hyperlink listener which reacts on hyperlinks in html in swing components that denote special myjaphoo objects.
 * @author mla
 * @version $Id$
 *
 */
@TypeChecked
class MJHyperlinkListener implements HyperlinkListener {

    JComponent component;
    JToolTip toolTip;

    MJHyperlinkListener() {

    }

    MJHyperlinkListener(JComponent component) {
        this.component = component;
        toolTip = component.createToolTip();
    }

    @Override
    void hyperlinkUpdate(HyperlinkEvent e) {
        ParsedEvent pe = parseEvent(e);

        if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
            switch(pe.type) {
                case PTypes.tagref:   openNewViewForTag(pe); break;
                case PTypes.metatagref: openNewViewForMetaTag(pe); break;
                case PTypes.file: OpenFileExplorer.openEntryPathInExplorer(pe.linkContent); break;
            }

        } else {
            boolean entered = HyperlinkEvent.EventType.ENTERED.equals(e.getEventType())
            String descr = e.getDescription()

            switch(pe.type) {
                case PTypes.tagref:  descr = "click opens new view filtered by tag '$pe.linkContent'"; break;
                case PTypes.metatagref: descr = "click opens new view filtered by meta tag '$pe.linkContent'"; break;
                case PTypes.file: descr = "click opens system explorer for '$pe.linkContent'"; break;
            }

            String toolTipTxt = entered ? descr : null;
            if (component != null) {

                component.setToolTipText(toolTipTxt);
                toolTip.repaint();
            }
        }
    }

    enum PTypes {
        nothing,
        tagref,
        metatagref,
        file
    }

    class ParsedEvent {
        PTypes type = PTypes.nothing;
        String linkContent
    }

    def ParsedEvent parseEvent(HyperlinkEvent e) {
        String refDescr = e.getDescription();
        if (refDescr.contains("=")) {
            String[] parsed = refDescr.split("=")
            if (parsed.length == 2) {
                ParsedEvent pe = new ParsedEvent()
                pe.type = PTypes.valueOf(PTypes, parsed[0])
                pe.linkContent = parsed[1]
                return pe;
            }
        }
        // return an "empty" event
        return new ParsedEvent();
    }


    private void openNewViewForTag(ParsedEvent pe) {
        final String expr = Identifiers.TAG.getName() + " is '" + pe.linkContent + "'"; //NOI18N
        MyjaphooApp.getApplication().startNewView(expr);
    }

    private void openNewViewForMetaTag(ParsedEvent pe) {
        final String expr = Identifiers.METATAG.getName() + " is '" + pe.linkContent + "'"; //NOI18N
        MyjaphooApp.getApplication().startNewView(expr);
    }
}