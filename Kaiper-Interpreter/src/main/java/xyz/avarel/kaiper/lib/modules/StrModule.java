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

package xyz.avarel.kaiper.lib.modules;

import xyz.avarel.kaiper.interpreter.ExprInterpreter;
import xyz.avarel.kaiper.runtime.Str;
import xyz.avarel.kaiper.runtime.modules.NativeModule;

public class StrModule extends NativeModule {
    public StrModule(ExprInterpreter interpreter) {
        super("Obj");
        declare("TYPE", Str.TYPE);

        // "self", "start", "end", "step"
//        declare("slice", new NativeFunc("slice",
//                new VariableRTPattern("self"),
//                new VariableRTPattern("start", true),
//                new VariableRTPattern("end", true),
//                new VariableRTPattern("step", true)
//        ) {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                Str str = arguments.get("self").as(Str.TYPE);
//                Obj startObj = arguments.get("start");
//                Obj endObj = arguments.get("end");
//                Obj stepObj = arguments.get("step");
//
//                int start, end, step;
//
//                if (startObj == Null.VALUE) {
//                    start = 0;
//                } else {
//                    start = startObj.as(Int.TYPE).value();
//                    if (start < 0) start += str.length();
//                }
//
//                if (endObj == Null.VALUE) {
//                    end = str.length();
//                } else {
//                    end = endObj.as(Int.TYPE).value();
//                    if (end < 0) end += str.length();
//                }
//
//                if (stepObj == Null.VALUE) {
//                    step = 1;
//                } else {
//                    step = stepObj.as(Int.TYPE).value();
//                }
//
//                if (step == 1) {
//                    return Str.of(str.value.substring(Math.max(0, start), Math.min(str.length(), end)));
//                } else {
//                    if (step > 0) {
//                        StringBuilder buffer = new StringBuilder();
//
//                        for (int i = start; i < end; i += step) {
//                            buffer.append(str.get(i));
//                        }
//
//                        return Str.of(buffer.toString());
//                    } else if (step < 0) {
//                        StringBuilder buffer = new StringBuilder();
//
//                        for (int i = end - 1; i >= start; i += step) {
//                            buffer.append(str.get(i));
//                        }
//
//                        return Str.of(buffer.toString());
//                    } else { // step == 0
//                        return Null.VALUE;
//                    }
//                }
//            }
//        });
//
//        declare("length", new NativeFunc("length", "self") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return Int.of(arguments.get("self").as(Str.TYPE).length());
//            }
//        });
//
//        declare("contains", new NativeFunc("contains", "self", "query") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).contains(arguments.get("query").as(Str.TYPE));
//            }
//        });
//        declare("indexOf", new NativeFunc("indexOf", "self", "query") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).indexOf(arguments.get("query").as(Str.TYPE));
//            }
//        });
//        declare("split", new NativeFunc("split", "self", "query") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).split(arguments.get("query").as(Str.TYPE));
//            }
//        });
//        declare("substring", new RuntimeMultimethod("substring")
//                .addCase(new RuntimePatternCase("self", "start"),
//                        scope -> scope.get("self").as(Str.TYPE).substring(scope.get("start").as(Int.TYPE)))
//                .addCase(new RuntimePatternCase("self", "start", "end"),
//                        scope -> scope.get("self").as(Str.TYPE).substring(scope.get("start").as(Int.TYPE), scope.get("end").as(Int.TYPE)))
//        );
//        declare("toVector", new NativeFunc("toVector", "self") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).toVector();
//            }
//        });
//        declare("toLowerCase", new NativeFunc("toLowerCase", "self") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).toLowerCase();
//            }
//        });
//        declare("toUpperCase", new NativeFunc("toUpperCase", "self") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).toUpperCase();
//            }
//        });
//        declare("trim", new NativeFunc("trim", "self") {
//            @Override
//            protected Obj eval(Map<String, Obj> arguments) {
//                return arguments.get("self").as(Str.TYPE).trim();
//            }
//        });
    }
}