package com.codenjoy.dojo.chess.engine.level;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.chess.engine.model.Element;
import com.codenjoy.dojo.chess.engine.model.*;
import com.codenjoy.dojo.chess.engine.model.item.Barrier;
import com.codenjoy.dojo.chess.engine.model.item.Square;
import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.codenjoy.dojo.chess.engine.service.ElementMapper;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.LevelUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.chess.engine.model.Element.BARRIER;
import static com.codenjoy.dojo.chess.engine.model.Element.SQUARE;

public class Level {

    private final LengthToXY xy;
    private final String map;

    public Level(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    public List<Point> pieces(Color color, Piece.Type type) {
        return LevelUtils.getObjects(xy, map, Function.identity(), ElementMapper.mapToElement(color, type));
    }

    public List<Color> presentedColors() {
        Set<Color> presented = Sets.newHashSet();
        for (int i = 0; i < map.length(); i++) {
            Optional.ofNullable(Element.of(map.charAt(i)))
                    .filter(e -> Lists.newArrayList(Element.pieces()).contains(e))
                    .map(ElementMapper::mapToColor)
                    .ifPresent(presented::add);
        }
        return Lists.newArrayList(presented);
    }

    public List<Square> squares() {
        List<Point> squares = LevelUtils.getObjects(xy, map, Function.identity(), SQUARE);
        List<Point> pieces = LevelUtils.getObjects(xy, map, Function.identity(), Element.pieces());
        squares.addAll(pieces);
        return squares.stream()
                .map(Square::new)
                .collect(Collectors.toList());
    }

    public List<Barrier> barriers() {
        return LevelUtils.getObjects(xy, map, Function.identity(), BARRIER).stream()
                .map(Barrier::new)
                .collect(Collectors.toList());
    }
}