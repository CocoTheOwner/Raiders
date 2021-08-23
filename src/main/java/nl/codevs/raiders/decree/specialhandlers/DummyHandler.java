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

package nl.codevs.raiders.decree.specialhandlers;

import nl.codevs.raiders.decree.DecreeParameterHandler;
import nl.codevs.raiders.decree.exceptions.DecreeParsingException;
import nl.codevs.raiders.decree.exceptions.DecreeWhichException;

import java.util.List;

public class DummyHandler implements DecreeParameterHandler<Object> {
    @Override
    public List getPossibilities() {
        return null;
    }

    public boolean isDummy()
    {
        return true;
    }

    @Override
    public String toString(Object o) {
        return null;
    }

    @Override
    public Object parse(String in) throws DecreeParsingException, DecreeWhichException {
        return null;
    }

    @Override
    public boolean supports(Class type) {
        return false;
    }
}
