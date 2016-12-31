package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelAction;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class ItemHandlerWheelActions implements IWheelActions {

    private static List<WheelAction> actions = null;

    private final IItemHandler inventory;

    public ItemHandlerWheelActions(IItemHandler inventory) {
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
                stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                player.inventory.setInventorySlotContents(i, stack);
            }
        } else if (action.getId().equals(StandardWheelActions.EXTRACT.getId())) {
            for (int i = 0 ; i < inventory.getSlots() ; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (ItemStackTools.isValid(stack)) {
                    int s = ItemStackTools.getStackSize(stack);
                    ItemStack extracted = inventory.extractItem(i, s, true);        // Simulate
                    if (player.inventory.addItemStackToInventory(extracted)) {
                        inventory.extractItem(i, s, false); // Do for real
                    }
                }
            }
        }
    }
}
