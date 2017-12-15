/*
 *  Copyright 2017 An Tran and Adrian Todt
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package xyz.avarel.kaiper.parser.parslets.variables

import xyz.avarel.kaiper.Precedence
import xyz.avarel.kaiper.ast.expr.Expr
import xyz.avarel.kaiper.ast.expr.collections.GetOperation
import xyz.avarel.kaiper.ast.expr.collections.SetOperation
import xyz.avarel.kaiper.ast.expr.variables.AssignmentExpr
import xyz.avarel.kaiper.ast.expr.variables.Identifier
import xyz.avarel.kaiper.exceptions.SyntaxException
import xyz.avarel.kaiper.lexer.Token
import xyz.avarel.kaiper.parser.BinaryParser
import xyz.avarel.kaiper.parser.ExprParser

class AssignmentParser : BinaryParser(Precedence.ASSIGNMENT) {

    override fun parse(parser: ExprParser, left: Expr, token: Token): Expr {
        val value = parser.parseExpr()

        return when (left) {
            is Identifier -> AssignmentExpr(
                    token.position,
                    left,
                    value
            )
            is GetOperation -> SetOperation(
                    token.position,
                    left,
                    value
            )
            else -> throw SyntaxException("Invalid assignment target", left.position)
        }
    }
}