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

package nl.codevs.raiders.decree.handlers;


import nl.codevs.raiders.decree.exceptions.DecreeParsingException;
import nl.codevs.raiders.decree.objects.DecreeParameterHandler;
import nl.codevs.raiders.decree.util.KList;
import nl.codevs.raiders.decree.util.Maths;

import java.util.concurrent.atomic.AtomicReference;

public class LongHandler implements DecreeParameterHandler<Long> {
    @Override
    public KList<Long> getPossibilities() {
        return null;
    }

    @Override
    public Long parse(String in) throws DecreeParsingException {
        try {
            AtomicReference<String> r = new AtomicReference<>(in);
            double m = getMultiplier(r);
            return (long) (Long.valueOf(r.get()).doubleValue() * m);
        } catch (Throwable e) {
            throw new DecreeParsingException("Unable to parse long \"" + in + "\"");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Long.class) || type.equals(long.class);
    }

    @Override
    public String toString(Long f) {
        return f.toString();
    }

    @Override
    public String getRandomDefault() {
        return String.valueOf(Maths.irand(0, 99));
    }
}
