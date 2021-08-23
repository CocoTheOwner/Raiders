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


import java.io.File;

public class ObjectHandler implements DecreeParameterHandler<String> {
    @Override
    public KList<String> getPossibilities() {
        KList<String> p = new KList<>();

        //noinspection ConstantConditions
        for (File i : Iris.instance.getDataFolder("packs").listFiles()) {
            if (i.isDirectory()) {
                IrisData data = IrisData.get(i);
                p.add(data.getObjectLoader().getPossibleKeys());
            }
        }

        return p;
    }

    @Override
    public String toString(String irisObject) {
        return irisObject;
    }

    @Override
    public String parse(String in) throws DecreeParsingException, DecreeWhichException {
        try {
            KList<String> options = getPossibilities(in);

            if (options.isEmpty()) {
                throw new DecreeParsingException("Unable to find Object \"" + in + "\"");
            } else if (options.size() > 1) {
                throw new DecreeWhichException();
            }

            return options.get(0);
        } catch (DecreeParsingException e) {
            throw e;
        } catch (Throwable e) {
            throw new DecreeParsingException("Unable to find Object \"" + in + "\" because of an uncaught exception: " + e);
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(String.class);
    }

    @Override
    public String getRandomDefault() {
        String f = getPossibilities().getRandom();

        return f == null ? "object" : f;
    }
}
