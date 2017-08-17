/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
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

package xyz.avarel.kaiper.others;

import xyz.avarel.kaiper.KaiperScript;
import xyz.avarel.kaiper.ScriptExpr;
import xyz.avarel.kaiper.lexer.KaiperLexer;
import xyz.avarel.kaiper.runtime.Obj;
import xyz.avarel.kaiper.runtime.functions.NativeFunc;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FileTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Kaiper FILE");
        System.out.println();

        System.out.println(new KaiperLexer(new FileReader(new File("script.kip"))).getTokens().toString());

        KaiperScript exp = new KaiperScript(new FileReader(new File("script.kip")));

        exp.add("println", new NativeFunc("print","string") {
            @Override
            protected Obj eval(Map<String, Obj> arguments) {
                System.out.println(arguments.get("string"));
                return null;
            }
        });

        ScriptExpr expr = exp.compile();

        StringBuilder sb = new StringBuilder();
        expr.ast(sb, "", true);
        System.out.println(sb);

        Future<Obj> future = CompletableFuture.supplyAsync(expr::compute);

        System.out.println("\n\t\tRESULT |> " + future.get(500, TimeUnit.MILLISECONDS));
    }
}