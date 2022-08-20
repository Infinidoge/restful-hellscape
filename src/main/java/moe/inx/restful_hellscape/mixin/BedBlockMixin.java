package moe.inx.restful_hellscape.mixin;

import moe.inx.restful_hellscape.RestfulHellscape;
import moe.inx.restful_hellscape.mixinsupport.BedBlockInvoker;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin
    extends HorizontalFacingBlock implements BlockEntityProvider {

  protected BedBlock INSTANCE = ((BedBlock)(Object)this);

  protected BedBlockMixin(DyeColor dyeColor, AbstractBlock.Settings settings) {
    super(settings);
  }

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
    if (state.get(INSTANCE.OCCUPIED)) {
      if (!((BedBlockInvoker)INSTANCE).wakeVillager(world, pos)) {
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
