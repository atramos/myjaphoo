package org.myjaphoo.gui.repath;

import org.mlsoft.swing.jtable.ChangeAction;
import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.util.tables.BaseTable;
import org.myjaphoo.model.db.PathMapping;
import org.myjaphoo.model.logic.PathMappingJpaController;
import org.myjaphoo.util.MJObservableList;
import org.slf4j.LoggerFactory;

import static org.mlsoft.swing.jtable.ColDescr.col;

/**
 * RelPathTableModel
 *
 * @author mla
 * @version $Id$
 */
public class RelPathTable extends BaseTable {
    private PathMappingJpaController pathJpa = new PathMappingJpaController();

    private MappedTableModel<PathMapping> model;

    /**
     * all bookmark entries.
     */
    MJObservableList relpathList = new MJObservableList();

    public RelPathTable() {

        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
        init();
    }

    private void init() {
        relpathList.addAll(pathJpa.findPathMappingEntities());


        model = MappedTableModel.configure(this, relpathList, PathMapping.class,
                col("substitution", "Substitution", true),
                col("pathPrefix", "Path Prefix", true)
        );

        model.setChangeAction(new ChangeAction<PathMapping>() {
            @Override

            public boolean onChangeNode(PathMapping bm, Object oldVal, Object value, ColDescr<PathMapping> col) {
                try {
                    pathJpa.edit(bm);
                    // fire change event on list:
                    MainApplicationController.getInstance().getBookmarkList().fireListElementChanged(bm);
                } catch (Exception ex) {
                    LoggerFactory.getLogger(PathMapping.class.getName()).error("error", ex); //NOI18N
                    throw new RuntimeException(ex);
                }
                return true;
            }
        });
    }

    public MappedTableModel<PathMapping> getRelPathModel() {
        return model;
    }

    public void addNewMapping(PathMapping newMapping) {
        relpathList.add(newMapping);
    }

    public void removeMapping(PathMapping bm) {
        relpathList.remove(bm);
    }
}
