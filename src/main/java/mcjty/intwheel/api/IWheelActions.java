package mcjty.intwheel.api;

import java.util.List;

/**
 * Implement this in your block to support custom wheel actions
 */
public interface IWheelActions {

    // Get all support actions. This is called client-side
    List<WheelAction> getActions();

    // Perform an action. This is called server-side
    void performServer(WheelAction action);

    // Perform an action. This is called client-side. If this returns false the server
    // side action is not performed
    default boolean performClient(WheelAction action) { return true; }
}
