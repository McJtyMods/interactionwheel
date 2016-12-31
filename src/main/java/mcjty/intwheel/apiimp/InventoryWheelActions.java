package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

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

    }
}
