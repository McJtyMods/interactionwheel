package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelAction;
import net.minecraftforge.items.IItemHandler;

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
    public void performServer(WheelAction action) {

    }
}
