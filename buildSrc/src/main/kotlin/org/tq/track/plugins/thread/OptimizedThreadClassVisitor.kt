package org.tq.track.plugins.thread

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode
import org.tq.track.utils.InitMethodName
import org.tq.track.utils.insertArgument
import org.tq.track.utils.nameWithDesc
import org.tq.track.utils.simpleClassName

internal interface OptimizedThreadConfigParameters:InstrumentationParameters {
    @get:Input
    val config: Property<OptimizedThreadConfig>
}

internal abstract class OptimizedThreadClassVisitorFactory:AsmClassVisitorFactory<OptimizedThreadConfigParameters>{
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return OptimizedThreadClassVisitor(
            config = parameters.get().config.get(),
            nextClassVisitor = nextClassVisitor
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }
}

private class OptimizedThreadClassVisitor(
    private val config: OptimizedThreadConfig,
    private val nextClassVisitor: ClassVisitor
) : ClassNode(Opcodes.ASM5) {
    
    companion object {
        private const val executorsClass = "java/util/concurrent/Executors"
        
        private const val threadClass = "java/lang/Thread"
        
        private const val threadFactoryClass = "java/util/concurrent/ThreadFactory"
        
        private const val threadFactoryNewThreadMethodDesc = "newThread(Ljava/lang/Runnable;)Ljava/lang/Thread;"
    }

    override fun visitEnd() {
        super.visitEnd()
        methods.forEach { methodNode ->
            val instructions = methodNode.instructions
            if(instructions != null && instructions.size() > 0) {
                instructions.forEach { instruction ->
                    when(instruction.opcode) {
                        Opcodes.INVOKESTATIC -> {
                            val methodInsnNode = instruction as? MethodInsnNode
                            if(methodInsnNode?.owner == executorsClass) {
                                transformInvokeExecutorsInstruction(
                                    methodNode,
                                    instruction
                                )
                            }
                        }
                        Opcodes.NEW -> {
                            val typeInsnNode = instruction as? TypeInsnNode
                            if(typeInsnNode != null) {
                                if(typeInsnNode.desc == threadClass) {
                                    if(!isThreadFactoryMethod(methodNode)){
                                        transformNewThreadInstruction(methodNode, instruction)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        accept(nextClassVisitor)
    }
    
    private fun transformInvokeExecutorsInstruction(methodNode: MethodNode, methodInsnNode: MethodInsnNode) {
        val pointMethod = config.executorsMethodNameList.find { it == methodInsnNode.name }
        if(pointMethod != null) {
            methodInsnNode.owner = config.optimizedExecutorsClass
            methodInsnNode.insertArgument(String::class.java)
            methodNode.instructions.insertBefore(methodInsnNode, LdcInsnNode(simpleClassName))
        }
    }
    
    private fun transformNewThreadInstruction(methodNode: MethodNode, typeInsnNode: TypeInsnNode) {
        val instructions = methodNode.instructions
        val typeInsnNodeIndex = instructions.indexOf(typeInsnNode)
        for(index in typeInsnNodeIndex + 1 until instructions.size()){
            val node = instructions[index]
            if(node is MethodInsnNode && node.isThreadInitMethod()) {
                typeInsnNode.desc = config.optimizedThreadClass
                node.owner = config.optimizedThreadClass
                node.insertArgument(String::class.java)
                instructions.insertBefore(node, LdcInsnNode(simpleClassName))
                break
            }
        }
    }
    
    private fun MethodInsnNode.isThreadInitMethod(): Boolean {
        return this.owner == threadClass && this.name == InitMethodName
    }
    
    private fun ClassNode.isThreadFactoryMethod(methodNode: MethodNode): Boolean {
        return this.interfaces?.contains(threadFactoryClass) == true && 
                methodNode.nameWithDesc == threadFactoryNewThreadMethodDesc
    }
    
}