/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.systemprops;

import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.util.tables.BaseTable;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import static org.mlsoft.swing.jtable.ColDescr.col;

/**
 * @author mla
 */
public class SystemPropsTable extends BaseTable {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/systemprops/resources/SystemPropsPanel");

    private MyjaphooController controller;

    private MappedTableModel<SysPropsEntry> model;

    public SystemPropsTable(MyjaphooController controller) {
        this.controller = controller;
        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
        List<SysPropsEntry> entries = buildEntries();

        model = MappedTableModel.configure(this, entries, SysPropsEntry.class, localeBundle,
                col("name", "Name", false),
                col("value", "Value", false)
        );
    }

    private List<SysPropsEntry> buildEntries() {
        ArrayList<SysPropsEntry> sysprops = new ArrayList<>();

        Enumeration enumeration = System.getProperties().propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            sysprops.add(new SysPropsEntry(name, System.getProperty(name)));
        }

        // supported picture formats:
        for (String formatName : ImageIO.getReaderFormatNames()) {
            sysprops.add(new SysPropsEntry("imageIO.SupportedFormat", formatName));
        }

        return sysprops;
    }


    public void refreshModel() {
        model.refresh(buildEntries());
    }

    public MappedTableModel<SysPropsEntry> getSysPropsModel() {
        return model;
    }
}
