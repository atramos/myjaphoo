// Generated from org\myjaphoo\model\grammars\FilterLanguage.g4 by ANTLR 4.3
package org.myjaphoo.model.grammars;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FilterLanguageParser}.
 */
public interface FilterLanguageListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(@NotNull FilterLanguageParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(@NotNull FilterLanguageParser.ArgListContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#substIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSubstIdentifier(@NotNull FilterLanguageParser.SubstIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#substIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSubstIdentifier(@NotNull FilterLanguageParser.SubstIdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#groupExprList}.
	 * @param ctx the parse tree
	 */
	void enterGroupExprList(@NotNull FilterLanguageParser.GroupExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#groupExprList}.
	 * @param ctx the parse tree
	 */
	void exitGroupExprList(@NotNull FilterLanguageParser.GroupExprListContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(@NotNull FilterLanguageParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(@NotNull FilterLanguageParser.ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#simpleSearchLiteral}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSearchLiteral(@NotNull FilterLanguageParser.SimpleSearchLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#simpleSearchLiteral}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSearchLiteral(@NotNull FilterLanguageParser.SimpleSearchLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#qualifier}.
	 * @param ctx the parse tree
	 */
	void enterQualifier(@NotNull FilterLanguageParser.QualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#qualifier}.
	 * @param ctx the parse tree
	 */
	void exitQualifier(@NotNull FilterLanguageParser.QualifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#groupTerm}.
	 * @param ctx the parse tree
	 */
	void enterGroupTerm(@NotNull FilterLanguageParser.GroupTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#groupTerm}.
	 * @param ctx the parse tree
	 */
	void exitGroupTerm(@NotNull FilterLanguageParser.GroupTermContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#simpleArg}.
	 * @param ctx the parse tree
	 */
	void enterSimpleArg(@NotNull FilterLanguageParser.SimpleArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#simpleArg}.
	 * @param ctx the parse tree
	 */
	void exitSimpleArg(@NotNull FilterLanguageParser.SimpleArgContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(@NotNull FilterLanguageParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(@NotNull FilterLanguageParser.HavingClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#groupExprPart}.
	 * @param ctx the parse tree
	 */
	void enterGroupExprPart(@NotNull FilterLanguageParser.GroupExprPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#groupExprPart}.
	 * @param ctx the parse tree
	 */
	void exitGroupExprPart(@NotNull FilterLanguageParser.GroupExprPartContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#orLiteral}.
	 * @param ctx the parse tree
	 */
	void enterOrLiteral(@NotNull FilterLanguageParser.OrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#orLiteral}.
	 * @param ctx the parse tree
	 */
	void exitOrLiteral(@NotNull FilterLanguageParser.OrLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseStmt(@NotNull FilterLanguageParser.ElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseStmt(@NotNull FilterLanguageParser.ElseStmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#identWithSimpleRegExSearchStatement}.
	 * @param ctx the parse tree
	 */
	void enterIdentWithSimpleRegExSearchStatement(@NotNull FilterLanguageParser.IdentWithSimpleRegExSearchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#identWithSimpleRegExSearchStatement}.
	 * @param ctx the parse tree
	 */
	void exitIdentWithSimpleRegExSearchStatement(@NotNull FilterLanguageParser.IdentWithSimpleRegExSearchStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#brackedExpr}.
	 * @param ctx the parse tree
	 */
	void enterBrackedExpr(@NotNull FilterLanguageParser.BrackedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#brackedExpr}.
	 * @param ctx the parse tree
	 */
	void exitBrackedExpr(@NotNull FilterLanguageParser.BrackedExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#thenStmt}.
	 * @param ctx the parse tree
	 */
	void enterThenStmt(@NotNull FilterLanguageParser.ThenStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#thenStmt}.
	 * @param ctx the parse tree
	 */
	void exitThenStmt(@NotNull FilterLanguageParser.ThenStmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(@NotNull FilterLanguageParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(@NotNull FilterLanguageParser.IfStmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#sizePrefix}.
	 * @param ctx the parse tree
	 */
	void enterSizePrefix(@NotNull FilterLanguageParser.SizePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#sizePrefix}.
	 * @param ctx the parse tree
	 */
	void exitSizePrefix(@NotNull FilterLanguageParser.SizePrefixContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#negatedBoolExpr}.
	 * @param ctx the parse tree
	 */
	void enterNegatedBoolExpr(@NotNull FilterLanguageParser.NegatedBoolExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#negatedBoolExpr}.
	 * @param ctx the parse tree
	 */
	void exitNegatedBoolExpr(@NotNull FilterLanguageParser.NegatedBoolExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#brackedBoolExpr}.
	 * @param ctx the parse tree
	 */
	void enterBrackedBoolExpr(@NotNull FilterLanguageParser.BrackedBoolExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#brackedBoolExpr}.
	 * @param ctx the parse tree
	 */
	void exitBrackedBoolExpr(@NotNull FilterLanguageParser.BrackedBoolExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#groupInstruction}.
	 * @param ctx the parse tree
	 */
	void enterGroupInstruction(@NotNull FilterLanguageParser.GroupInstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#groupInstruction}.
	 * @param ctx the parse tree
	 */
	void exitGroupInstruction(@NotNull FilterLanguageParser.GroupInstructionContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull FilterLanguageParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull FilterLanguageParser.NumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#andLiteral}.
	 * @param ctx the parse tree
	 */
	void enterAndLiteral(@NotNull FilterLanguageParser.AndLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#andLiteral}.
	 * @param ctx the parse tree
	 */
	void exitAndLiteral(@NotNull FilterLanguageParser.AndLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#elseIfStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStmt(@NotNull FilterLanguageParser.ElseIfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#elseIfStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStmt(@NotNull FilterLanguageParser.ElseIfStmtContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#boolexpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolexpr(@NotNull FilterLanguageParser.BoolexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#boolexpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolexpr(@NotNull FilterLanguageParser.BoolexprContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#group}.
	 * @param ctx the parse tree
	 */
	void enterGroup(@NotNull FilterLanguageParser.GroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#group}.
	 * @param ctx the parse tree
	 */
	void exitGroup(@NotNull FilterLanguageParser.GroupContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(@NotNull FilterLanguageParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(@NotNull FilterLanguageParser.FuncCallContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(@NotNull FilterLanguageParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(@NotNull FilterLanguageParser.FilterContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull FilterLanguageParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull FilterLanguageParser.LiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link FilterLanguageParser#groupExpr}.
	 * @param ctx the parse tree
	 */
	void enterGroupExpr(@NotNull FilterLanguageParser.GroupExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterLanguageParser#groupExpr}.
	 * @param ctx the parse tree
	 */
	void exitGroupExpr(@NotNull FilterLanguageParser.GroupExprContext ctx);
}