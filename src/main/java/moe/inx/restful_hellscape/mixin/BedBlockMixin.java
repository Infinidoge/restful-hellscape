package moe.inx.restful_hellscape.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin {

  @Shadow public abstract boolean wakeVillager(World world, BlockPos pos);
  @Shadow public static BooleanProperty OCCUPIED;

  @Inject(
      method = "onUse",
      at = @At(
          value = "INVOKE",
          target =
              "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z")
      ,
      cancellable = true)
  public void
  onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
        Hand hand, BlockHitResult hit,
        CallbackInfoReturnable<ActionResult> ci) {
    if (state.get(OCCUPIED)) {
      if (!wakeVillager(world, pos)) {
        player.sendMessage(Text.translatable("block.minecraft.bed.occupied"),
                           true);
      }
      ci.setReturnValue(ActionResult.SUCCESS);
    } else {
      player.sleep(pos);
      ci.setReturnValue(ActionResult.SUCCESS);
    }
  }
}
