package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.IWheelActionRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WheelActionRegistry implements IWheelActionRegistry {

    private final Map<String, IWheelAction> actions = new HashMap<>();
    private final List<String> registrationOrder = new ArrayList<>();

    @Override
    public void register(IWheelAction action) {
        actions.put(action.getId(), action);
        registrationOrder.add(action.getId());
    }

    public IWheelAction get(String id) {
        return actions.get(id);
    }

    public Map<String, IWheelAction> getActions() {
        return actions;
    }

    public List<String> getRegistrationOrder() {
        return registrationOrder;
    }
}
