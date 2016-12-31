package mcjty.intwheel.api;

import java.util.List;

/**
 * Implement this in your block to support custom wheel actions
 */
public interface IWheelActions {

    // Get all support actions. This is called client-side
    List<WheelActionElement> getActions();
}
