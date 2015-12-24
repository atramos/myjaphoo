// Generated from org\myjaphoo\model\grammars\FilterLanguage.g4 by ANTLR 4.3
package org.myjaphoo.model.grammars;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FilterLanguageParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FilterLanguageVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(@NotNull FilterLanguageParser.ArgListContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#substIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstIdentifier(@NotNull FilterLanguageParser.SubstIdentifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#groupExprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupExprList(@NotNull FilterLanguageParser.GroupExprListContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(@NotNull FilterLanguageParser.ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#simpleSearchLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSearchLiteral(@NotNull FilterLanguageParser.SimpleSearchLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifier(@NotNull FilterLanguageParser.QualifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#groupTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupTerm(@NotNull FilterLanguageParser.GroupTermContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#simpleArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleArg(@NotNull FilterLanguageParser.SimpleArgContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(@NotNull FilterLanguageParser.HavingClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#groupExprPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupExprPart(@NotNull FilterLanguageParser.GroupExprPartContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#orLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrLiteral(@NotNull FilterLanguageParser.OrLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#elseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStmt(@NotNull FilterLanguageParser.ElseStmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#identWithSimpleRegExSearchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentWithSimpleRegExSearchStatement(@NotNull FilterLanguageParser.IdentWithSimpleRegExSearchStatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#brackedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBrackedExpr(@NotNull FilterLanguageParser.BrackedExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#thenStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThenStmt(@NotNull FilterLanguageParser.ThenStmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(@NotNull FilterLanguageParser.IfStmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#sizePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSizePrefix(@NotNull FilterLanguageParser.SizePrefixContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#negatedBoolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegatedBoolExpr(@NotNull FilterLanguageParser.NegatedBoolExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#brackedBoolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBrackedBoolExpr(@NotNull FilterLanguageParser.BrackedBoolExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#groupInstruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInstruction(@NotNull FilterLanguageParser.GroupInstructionContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull FilterLanguageParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#andLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndLiteral(@NotNull FilterLanguageParser.AndLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#elseIfStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStmt(@NotNull FilterLanguageParser.ElseIfStmtContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#boolexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolexpr(@NotNull FilterLanguageParser.BoolexprContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(@NotNull FilterLanguageParser.GroupContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(@NotNull FilterLanguageParser.FuncCallContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(@NotNull FilterLanguageParser.FilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull FilterLanguageParser.LiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link FilterLanguageParser#groupExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupExpr(@NotNull FilterLanguageParser.GroupExprContext ctx);
}