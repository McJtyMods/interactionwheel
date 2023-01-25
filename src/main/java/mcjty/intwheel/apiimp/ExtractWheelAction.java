package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;

public class ExtractWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_EXTRACT;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.EXTRACT.createElement();
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(inventory -> {
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        int s = stack.getCount();
                        ItemStack extracted = inventory.extractItem(i, s, true);        // Simulate
                        if (player.getInventory().add(extracted)) {
                            inventory.extractItem(i, s, false); // Do for real
                        }
                    }
                }
            });
        }
        // @todo 1.19.2
//        player.openContainer.detectAndSendChanges();
    }
}
