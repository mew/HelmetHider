package zone.nora.helmethider.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class RenderEntityItemTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (transformedName.equals("net.minecraft.client.renderer.entity.RenderEntityItem")) {
            ClassReader reader = new ClassReader(basicClass);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.EXPAND_FRAMES);

            for (MethodNode methodNode : node.methods) {
                String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(node.name, methodNode.name, methodNode.desc);

                if (methodName.equals("doRender") || methodName.equals("func_76986_a")) {
                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), insnList());
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            try {
                node.accept(writer);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return writer.toByteArray();
        }

        return basicClass;
    }

    private InsnList insnList() {
        InsnList insnList = new InsnList();

        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zone/nora/helmethider/HelmetHider", "hideItem", "(Lnet/minecraft/entity/Entity;)Z", false));
        LabelNode hidden = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, hidden));
        insnList.add(new InsnNode(Opcodes.RETURN));
        insnList.add(hidden);

        return insnList;
    }
}
