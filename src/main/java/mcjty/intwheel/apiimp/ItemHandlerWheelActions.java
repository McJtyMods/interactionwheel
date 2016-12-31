package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class ItemHandlerWheelActions implements IWheelActions {

    private static List<WheelActionElement> actions = null;

    private final IItemHandler inventory;

    public ItemHandlerWheelActions(IItemHandler inventory) {
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
