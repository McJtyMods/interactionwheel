package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.inventory.IInventory;

import java.util.List;

public class InventoryWheelActions implements IWheelActions {

    private static List<WheelActionElement> actions = null;

    private final IInventory inventory;

    public InventoryWheelActions(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<WheelActionElement> getActions() {
        if (actions == null) {
            actions = StandardWheelActions.createStandardActions();
        }
        return actions;
    }
}
