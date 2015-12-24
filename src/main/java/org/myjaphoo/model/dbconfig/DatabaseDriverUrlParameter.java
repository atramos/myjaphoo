/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


/**
 * All supported parameter that might occur in a connection parameter url string.
 * The DatabaseDriver class defines by its connection parameter patterns which
 * concrete paramter are supported.
 * The parameters itself have functions to find out, if a url patterm supports
 * that paraemter and methods to set the parameter in the pattern.
 * This is all simply defined by regular expression matching.
 * A parameter has usually the format "<" parametername ">".
 * A parameter can contain a default value in in the url pattern description.
 * This should then have the form: "<" parametername "(" default value ")"">".
 * @author mla
 */
public enum DatabaseDriverUrlParameter {

    SERVER("Server", "<server>", new PropExtractor() {

@Override
public String getValue(DatabaseConfiguration dc) {
    return dc.getServer();
}
}),
    PORT("Port", "<port\\([0-9]*\\)>", new PropExtractor() {

@Override
public String getValue(DatabaseConfiguration dc) {
    if (dc.getPort() != null) {
        return Integer.toString(dc.getPort());
    } else {
        return null;
    }
}
}),
    FILENAME("Filename", "<fileName>", new PropExtractor() {

@Override
public String getValue(DatabaseConfiguration dc) {
    return dc.getFilename();
}
}),
    DATABASENAME("Databasename", "<databaseName>", new PropExtractor() {

@Override
public String getValue(DatabaseConfiguration dc) {
    return dc.getDatabasename();
}
}),
    SID("id", "<sid>", new PropExtractor() {

@Override
public String getValue(DatabaseConfiguration dc) {
    return dc.getSid();
}
}),
    CREATEDB("Create DB", "<createDB>", new PropExtractor() {

@Override
public String getValue(DatabaseConfiguration dc) {
    return Boolean.toString(dc.isCreateDb());
}
});

    interface PropExtractor {

        String getValue(DatabaseConfiguration dc);
    };
    private String name;
    private String regExpSearchPattern;
    private static final String defParamPattern = "\\(.*\\)";
    private PropExtractor extractor;

    private DatabaseDriverUrlParameter(String name, String regExpSearchPattern, PropExtractor extractor) {
        this.name = name;
        this.regExpSearchPattern = regExpSearchPattern;
        this.extractor = extractor;
    }

    public boolean hasParameter(String urlConnectionPattern) {
        Pattern pattern = Pattern.compile(regExpSearchPattern);
        Matcher matcher = pattern.matcher(urlConnectionPattern);
        return matcher.find();
    }

    public String extractDefaultParameterFromPattern(String urlConnectionPattern) {
        Pattern pattern = Pattern.compile(regExpSearchPattern);
        Matcher matcher = pattern.matcher(urlConnectionPattern);

        if (matcher.find()) {
            String extractedParamDescr = urlConnectionPattern.substring(matcher.start(), matcher.end());
            // now extract the default parameter if exists:
            Pattern defValuePattern = Pattern.compile(defParamPattern);
            Matcher matchDefaultValue = defValuePattern.matcher(extractedParamDescr);
            if (matchDefaultValue.find()) {
                String valueWithCommas = extractedParamDescr.substring(matchDefaultValue.start(), matchDefaultValue.end());
                return valueWithCommas.substring(1, valueWithCommas.length() - 1);
            }
        }
        // no default value found:
        return "";
    }

    public String replaceParameter(String urlConnectionPattern, String replacement) {
        if (StringUtils.isEmpty(replacement)) {
            // check, if there is a default value for this parameter:
            replacement = extractDefaultParameterFromPattern(urlConnectionPattern);
        }
        if (!StringUtils.isEmpty(replacement)) {
            Pattern pattern = Pattern.compile(regExpSearchPattern);
            Matcher matcher = pattern.matcher(urlConnectionPattern);
            // escape backslashes; this works with the java escaper, unfortunately not with Pattern.quote
            String replAsLiteral = StringEscapeUtils.escapeJava(replacement);
            return matcher.replaceAll(replAsLiteral);
        } else {
            return urlConnectionPattern;
        }
    }

    public String replaceParameter(String urlTemplate, DatabaseConfiguration dc) {
        return replaceParameter(urlTemplate, extractor.getValue(dc));
    }
}
