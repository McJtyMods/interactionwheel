package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.IWheelActionRegistry;

import java.util.HashMap;
import java.util.Map;

public class WheelActionRegistry implements IWheelActionRegistry {

    private final Map<String, IWheelAction> actions = new HashMap<>();

    @Override
    public void register(IWheelAction action) {
        actions.put(action.getId(), action);
    }

    public IWheelAction get(String id) {
        return actions.get(id);
    }

    public Map<String, IWheelAction> getActions() {
        return actions;
    }
}
