import org.myjaphoo.model.filterparser.FilterParser
import org.myjaphoo.model.filterparser.expr.ExprType
import org.myjaphoo.model.filterparser.functions.Function
import org.myjaphoo.model.filterparser.idents.Qualifier
import org.myjaphoo.model.filterparser.operator.AbstractOperator
import org.myjaphoo.model.filterparser.syntaxtree.Constant
import org.myjaphoo.model.filterparser.syntaxtree.SelfDescriptingElement
import org.myjaphoo.model.filterparser.syntaxtree.Unit

/**
 * Created with IntelliJ IDEA.
 * User: lang
 * Creates dokuwiki tables for the syntax elements which are able to self describe.
 */
public class CreateDokuWikiSyntaxTables {

    public static void main(String[] args) {

        Locale l = new Locale("en", "US");
        Locale.setDefault(l);
        FilterParser.initialize();

        def WikiPage idents = new WikiPage("src/main/dokuwiki/data/pages/manual/identifiers.txt")
        idents.withCloseable {
            idents.with {
                h1("Identifiers")
                text("There are several so called predefined identifiers available which select attributes from media files for filtering. ");

                header("Identifier", "Type", "Description", "Example");
                for (final Qualifier ident : ordered(Qualifier.getList())) {
                    if (!ident.getName().startsWith("exif")) {
                        row(ident.getName(), ident.getType().name(), ident.getSelfShortDescription(), ident.getExampleUsage());
                    }
                }
            }
        }

        def WikiPage exif = new WikiPage("src/main/dokuwiki/data/pages/manual/exifidentifiers.txt")
        exif.withCloseable {
            exif.with {
                h1("Exif Identifiers")

                header("Exif Identifier", "Type", "Description");
                for (final Qualifier ident : ordered(Qualifier.getList())) {
                    if (ident.getName().startsWith("exif")) {
                        row(ident.getName(), ident.getType().name(), ident.getSelfShortDescription());
                    }
                }
            }
        }

        def WikiPage ops = new WikiPage("src/main/dokuwiki/data/pages/manual/opterators.txt")
        ops.withCloseable {
            ops.with {
                h1("Operators")
                text("This table lists all available operators.")
                header("Operator", "Compatible Types", "Description");
                for (final AbstractOperator op : ordered(AbstractOperator.getList())) {

                    row(op.getName(), list(op.worksWithTypes()), op.getSelfShortDescription());
                }

//                for (final AbstractOperator op : ordered(AbstractOperator.getList())) {
//
//                    h2("Operator $op.name")
//                    text("The operator $op.name works with the types ${op.worksWithTypes()}")
//                    h3("Description")
//                    text(op.getSelfShortDescription())
//                }
            }
        }

        def WikiPage funcs = new WikiPage("src/main/dokuwiki/data/pages/manual/functions.txt")
        funcs.withCloseable {
            funcs.with {
                h1("Functions")
                header("Function", "Type", "Description");
                for (final Function function : ordered(Function.getList())) {
                    row(function.getName(), function.getType().name(), function.getSelfShortDescription());
                }
            }
        }

        def WikiPage consts = new WikiPage("src/main/dokuwiki/data/pages/manual/constants.txt")
        consts.withCloseable {
            consts.with {
                h1("Constants");
                header("Constant", "Type", "Description");
                for (final Constant constant : ordered(Constant.getAllConstants())) {
                    row(constant.getName(), constant.getType().name(), constant.getSelfShortDescription());
                }

                h1("Postfixes/Units");
                text("You could use the following postfixes when using numbers in expressions. They simply multiply the value by the given Unit value ")

                header("Unit Postfix", "Factor");
                for (final Unit unit : ordered(Unit.allUnits)) {
                    row(unit.getName(), unit.getSelfShortDescription());
                }
            }
        }


    }


    private static <T extends SelfDescriptingElement> List<T> ordered(Collection<T> collection) {
        return collection.sort { it.name };
    }

    private static String list(Set<ExprType> exprTypes) {
        String list = "";
        for (ExprType e : exprTypes) {
            if (list.length() > 0) {
                list += ",";
            }
            list += e.name();
        }
        return list;
    }

}
