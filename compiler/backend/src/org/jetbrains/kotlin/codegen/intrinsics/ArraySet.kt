/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.codegen.intrinsics

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.codegen.CallableMethod
import org.jetbrains.kotlin.codegen.ExpressionCodegen
import org.jetbrains.kotlin.codegen.ExtendedCallable
import org.jetbrains.kotlin.codegen.StackValue
import org.jetbrains.kotlin.psi.JetExpression
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter

import org.jetbrains.kotlin.codegen.AsmUtil.correctElementType

public class ArraySet : IntrinsicMethod() {
    override fun generateImpl(codegen: ExpressionCodegen, v: InstructionAdapter, returnType: Type, element: PsiElement?, arguments: List<JetExpression>, receiver: StackValue): Type {
        receiver.put(receiver.type, v)
        val type = correctElementType(receiver.type)

        codegen.gen(arguments.get(0), Type.INT_TYPE)
        codegen.gen(arguments.get(1), type)

        v.astore(type)
        return Type.VOID_TYPE
    }

    override fun supportCallable(): Boolean {
        return true
    }

    override fun toCallable(method: CallableMethod): ExtendedCallable {
        val type = correctElementType(method.getThisType())
        return object: IntrinsicCallable(Type.VOID_TYPE, listOf(Type.INT_TYPE, type), method.getThisType(), method.getReceiverClass()) {
            override fun invokeIntrinsic(v: InstructionAdapter) {
                v.astore(type)
            }

        }
    }
}
