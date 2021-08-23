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

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.locks.ReentrantLock;

@Data
@Accessors(
        chain = true
)
public class DecreeLock {
    private transient final ReentrantLock lock;
    private transient final String name;
    private transient boolean disabled = false;

    public DecreeLock(String name) {
        this.name = name;
        lock = new ReentrantLock(false);
    }

    public void lock() {
        if (disabled) {
            return;
        }

        lock.lock();
    }

    public void unlock() {
        if (disabled) {
            return;
        }
        try {
            lock.unlock();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
