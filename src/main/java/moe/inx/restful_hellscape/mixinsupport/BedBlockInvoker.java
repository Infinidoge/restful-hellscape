package moe.inx.restful_hellscape.mixinsupport;

import net.minecraft.block.BedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BedBlock.class)
public interface BedBlockInvoker {
  @Invoker("wakeVillager")
  public boolean wakeVillager(World world, BlockPos pos);
}
