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


import java.util.function.Supplier;

public class AtomicCache<T> {
    private transient volatile T t;
    private transient volatile long a;
    private transient volatile int validations;
    private final Lock check;
    private final Lock time;
    private final Lock write;
    private final boolean nullSupport;

    public AtomicCache() {
        this(false);
    }

    public AtomicCache(boolean nullSupport) {
        this.nullSupport = nullSupport;
        check = new Lock("Check");
        write = new Lock("Write");
        time = new Lock("Time");
        validations = 0;
        a = -1;
        t = null;
    }

    public void reset() {
        check.lock();
        write.lock();
        time.lock();
        a = -1;
        t = null;
        time.unlock();
        write.unlock();
        check.unlock();
    }

    public T aquire(Supplier<T> t) {
        if (nullSupport) {
            return aquireNull(t);
        }

        if (this.t != null && validations > 1000) {
            return this.t;
        }

        if (this.t != null && System.currentTimeMillis() - a > 1000) {
            if (this.t != null) {
                //noinspection NonAtomicOperationOnVolatileField
                validations++;
            }

            return this.t;
        }

        check.lock();

        if (this.t == null) {
            write.lock();
            this.t = t.get();

            time.lock();

            if (a == -1) {
                a = System.currentTimeMillis();
            }

            time.unlock();
            write.unlock();
        }

        check.unlock();
        return this.t;
    }

    public T aquireNull(Supplier<T> t) {
        if (validations > 1000) {
            return this.t;
        }

        if (System.currentTimeMillis() - a > 1000) {
            //noinspection NonAtomicOperationOnVolatileField
            validations++;
            return this.t;
        }

        check.lock();
        write.lock();
        this.t = t.get();

        time.lock();

        if (a == -1) {
            a = System.currentTimeMillis();
        }

        time.unlock();
        write.unlock();
        check.unlock();
        return this.t;
    }
}
