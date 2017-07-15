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

package xyz.avarel.aje.parser;

import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.Single;
import xyz.avarel.aje.ast.flow.Statements;
import xyz.avarel.aje.ast.functions.ParameterData;
import xyz.avarel.aje.ast.variables.Identifier;
import xyz.avarel.aje.exceptions.SyntaxException;
import xyz.avarel.aje.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AJEParserUtils {
    public static final Identifier THIS_ID = new Identifier("this");
    public static final Identifier OBJ_ID = new Identifier("Object");

    public static Expr parseBlock(AJEParser parser) {
        Expr expr = new Statements();
        parser.eat(TokenType.LEFT_BRACE);
        if (!parser.match(TokenType.RIGHT_BRACE)) {
            expr = parser.parseStatements();
            parser.eat(TokenType.RIGHT_BRACE);
        }
        return expr;
    }

    public static List<Single> parseArguments(AJEParser parser) {
        List<Single> arguments = new ArrayList<>();

        parser.eat(TokenType.LEFT_PAREN);
        if (!parser.match(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(parser.parseSingle());
            } while (parser.match(TokenType.COMMA));
            parser.eat(TokenType.RIGHT_PAREN);
        }

        return arguments;
    }

    public static List<ParameterData> parseParameters(AJEParser parser) {
        List<ParameterData> parameters = new ArrayList<>();

        parser.eat(TokenType.LEFT_PAREN);
        if (!parser.match(TokenType.RIGHT_PAREN)) {
            Set<String> paramNames = new HashSet<>();
            boolean requireDef = false;

            do {
                ParameterData parameter = parseParameter(parser, requireDef);

                if (parameter.getDefault() != null) {
                    requireDef = true;
                }

                if (paramNames.contains(parameter.getName())) {
                    throw new SyntaxException("Duplicate parameter name", parser.getLast().getPosition());
                } else {
                    paramNames.add(parameter.getName());
                }

                if (parameter.isRest() && parser.match(TokenType.COMMA)) {
                    throw new SyntaxException("Rest parameters must be the last parameter",
                            parser.peek(0).getPosition());
                }

                parameters.add(parameter);
            } while (parser.match(TokenType.COMMA));
            parser.match(TokenType.RIGHT_PAREN);
        }

        return parameters;
    }

    public static ParameterData parseParameter(AJEParser parser, boolean requireDef) {
        boolean rest = parser.match(TokenType.REST);

        String parameterName = parser.eat(TokenType.IDENTIFIER).getString();

        Identifier parameterType = OBJ_ID;
        Single parameterDefault = null;

        if (parser.match(TokenType.COLON)) {
            parameterType = parser.parseIdentifier();
        }

        if (parser.match(TokenType.ASSIGN)) {
            parameterDefault = parser.parseSingle();
        } else if (requireDef) {
            throw new SyntaxException("All parameters after the first default requires a default",
                    parser.peek(0).getPosition());
        }

        return new ParameterData(parameterName, parameterType, parameterDefault, rest);
    }
}