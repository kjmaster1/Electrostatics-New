package com.kjmaster.electrostatics.staticgenerator;

import com.kjmaster.electrostatics.Config;
import com.kjmaster.electrostatics.treetap.TreeTapModule;
import com.kjmaster.kjlib.api.container.DefaultContainerProvider;
import com.kjmaster.kjlib.blocks.BaseBlock;
import com.kjmaster.kjlib.builder.BlockBuilder;
import com.kjmaster.kjlib.container.ContainerFactory;
import com.kjmaster.kjlib.container.GenericContainer;
import com.kjmaster.kjlib.container.GenericItemHandler;
import com.kjmaster.kjlib.container.SlotDefinition;
import com.kjmaster.kjlib.tileentity.Cap;
import com.kjmaster.kjlib.tileentity.CapType;
import com.kjmaster.kjlib.tileentity.GenericEnergyStorage;
import com.kjmaster.kjlib.tileentity.TickingTileEntity;
import com.kjmaster.kjlib.varia.EnergyTools;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.kjmaster.kjlib.api.container.DefaultContainerProvider.container;
import static com.kjmaster.kjlib.builder.TooltipBuilder.*;

public class StaticGeneratorTileEntity extends TickingTileEntity {

    public static final int SLOT_CHARGEITEM = 0;

    private final Map<UUID, Double> xOlds = new HashMap<>();
    private final Map<UUID, Double> yOlds = new HashMap<>();
    private final Map<UUID, Double> zOlds = new HashMap<>();

    private static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(1)
            .slot(SlotDefinition.specific(EnergyTools::isEnergyItem).in().out(), SLOT_CHARGEITEM, 82, 24)
            .playerSlots(10, 70));

    @Cap(type = CapType.ITEMS_AUTOMATION)
    private final GenericItemHandler items = GenericItemHandler.create(this, CONTAINER_FACTORY)
            .itemValid((slot, stack) -> EnergyTools.isEnergyItem(stack))
            .build();

    @Cap(type = CapType.ENERGY)
    private final GenericEnergyStorage energyStorage = new GenericEnergyStorage(this, false, Config.staticGeneratorMaxCapacity, 0);

    @Cap(type = CapType.CONTAINER)
    private final LazyOptional<MenuProvider> screenHandler = LazyOptional.of(() -> new DefaultContainerProvider<GenericContainer>("Electrostatics Generator")
            .containerSupplier(container(StaticGeneratorModule.CONTAINER_STATICGENERATOR, CONTAINER_FACTORY, this))
            .itemHandler(() -> items)
            .energyHandler(() -> energyStorage)
            .setupSync(this)
    );

    public StaticGeneratorTileEntity(BlockPos pos, BlockState state) {
        super(StaticGeneratorModule.TYPE_STATICGENERATOR.get(), pos, state);
    }

    public static BaseBlock createBlock() {
        return new BaseBlock(new BlockBuilder()
                .tileEntitySupplier(StaticGeneratorTileEntity::new)
                .info(key("message.electrostatics.shiftmessage"))
                .infoShift(header(), gold(), parameter("info", itemStack -> Config.staticGeneratorRfPerTick + " RF/FE"))
        );
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        CompoundTag info = tagCompound.getCompound("Info");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tagCompound) {
        super.saveAdditional(tagCompound);
        CompoundTag infoTag = getOrCreateInfo(tagCompound);
    }

    protected CompoundTag getOrCreateInfo(CompoundTag tagCompound) {
        if (tagCompound.contains("Info")) {
            return tagCompound.getCompound("Info");
        }
        CompoundTag data = new CompoundTag();
        tagCompound.put("Info", data);
        return data;
    }

    @Override
    public InteractionResult onBlockActivated(BlockState state, Player player, InteractionHand hand, BlockHitResult result) {
        if (canPlayerAccess(player)) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void tickServer() {
        markDirtyQuick();
        handleChargingItem(items);
        handleSendingEnergy();
        handlePowerGeneration();
    }

    private void handlePowerGeneration() {

        AABB entityCheckingBB = new AABB(getBlockPos().getX() - Config.staticGeneratorXRange, getBlockPos().getY() - Config.staticGeneratorYRange, getBlockPos().getZ() - Config.staticGeneratorZRange,
                getBlockPos().getX() + Config.staticGeneratorXRange, getBlockPos().getY() + Config.staticGeneratorYRange, getBlockPos().getZ() + Config.staticGeneratorZRange);

        if (this.level == null) {
            return;
        }

        List<Entity> entities = this.level.getEntities(null, entityCheckingBB);

        int numEntitiesGenerating = 0;

        for (Entity entity: entities) {
            if (entity instanceof LivingEntity livingEntity && numEntitiesGenerating < Config.staticGeneratorMaxEntities) {
                ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);

                if (stack.is(TreeTapModule.RUBBER_BOOTS.get()) && livingEntity.getFeetBlockState().getBlock() instanceof CarpetBlock) {

                    UUID uuid = livingEntity.getUUID();

                    if (!xOlds.containsKey(uuid) || !yOlds.containsKey(uuid) || !zOlds.containsKey(uuid)) {
                        xOlds.put(uuid, livingEntity.getX());
                        yOlds.put(uuid, livingEntity.getY());
                        zOlds.put(uuid, livingEntity.getZ());
                    }

                    double xOld = xOlds.get(uuid);
                    double yOld = yOlds.get(uuid);
                    double zOld = zOlds.get(uuid);

                    if (livingEntity.getX() != xOld || livingEntity.getY() != yOld || livingEntity.getZ() != zOld) {
                        numEntitiesGenerating++;
                    }

                    xOlds.put(uuid, livingEntity.getX());
                    yOlds.put(uuid, livingEntity.getY());
                    zOlds.put(uuid, livingEntity.getZ());
                }
            }
        }

        long rf = getRfPerTick() * numEntitiesGenerating;
        energyStorage.produceEnergy(rf);
    }

    public long getRfPerTick() {
        return Config.staticGeneratorRfPerTick;
    }

    private void handleChargingItem(IItemHandler handler) {
        ItemStack stack = handler.getStackInSlot(SLOT_CHARGEITEM);
        if (!stack.isEmpty()) {
            long storedPower = energyStorage.getEnergy();
            long rfToGive = Math.min(Config.staticGeneratorChargePerTick, storedPower);
            long received = EnergyTools.receiveEnergy(stack, rfToGive);
            energyStorage.consumeEnergy(received);
        }
    }

    private void handleSendingEnergy() {
        long storedPower = energyStorage.getEnergy();
        EnergyTools.handleSendingEnergy(level, worldPosition, storedPower, Config.staticGeneratorSendPerTick, energyStorage);
    }
}
