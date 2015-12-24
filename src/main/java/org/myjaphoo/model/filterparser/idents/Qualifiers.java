package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * All defined qualifiers.
 */
public class Qualifiers {


    public static final Qualifier LEFT = new Qualifier<JoinedDataRow, JoinedDataRow>("left", "left side of comparison", "left.path like blubb", ExprType.NULL, JoinedDataRow.class, JoinedDataRow.class, false, false) {

        @Override
        public JoinedDataRow extractQualifierContext(JoinedDataRow row) {
            return row;
        }
    };

    public static final Qualifier RIGHT = new Qualifier<JoinedDataRow, JoinedDataRow>("right", "right side of comparison", "right.path like blubb", ExprType.NULL, JoinedDataRow.class, JoinedDataRow.class, false, false) {
        @Override
        public JoinedDataRow extractQualifierContext(JoinedDataRow row) {
            // this does only work for diff results;
            // what we do here is, we just return the right side of the comparison result:
            if (row instanceof DBDiffCombinationResult) {
            DBDiffCombinationResult diff = (DBDiffCombinationResult) row;
            return diff.getT2();
            } else {
                // defaults to "right":
                return row;
            }
        }
    };
}
