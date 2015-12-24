/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.operator;

import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.values.LongValue;
import org.myjaphoo.model.filterparser.values.StringValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.grammars.FilterLanguageParser;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.myjaphoo.model.filterparser.expr.ExprType.*;
import static org.myjaphoo.model.util.ComparatorUtils.compareTo;
import static org.myjaphoo.model.util.ComparatorUtils.equalsTo;

/**
 * @author mla
 */
public class Operators {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/filterparser/operator/resources/Operators");
    private static final String EQUAL_DESCR_PARAM = localeBundle.getString("EQUAL_DESCR");
    private static final String NOTEQUAL_DESCR_PARAM = localeBundle.getString("NOTEQUAL_DESCR");
    private static final String LIKE_DESCR_PARAM = localeBundle.getString("LIKE_DESCR");
    private static final String STARTWITH_DESCR_PARAM = localeBundle.getString("STARTWITH_DESCR");
    private static final String ENDSWITH_DESCR_PARAM = localeBundle.getString("ENDSWITH_DESCR");
    private static final String SMALLERTHAN_DESCR_PARAM = localeBundle.getString("SMALLERTHAN_DESCR");
    private static final String SMALLEROREQUALTHAN_DESCR_PARAM = localeBundle.getString("SMALLEROREQUALTHAN_DESCR");

    private static final String BIGGERTHAN_DESCR_PARAM = localeBundle.getString("BIGGERTHAN_DESCR");
    private static final String BIGGEROREQUALTHAN_DESCR_PARAM = localeBundle.getString("BIGGEROREQUALTHAN_DESCR");

