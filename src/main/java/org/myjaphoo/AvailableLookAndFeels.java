package org.myjaphoo;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.*;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * The list of look and fields, that we provide the user to select from.
 *
 * @author mla
 * @version $Id$
 */
public enum AvailableLookAndFeels {

    NIMBUS("Nimbus") {
        @Override
        void activate() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(new NimbusLookAndFeel());
                    } catch (UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    },

    AUTUMNSKIN(new AutumnSkin()),
    BUSINESSBLACKSTEELSKIN(new BusinessBlackSteelSkin()),
    BUSINESSBLUESTEELSKIN(new BusinessBlueSteelSkin()),
    BUSINESSSKIN(new BusinessSkin()),
    CERULEANSKIN(new CeruleanSkin()),
    CHALLENGERDEEPSKIN(new ChallengerDeepSkin()),
    CREMECOFFEESKIN(new CremeCoffeeSkin()),
    CREMESKIN(new CremeSkin()),
    DUSTSKIN(new DustSkin()),
    EMERALDDUSKSKIN(new EmeraldDuskSkin()),
    GEMINISKIN(new GeminiSkin()),
    GRAPHITEAQUASKIN(new GraphiteAquaSkin()),
    GRAPHITEGLASSSKIN(new GraphiteGlassSkin()),
    GRAPHITESKIN(new GraphiteSkin()),
    MAGELLANSKIN(new MagellanSkin()),
    MARINERSKIN(new MarinerSkin()),
    MISTAQUASKIN(new MistAquaSkin()),
    MISTSILVERSKIN(new MistSilverSkin()),
    MODERATESKIN(new ModerateSkin()),
    NEBULASKIN(new NebulaSkin()),
    OFFICEBLACK2007SKIN(new OfficeBlack2007Skin()),
    OFFICEBLUE2007SKIN(new OfficeBlue2007Skin()),
    OFFICESILVER2007SKIN(new OfficeSilver2007Skin()),
    RAVENSKIN(new RavenSkin()),
    SAHARASKIN(new SaharaSkin()),
    TWILIGHTSKIN(new TwilightSkin());

    private String UIName;
    private SubstanceSkin substanceSkin;

    private AvailableLookAndFeels(String UIName) {
        this.UIName = UIName;
    }

    private AvailableLookAndFeels(SubstanceSkin substanceSkin) {
        this.substanceSkin = substanceSkin;
    }

    void activate() {
        if (substanceSkin != null) {
            SubstanceLookAndFeel.setSkin(substanceSkin);
        }
    }

    public String getUIName() {
        if (substanceSkin != null) {
            return substanceSkin.getDisplayName();
        }
        return UIName;
    }
}
