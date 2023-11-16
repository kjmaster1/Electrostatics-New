package com.kjmaster.electrostatics.treetap;

import com.kjmaster.electrostatics.Config;
import com.kjmaster.kjlib.items.BaseItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.kjmaster.electrostatics.Config.tapDurability;

public class TreeTapItem extends BaseItem {

    public TreeTapItem() {
        super(new Item.Properties().durability(tapDurability));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        if (state.is(BlockTags.LOGS_THAT_BURN) && !level.isClientSide() && Config.isTapEnabled) {
            Containers.dropItemStack(level, context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), new ItemStack(TreeTapModule.RESIN.get()));
            ItemStack stack = context.getItemInHand();
            if (context.getPlayer() != null) {
                stack.hurtAndBreak(1, context.getPlayer(), (p_40992_) -> {
                    p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
            }
        }
        return super.useOn(context);
    }
}
