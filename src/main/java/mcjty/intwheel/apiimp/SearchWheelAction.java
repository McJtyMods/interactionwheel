package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketInventoriesToClient;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.HashSet;
import java.util.Set;

public class SearchWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_SEARCH;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.SEARCH.createElement();
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        pos = player.blockPosition();
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            Set<BlockPos> found = new HashSet<>();
            for (int dx = -20 ; dx <= 20 ; dx++) {
                for (int dy = -20 ; dy <= 20 ; dy++) {
                    for (int dz = -20 ; dz <= 20 ; dz++) {
                        BlockPos p = pos.offset(dx, dy, dz);
                        IItemHandler inventory = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
                        if (inventory != null) {
                            for (int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack stack = inventory.getStackInSlot(i);
                                if (!stack.isEmpty() && heldItem.is(stack.getItem())) { // Check on item?
                                    found.add(p);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            PacketHandler.sendToPlayer(new PacketInventoriesToClient(found), (ServerPlayer) player);
        }
    }
}
