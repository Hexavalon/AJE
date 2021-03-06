/*
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package xyz.avarel.kaiper.parser;

import xyz.avarel.kaiper.Precedence;
import xyz.avarel.kaiper.lexer.TokenType;
import xyz.avarel.kaiper.operations.BinaryOperatorType;
import xyz.avarel.kaiper.operations.UnaryOperatorType;
import xyz.avarel.kaiper.parser.parslets.*;
import xyz.avarel.kaiper.parser.parslets.flow.ForEachParser;
import xyz.avarel.kaiper.parser.parslets.flow.IfElseParser;
import xyz.avarel.kaiper.parser.parslets.flow.ReturnParser;
import xyz.avarel.kaiper.parser.parslets.functional.PipeBackwardParser;
import xyz.avarel.kaiper.parser.parslets.functional.PipeForwardParser;
import xyz.avarel.kaiper.parser.parslets.functions.FunctionParser;
import xyz.avarel.kaiper.parser.parslets.functions.InvocationParser;
import xyz.avarel.kaiper.parser.parslets.functions.LambdaFunctionParser;
import xyz.avarel.kaiper.parser.parslets.nodes.*;
import xyz.avarel.kaiper.parser.parslets.operators.BinaryOperatorParser;
import xyz.avarel.kaiper.parser.parslets.operators.RangeOperatorParser;
import xyz.avarel.kaiper.parser.parslets.operators.UnaryOperatorParser;
import xyz.avarel.kaiper.parser.parslets.variables.AttributeParser;
import xyz.avarel.kaiper.parser.parslets.variables.DeclarationParser;
import xyz.avarel.kaiper.parser.parslets.variables.NameParser;

public class DefaultGrammar extends Grammar {
    public static final Grammar INSTANCE = new DefaultGrammar();

    private DefaultGrammar() {
        // BLOCKS
        prefix(TokenType.LEFT_BRACKET, new CollectionsParser());
        prefix(TokenType.LEFT_PAREN, new GroupParser());

        // FLOW CONTROL
        prefix(TokenType.IF, new IfElseParser());
        prefix(TokenType.FOR, new ForEachParser());
        prefix(TokenType.RETURN, new ReturnParser());
        infix(TokenType.ELVIS, new ElvisParser());

        // NODES
        prefix(TokenType.INT, new IntParser());
        prefix(TokenType.NUMBER, new DecimalParser());
        prefix(TokenType.BOOLEAN, new BoolParser());
        prefix(TokenType.STRING, new StrParser());
        prefix(TokenType.NULL, new NullParser());


        prefix(TokenType.FUNCTION, new FunctionParser());
        prefix(TokenType.LEFT_BRACE, new LambdaFunctionParser());


        prefix(TokenType.IDENTIFIER, new NameParser());
        prefix(TokenType.LET, new DeclarationParser());

        prefix(TokenType.TYPE, new TypeParser());
        prefix(TokenType.MODULE, new ModuleParser());

        // Numeric
        prefix(TokenType.MINUS, new UnaryOperatorParser(UnaryOperatorType.MINUS));
        prefix(TokenType.PLUS, new UnaryOperatorParser(UnaryOperatorType.PLUS));
        infix(TokenType.PLUS, new BinaryOperatorParser(Precedence.ADDITIVE, true, BinaryOperatorType.PLUS));
        infix(TokenType.MINUS, new BinaryOperatorParser(Precedence.ADDITIVE, true, BinaryOperatorType.MINUS));
        infix(TokenType.ASTERISK, new BinaryOperatorParser(Precedence.MULTIPLICATIVE, true, BinaryOperatorType.TIMES));
        infix(TokenType.SLASH, new BinaryOperatorParser(Precedence.MULTIPLICATIVE, true, BinaryOperatorType.DIVIDE));
        infix(TokenType.PERCENT, new BinaryOperatorParser(Precedence.MULTIPLICATIVE, true, BinaryOperatorType.MODULUS));
        infix(TokenType.CARET, new BinaryOperatorParser(Precedence.EXPONENTIAL, false, BinaryOperatorType.POWER));
        infix(TokenType.SHIFT_RIGHT, new BinaryOperatorParser(Precedence.SHIFT, true, BinaryOperatorType.SHR));
        infix(TokenType.SHIFT_LEFT, new BinaryOperatorParser(Precedence.SHIFT, true, BinaryOperatorType.SHL));
        infix(TokenType.RANGE_TO, new RangeOperatorParser());

        // RELATIONAL
        infix(TokenType.EQUALS, new BinaryOperatorParser(Precedence.EQUALITY, true, BinaryOperatorType.EQUALS));
        infix(TokenType.NOT_EQUAL, new BinaryOperatorParser(Precedence.EQUALITY, true, BinaryOperatorType.NOT_EQUALS));
        infix(TokenType.GT, new BinaryOperatorParser(Precedence.COMPARISON, true, BinaryOperatorType.GREATER_THAN));
        infix(TokenType.LT, new BinaryOperatorParser(Precedence.COMPARISON, true, BinaryOperatorType.LESS_THAN));
        infix(TokenType.GTE, new BinaryOperatorParser(Precedence.COMPARISON, true, BinaryOperatorType.GREATER_THAN_EQUAL));
        infix(TokenType.LTE, new BinaryOperatorParser(Precedence.COMPARISON, true, BinaryOperatorType.LESS_THAN_EQUAL));

        // Truth
        prefix(TokenType.BANG, new UnaryOperatorParser(UnaryOperatorType.NEGATE));
        infix(TokenType.AND, new BinaryOperatorParser(Precedence.CONJUNCTION, true, BinaryOperatorType.AND));
        infix(TokenType.OR, new BinaryOperatorParser(Precedence.DISJUNCTION, true, BinaryOperatorType.OR));

        // Functional
        infix(TokenType.LEFT_PAREN, new InvocationParser());
        //infix(TokenType.LEFT_BRACE, new BlockParameterParser());

        infix(TokenType.LEFT_BRACKET, new GetSetParser());
        infix(TokenType.DOT, new AttributeParser());

        infix(TokenType.PIPE_BACKWARD, new PipeBackwardParser());
        infix(TokenType.PIPE_FORWARD, new PipeForwardParser());
    }
}