    private static final String LOGICALAND_DESCR_PARAM = localeBundle.getString("LOGICALAND_DESCR");
    private static final String LOGICALOR_DESCR_PARAM = localeBundle.getString("LOGICALOR_DESCR");
    private static final String LOGICALNOT_DESCR_PARAM = localeBundle.getString("LOGICALNOT_DESCR");
    private static final String REGEX_DESCR_PARAM = localeBundle.getString("REGEX_DESCR");
    private static final String REGEXFIND_DESCR_PARAM = localeBundle.getString("REGEXFIND_DESCR");
    public static final AbstractOperator EQUAL = new DefaultBooleanOperator(
            FilterLanguageParser.EQUAL,
            '=',
            EQUAL_DESCR_PARAM,
            NUMBER, TEXT, DATE, NULL) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return StringUtils.equals(txt1, stringLiteral.asString());
        }

        @Override
        public boolean eval(long i1, long i2) {
            return i1 == i2;
        }

        @Override
        public boolean eval(Date d1, Date d2) {
            return equalsTo(d1, d2);
        }
    };
    public static final AbstractOperator NOTEQUAL = new DefaultBooleanOperator(
            FilterLanguageParser.UNEQUAL,
            "<>", //NOI18N
            NOTEQUAL_DESCR_PARAM,
            NUMBER, TEXT, DATE) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return !StringUtils.equals(txt1, stringLiteral.asString());
        }

        @Override
        public boolean eval(long i1, long i2) {
            return i1 != i2;
        }

        @Override
        public boolean eval(Date d1, Date d2) {
            return !equalsTo(d1, d2);
        }
    };
    public static final AbstractOperator IS = new SynonymOp(FilterLanguageParser.IS, "is", EQUAL); //NOI18N
    public static final AbstractOperator LIKE = new DefaultBooleanOperator(
            FilterLanguageParser.LIKE,
            "like", //NOI18N
            LIKE_DESCR_PARAM,
            TEXT) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return StringUtils.containsIgnoreCase(txt1, stringLiteral.asString());
        }
    };
    public static final AbstractOperator WIE = new SynonymOp(FilterLanguageParser.WIE, "wie", LIKE); //NOI18N
    public static final AbstractOperator startswith = new DefaultBooleanOperator(
            FilterLanguageParser.STARTSWITH,
            "startswith", //NOI18N
            STARTWITH_DESCR_PARAM,
            TEXT) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            if (txt1 == null || stringLiteral.asString() == null) {
                return false;
            }
            return txt1.startsWith(stringLiteral.asString());
        }
    };
    public static final AbstractOperator endswith = new DefaultBooleanOperator(
            FilterLanguageParser.ENDSWITH,
            "endswith", //NOI18N
            ENDSWITH_DESCR_PARAM,
            TEXT) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            if (txt1 == null || stringLiteral.asString() == null) {
                return false;
            }
            return txt1.endsWith(stringLiteral.asString());
        }
    };
    public static final AbstractOperator LOWER = new DefaultBooleanOperator(
            FilterLanguageParser.LOWER,
            '<',
            SMALLERTHAN_DESCR_PARAM,
            NUMBER, TEXT, DATE) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return compareTo(txt1, stringLiteral.asString()) < 0;
        }

        @Override
        public boolean eval(long i1, long i2) {
            return i1 < i2;
        }

        @Override
        public boolean eval(Date d1, Date d2) {
            return compareTo(d1, d2) < 0;
        }
    };

    public static final AbstractOperator LOWEROREQUAL = new DefaultBooleanOperator(
            FilterLanguageParser.LOWEREQUALTHEN,
            "<=",
            SMALLEROREQUALTHAN_DESCR_PARAM,
            NUMBER, TEXT, DATE) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return compareTo(txt1, stringLiteral.asString()) <= 0;
        }

        @Override
        public boolean eval(long i1, long i2) {
            return i1 <= i2;
        }

        @Override
        public boolean eval(Date d1, Date d2) {
            return compareTo(d1, d2) <= 0;
        }
    };

    public static final AbstractOperator HIGHER = new DefaultBooleanOperator(
            FilterLanguageParser.HIGHER,
            '>',
            BIGGERTHAN_DESCR_PARAM,
            NUMBER, TEXT, DATE) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return compareTo(txt1, stringLiteral.asString()) > 0;
        }

        @Override
        public boolean eval(long i1, long i2) {
            return i1 > i2;
        }

        @Override
        public boolean eval(Date d1, Date d2) {
            return compareTo(d1, d2) > 0;
        }
    };

    public static final AbstractOperator HIGHEROREQUAL = new DefaultBooleanOperator(
            FilterLanguageParser.HIGHEREQUALTHEN,
            ">=",
            BIGGEROREQUALTHAN_DESCR_PARAM,
            NUMBER, TEXT, DATE) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            return compareTo(txt1, stringLiteral.asString()) >= 0;
        }

        @Override
        public boolean eval(long i1, long i2) {
            return i1 >= i2;
        }

        @Override
        public boolean eval(Date d1, Date d2) {
            return compareTo(d1, d2) >= 0;
        }
    };

    public static final AbstractOperator AND = new DefaultBooleanOperator(
            FilterLanguageParser.AND,
            "and", //NOI18N
            LOGICALAND_DESCR_PARAM,
            BOOLEAN) {

        @Override
        public boolean eval(boolean b1, boolean b2) {
            return b1 && b2;
        }
    };
    public static final AbstractOperator OR = new DefaultBooleanOperator(
            FilterLanguageParser.OR,
            "or", //NOI18N
            LOGICALOR_DESCR_PARAM,
            BOOLEAN) {

        @Override
        public boolean eval(boolean b1, boolean b2) {
            return b1 || b2;
        }
    };
    /**
     * "pseudo" operator not: dient nur zum parsen; funkionen sind nicht überladen.
     */
    public static final AbstractOperator NOT = new DefaultBooleanOperator(
            FilterLanguageParser.NOT,
            "not", //NOI18N
            LOGICALNOT_DESCR_PARAM,
            BOOLEAN) {
    };
    public static final AbstractOperator REGEX = new DefaultBooleanOperator(
            FilterLanguageParser.REGEX,
            "regex", //NOI18N
            REGEX_DESCR_PARAM,
            TEXT) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            Pattern pattern = stringLiteral.getRegExPattern();
            // Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
            if (txt1 == null) {
                txt1 = "";
            }
            Matcher matcher = pattern.matcher(txt1);
            return matcher.matches();

        }
    };

    public static final AbstractOperator REGEX_SYM_OP = new SynonymOp(FilterLanguageParser.REGEX2, "~", REGEX); //NOI18N


    public static final AbstractOperator REGEXFIND = new DefaultBooleanOperator(
            FilterLanguageParser.REGEXFIND,
            "regexfind", //NOI18N
            REGEXFIND_DESCR_PARAM,
            TEXT) {

        @Override
        public boolean eval(String txt1, StringValue stringLiteral) {
            Pattern pattern = stringLiteral.getRegExPattern();
            // Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
            if (txt1 == null) {
                txt1 = "";
            }
            Matcher matcher = pattern.matcher(txt1);
            return matcher.find();

        }
    };
    public static final AbstractOperator PLUS = new TermOperator(FilterLanguageParser.ADD, '+', "plus", ExprType.TEXT, ExprType.NUMBER) {

        @Override
        public Value eval(Value v1, Value v2) {
            if (ExprType.TEXT.isCompatible(v1.getType()) && ExprType.TEXT.isCompatible(v2.getType())) {
                return new StringValue(v1.asString() + v2.asString());
            }
            if (ExprType.NUMBER.isCompatible(v1.getType()) && ExprType.NUMBER.isCompatible(v2.getType())) {
                return new LongValue((v1.asLong() + v2.asLong()));
            }
            // this should never happen at all....
            throw new RuntimeException("incompatible types for plus!");
        }

        @Override
        public ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType) {
            if (ExprType.TEXT.isCompatible(rightExpressionType) && ExprType.TEXT.isCompatible(leftExpressionType)) {
                return ExprType.TEXT;
            }
            if (ExprType.NUMBER.isCompatible(rightExpressionType) && ExprType.NUMBER.isCompatible(leftExpressionType)) {
                return ExprType.NUMBER;
            }
            // this should never happen at all....
            throw new RuntimeException("incompatible types for plus!");
        }
    };

    public static final AbstractOperator MINUS = new TermOperator(FilterLanguageParser.SUB, '-', "minus", ExprType.NUMBER) {

        @Override
        public Value eval(Value v1, Value v2) {
                return new LongValue((v1.asLong() - v2.asLong()));
        }

        @Override
        public ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType) {
            if (ExprType.NUMBER.isCompatible(rightExpressionType) && ExprType.NUMBER.isCompatible(leftExpressionType)) {
                return ExprType.NUMBER;
            }
            // this should never happen at all....
            throw new RuntimeException("incompatible types for plus!");
        }
    };
    public static final AbstractOperator MULT = new TermOperator(FilterLanguageParser.MUL, '*', "mult", ExprType.NUMBER) {

        @Override
        public Value eval(Value v1, Value v2) {
            return new LongValue((v1.asLong() * v2.asLong()));
        }

        @Override
        public ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType) {
            if (ExprType.NUMBER.isCompatible(rightExpressionType) && ExprType.NUMBER.isCompatible(leftExpressionType)) {
                return ExprType.NUMBER;
            }
            // this should never happen at all....
            throw new RuntimeException("incompatible types for plus!");
        }
    };
    public static final AbstractOperator DIV = new TermOperator(FilterLanguageParser.DIV, '/', "div", ExprType.NUMBER) {

        @Override
        public Value eval(Value v1, Value v2) {
            return new LongValue((v1.asLong() / v2.asLong()));
        }

        @Override
        public ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType) {
            if (ExprType.NUMBER.isCompatible(rightExpressionType) && ExprType.NUMBER.isCompatible(leftExpressionType)) {
                return ExprType.NUMBER;
            }
            // this should never happen at all....
            throw new RuntimeException("incompatible types for plus!");
        }
    };
}
