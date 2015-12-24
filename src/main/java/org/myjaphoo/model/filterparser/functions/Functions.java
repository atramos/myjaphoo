/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.mlsoft.common.StringUtilities;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author mla
 */
public class Functions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/filterparser/functions/resources/Functions");
    private static final String ISPIC_DESCR_PARAM = localeBundle.getString("ISPIC_DESCR");
    private static final String ISMOV_DESCR_PARAM = localeBundle.getString("ISMOV_DESCR");
    private static final String ISTXT_DESCR_PARAM = localeBundle.getString("ISTXT_DESCR");
    private static final String CONCAT_DESCR_PARAM = localeBundle.getString("CONCAT_DESCR");
    private static final String ENTRYATTR_DESCR_PARAM = localeBundle.getString("ENTRYATTR_DESCR");
    private static final String TAGATTR_DESCR_PARAM = localeBundle.getString("TAGATTR_DESCR");
    private static final String METATAGATTR_DESCR_PARAM = localeBundle.getString("TAGATTR_DESCR");
    private static final String RETAINLETTERS_DESCR_PARAM = localeBundle.getString("RETAINLETTERS_DESCR");
    private static final String RETAINDIGITS_DESCR_PARAM = localeBundle.getString("RETAINDIGITS_DESCR");
    private static final String REMOVEDIGITS_DESCR_PARAM = localeBundle.getString("REMOVEDIGITS_DESCR");
    private static final String REMOVELETTERS_DESCR_PARAM = localeBundle.getString("REMOVELETTERS_DESCR");
    private static final String SEQ_DESCR_PARAM = localeBundle.getString("SEQ_DESCR");
    private static final String REPLACEALL_DESCR_PARAM = localeBundle.getString("REPLACEALL_DESCR");
    private static final String REGEXEXTRACT_DESCR_PARAM = localeBundle.getString("REGEXEXTRACT_DESCR");
    private static final String LEN_DESCR_PARAM = localeBundle.getString("LEN_DESCR");
    private static final String TONUM_DESCR_PARAM = localeBundle.getString("TONUM_DESCR");


    private static final String SUM_DESCR_PARAM = localeBundle.getString("SUM_DESCR");
    private static final String MAX_DESCR_PARAM = localeBundle.getString("MAX_DESCR");
    private static final String MIN_DESCR_PARAM = localeBundle.getString("MIN_DESCR");
    private static final String COUNT_DESCR_PARAM = localeBundle.getString("COUNT_DESCR");


    private static final String SPLIT_DESCR_PARAM = localeBundle.getString("SPLIT_DESCR");
    public static final Function SUBST = new SubstitutionFunction();

    public static final Function ISPIC = new BoolFunction("ispic", MessageFormat.format(ISPIC_DESCR_PARAM, FileType.Pictures.getFileFilter()))
    { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            return BoolValue.forVal(FileType.Pictures.is(row.getEntry()));
        }
    };
    public static final Function ISMOV = new BoolFunction("ismov", MessageFormat.format(ISMOV_DESCR_PARAM, FileType.Movies.getFileFilter()))
    { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            return BoolValue.forVal(FileType.Movies.is(row.getEntry()));
        }
    };
    public static final Function ISTXT = new BoolFunction("istxt", MessageFormat.format(ISTXT_DESCR_PARAM, FileType.Text.getFileFilter()))
    { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            return BoolValue.forVal(FileType.Text.is(row.getEntry()));
        }
    };
    public static final Function CONCAT = new StringFunction("concat", CONCAT_DESCR_PARAM, ExprType.TEXT, ExprType.TEXT) { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            Expression arg1 = args.get(1);
            String s1 = arg0.evaluate(context, row).asString();
            String s2 = arg1.evaluate(context, row).asString();
            return new StringValue(s1 + s2);
        }
    };
    public static final Function RETAINLETTERS = new StringToStringFunction(
            "retainletters", //NOI18N
            RETAINLETTERS_DESCR_PARAM) {

        @Override
        protected String stringToStringFunction(ExecutionContext context, String s) {
            return StringUtilities.retainLetters(s);
        }
    };
    public static final Function RETAINDIGITS = new StringToStringFunction(
            "retaindigits", //NOI18N
            RETAINDIGITS_DESCR_PARAM) {

        @Override
        protected String stringToStringFunction(ExecutionContext context, String s) {
            return StringUtilities.retainDigits(s);
        }
    };
    public static final Function REMOVEDIGITS = new StringToStringFunction("removedigits", REMOVEDIGITS_DESCR_PARAM) {

        @Override
        protected String stringToStringFunction(ExecutionContext context, String s) {
            return StringUtilities.removeDigits(s);
        }
    };
    public static final Function REMOVELETTERS = new StringToStringFunction("removeletters", REMOVELETTERS_DESCR_PARAM) {

        @Override
        protected String stringToStringFunction(ExecutionContext context, String s) {
            return StringUtilities.removeLetters(s);
        }
    };
    public static final Function SEQ = new StringToStringFunction("seq", SEQ_DESCR_PARAM) {

        @Override
        protected String stringToStringFunction(ExecutionContext context, String s) {
            return context.getSequenceNumber(s);
        }
    };
    public static final Function REPLACEALL = new StringFunction("replaceall", REPLACEALL_DESCR_PARAM, ExprType.TEXT, ExprType.TEXT, ExprType.TEXT) {

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            Expression arg1 = args.get(1);
            Expression arg2 = args.get(2);
            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                s1 = "";
            }
            String s2 = arg1.evaluate(context, row).asString();
            String s3 = arg2.evaluate(context, row).asString();
            return new StringValue(s1.replaceAll(s2, s3));
        }
    };
    public static final Function REGEXEXTRACT = new StringFunction("regexextract", REGEXEXTRACT_DESCR_PARAM, ExprType.TEXT, ExprType.TEXT) {

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            Expression arg1 = args.get(1);
            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                s1 = "";
            }
            Pattern pattern = ((StringValue) arg1.evaluate(context, row)).getRegExPattern();
            Matcher matcher = pattern.matcher(s1);

            if (matcher.find()) {
                return new StringValue(s1.substring(matcher.start(), matcher.end()));
            } else {
                return new StringValue("NOMATCH");
            }
        }
    };

    public static final Function SPLIT = new StringFunction("split", SPLIT_DESCR_PARAM, ExprType.TEXT, ExprType.TEXT) {

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            Expression arg1 = args.get(1);
            String s0 = arg0.evaluate(context, row).asString();
            if (s0 == null) {
                s0 = "";
            }
            String s1 = arg1.evaluate(context, row).asString();
            if (s1 == null) {
                s1 = "";
            }
            String[] splits = s0.split(s1);
            return ValueSet.createStringSet(splits);
        }
    };
    public static final Function STRLEN = new NumberFunction("strlen", LEN_DESCR_PARAM, ExprType.TEXT) {

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);

            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                return new LongValue(0L);
            } else {
                return new LongValue(s1.length());
            }

        }
    };

    public static final Function TONUM = new NumberFunction("tonum", TONUM_DESCR_PARAM, ExprType.TEXT) {

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);

            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                return new LongValue(0L);
            } else {
                // try to convert into a number:
                try {
                    // we are a bit lenient: we parse as double, but return only a long:
                    double d = Double.parseDouble(s1);
                    // we use only the long part of a double (we have only long types at the moment...)
                    long l = (long) d;
                    return new LongValue(l);
                } catch (NumberFormatException nfe) {
                    return new LongValue(0L);
                }
            }
        }
    };

    public static final Function ENTRYATTR = new StringFunction("entryattr", ENTRYATTR_DESCR_PARAM, ExprType.TEXT) { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                return new StringValue(null);
            }
            return new StringValue(row.getEntry().getAttributes().get(s1));
        }
    };

    public static final Function TAGATTR = new StringFunction("tagattr", TAGATTR_DESCR_PARAM, ExprType.TEXT) { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                return new StringValue(null);
            }
            return new StringValue(row.getToken().getAttributes().get(s1));
        }

        @Override
        public boolean needsTagRelation() {
            return true;
        }
    };

    public static final Function METATAGATTR = new StringFunction("metatagattr", METATAGATTR_DESCR_PARAM, ExprType.TEXT) { //NOI18N

        @Override
        public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
            Expression arg0 = args.get(0);
            String s1 = arg0.evaluate(context, row).asString();
            if (s1 == null) {
                return new StringValue(null);
            }
            return new StringValue(row.getMetaToken().getAttributes().get(s1));
        }

        @Override
        public boolean needsMetaTagRelation() {
            return true;
        }
    };


    public static final Function SUM = new NumberAggregateFunction("sum", SUM_DESCR_PARAM) {

        @Override
        protected long aggregateVal(long val, long additionalVal) {
            return val + additionalVal;
        }

        @Override
        protected long getBaseValue() {
            return 0;
        }
    };

    public static final Function MAX = new NumberAggregateFunction("max", MAX_DESCR_PARAM) {

        @Override
        protected long aggregateVal(long val, long additionalVal) {
            return Math.max(val, additionalVal);
        }

        @Override
        protected long getBaseValue() {
            return Long.MIN_VALUE;
        }
    };

    public static final Function MIN = new NumberAggregateFunction("min", MIN_DESCR_PARAM) {

        @Override
        protected long aggregateVal(long val, long additionalVal) {
            return Math.min(val, additionalVal);
        }

        @Override
        protected long getBaseValue() {
            return Long.MAX_VALUE;
        }
    };

    public static final Function COUNT = new NumberAggregateFunction("count", COUNT_DESCR_PARAM) {

        @Override
        protected long aggregateVal(long val, long additionalVal) {
            return val + 1;
        }

        @Override
        protected long getBaseValue() {
            return 0;
        }
    };
}
