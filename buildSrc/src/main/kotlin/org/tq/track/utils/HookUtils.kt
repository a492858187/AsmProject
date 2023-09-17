package org.tq.track.utils


import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodNode

internal const val InitMethodName = "<init>"

internal val MethodNode.nameWithDesc: String
    get() = name + desc

internal val MethodNode.isStatic: Boolean
    get() = access and Opcodes.ACC_STATIC != 0


internal fun MethodNode.hasAnnotation(annotationDesc: String): Boolean {
    return visibleAnnotations?.find { it.desc == annotationDesc } != null
}

internal fun MethodNode.filterLambda(filter: (InvokeDynamicInsnNode) -> Boolean) : List<InvokeDynamicInsnNode> {
    val mInstructions = instructions ?: return emptyList()
    val dynamicList = mutableListOf<InvokeDynamicInsnNode>()
    mInstructions.forEach { instruction -> 
        if(instruction is InvokeDynamicInsnNode) {
            if(filter(instruction)){
                dynamicList.add(instruction)
            }
        }
    }
    return dynamicList
}