package com.codenjoy.dojo.chess.engine.service;

import com.codenjoy.dojo.services.Joystick;
import org.apache.commons.lang3.NotImplementedException;

public interface NoDirectionsJoystick extends Joystick {

    @Override
    default void down() {
        throw new NotImplementedException();
    }

    @Override
    default void up() {
        throw new NotImplementedException();
    }

    @Override
    default void left() {
        throw new NotImplementedException();
    }

    @Override
    default void right() {
        throw new NotImplementedException();
    }

}
