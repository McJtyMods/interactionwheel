package mcjty.intwheel.api;

import java.util.List;

/**
 * Implement this in your block to support custom wheel actions
 */
public interface IWheelBlockSupport {

    /**
     * Update the actions. You get a list of action elements that is already filled
     * in by all the registered providers. You can update the list here. Even remove
     * actions if you want.
     * @param actions a list that you can modify
     */
    void updateWheelActions(List<WheelActionElement> actions);
}
