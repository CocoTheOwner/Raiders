/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2021 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nl.codevs.raiders.decree.util;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;

public class KSet<T> extends HashSet<T> {
    @Serial
    private static final long serialVersionUID = 1L;

    public KSet() {
        super();
    }

    public KSet(Collection<? extends T> c) {
        super(c);
    }

    public KSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public KSet(int initialCapacity) {
        super(initialCapacity);
    }

    public KSet<T> copy() {
        return new KSet<T>(this);
    }
}
