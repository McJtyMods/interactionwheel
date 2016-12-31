package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelAction;
import mcjty.intwheel.varia.InventoryHelper;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryWheelActions implements IWheelActions {

    private static List<WheelAction> actions = null;

    private final IInventory inventory;

    public InventoryWheelActions(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<WheelAction> getActions() {
        if (actions == null) {
            actions = StandardWheelActions.createStandardActions();
        }
        return actions;
    }

    @Override
    public void performServer(WheelAction action, EntityPlayer player) {
        if (action.getId().equals(StandardWheelActions.DUMP.getId())) {
            for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                int failed = InventoryHelper.mergeItemStackSafe(inventory, null, stack, 0, inventory.getSizeInventory(), null);
                if (failed > 0) {
                    ItemStack putBack = stack.copy();
                    ItemStackTools.setStackSize(putBack, failed);
                    player.inventory.setInventorySlotContents(i, stack);
                } else {
                    player.inventory.setInventorySlotContents(i, ItemStackTools.getEmptyStack());
                }
            }
        } else if (action.getId().equals(StandardWheelActions.EXTRACT.getId())) {
            for (int i = 0 ; i < inventory.getSizeInventory() ; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (ItemStackTools.isValid(stack)) {
                    int s = ItemStackTools.getStackSize(stack);
                    ItemStack extracted = inventory.decrStackSize(i, s);
                    if (!player.inventory.addItemStackToInventory(extracted)) {
                        inventory.setInventorySlotContents(i, extracted);
                    }
                }
            }
        }

    }
}
