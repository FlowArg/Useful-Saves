/*
 * All content copyright FlowArg, unless otherwise indicated. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package fr.flowarg.spigotconfiguration;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

/**
 * SPIGOT CONFIGURATION HELPER - 2020 by FlowArg
 *
 * This class represent an entry in a {@link FileConfiguration}.
 * You can get a value of the key or set a specific value to the key.
 *
 * @author FlowArg - 2020
 *
 * @param <T> This parameter represent the configuration value type.
 */
@SuppressWarnings("unused")
public abstract class SpigotConfigEntry<T>
{
    protected final String path;
    protected final FileConfiguration configuration;

    /**
     * Construct a new {@link SpigotConfigEntry}.
     * @param path          path in the {@link FileConfiguration} of the key.
     * @param configuration given configuration. Nothing else to add.
     */
    public SpigotConfigEntry(String path, FileConfiguration configuration)
    {
        this.path = path;
        this.configuration = configuration;
    }

    /**
     * Get the specified path.
     * @return the specified path.
     */
    public String getPath()
    {
        return this.path;
    }

    /**
     * Get the specified configuration.
     * @return the specified configuration.
     */
    public FileConfiguration getConfiguration()
    {
        return this.configuration;
    }

    /**
     * Get the given object {@link T} of the key.
     * @return the given object {@link T} of the key.
     */
    public abstract T get();

    /**
     * Set the given value in the {@link SpigotConfigEntry#path}
     *
     * @param value Value to set
     */    
    public abstract void set(T value);

    public static class StringEntry extends SpigotConfigEntry<String>
    {
        /**
         * Construct a new {@link StringEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public StringEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public String get()
        {
            return this.configuration.getString(this.path);
        }

        @Override
        public void set(String value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class IntegerEntry extends SpigotConfigEntry<Integer>
    {
        /**
         * Construct a new {@link IntegerEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public IntegerEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Integer get()
        {
            return this.configuration.getInt(this.path);
        }

        @Override
        public void set(Integer value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class DoubleEntry extends SpigotConfigEntry<Double>
    {
        /**
         * Construct a new {@link DoubleEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public DoubleEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Double get()
        {
            return this.configuration.getDouble(this.path);
        }

        @Override
        public void set(Double value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class BooleanEntry extends SpigotConfigEntry<Boolean>
    {
        /**
         * Construct a new {@link BooleanEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public BooleanEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Boolean get()
        {
            return this.configuration.getBoolean(this.path);
        }

        @Override
        public void set(Boolean value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class LongEntry extends SpigotConfigEntry<Long>
    {
        /**
         * Construct a new {@link LongEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public LongEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Long get()
        {
            return this.configuration.getLong(this.path);
        }

        @Override
        public void set(Long value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class ColorEntry extends SpigotConfigEntry<Color>
    {
        /**
         * Construct a new {@link ColorEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public ColorEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Color get()
        {
            return this.configuration.getColor(this.path);
        }

        @Override
        public void set(Color value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class ItemStackEntry extends SpigotConfigEntry<ItemStack>
    {
        /**
         * Construct a new {@link ItemStackEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public ItemStackEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public ItemStack get()
        {
            return this.configuration.getItemStack(this.path);
        }

        @Override
        public void set(ItemStack value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class LocationEntry extends SpigotConfigEntry<Location>
    {
        /**
         * Construct a new {@link LocationEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public LocationEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Location get()
        {
            return this.configuration.getLocation(this.path);
        }

        @Override
        public void set(Location value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static class VectorEntry extends SpigotConfigEntry<Vector>
    {
        /**
         * Construct a new {@link VectorEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public VectorEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        @Override
        public Vector get()
        {
            return this.configuration.getVector(this.path);
        }

        @Override
        public void set(Vector value)
        {
            this.configuration.set(this.path, value);
        }
    }

    public static abstract class ListEntry<T> extends SpigotConfigEntry<List<T>>
    {
        /**
         * Construct a new {@link ListEntry}.
         *
         * @param path          path in the {@link FileConfiguration} of the key.
         * @param configuration given configuration. Nothing to add.
         */
        public ListEntry(String path, FileConfiguration configuration)
        {
            super(path, configuration);
        }

        public abstract List<T> get();
        public abstract void set(List<T> value);

        public static class StringListEntry extends ListEntry<String>
        {
            /**
             * Construct a new {@link StringListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public StringListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<String> get()
            {
                return this.configuration.getStringList(this.path);
            }

            @Override
            public void set(List<String> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class IntegerListEntry extends ListEntry<Integer>
        {
            /**
             * Construct a new {@link IntegerListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public IntegerListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Integer> get()
            {
                return this.configuration.getIntegerList(this.path);
            }

            @Override
            public void set(List<Integer> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class DoubleListEntry extends ListEntry<Double>
        {
            /**
             * Construct a new {@link DoubleListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public DoubleListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Double> get()
            {
                return this.configuration.getDoubleList(this.path);
            }

            @Override
            public void set(List<Double> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class BooleanListEntry extends ListEntry<Boolean>
        {
            /**
             * Construct a new {@link BooleanListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public BooleanListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Boolean> get()
            {
                return this.configuration.getBooleanList(this.path);
            }

            @Override
            public void set(List<Boolean> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class LongListEntry extends ListEntry<Long>
        {
            /**
             * Construct a new {@link LongListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public LongListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Long> get()
            {
                return this.configuration.getLongList(this.path);
            }

            @Override
            public void set(List<Long> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class ByteListEntry extends ListEntry<Byte>
        {
            /**
             * Construct a new {@link ByteListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public ByteListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Byte> get()
            {
                return this.configuration.getByteList(this.path);
            }

            @Override
            public void set(List<Byte> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class CharacterListEntry extends ListEntry<Character>
        {
            /**
             * Construct a new {@link CharacterListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public CharacterListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Character> get()
            {
                return this.configuration.getCharacterList(this.path);
            }

            @Override
            public void set(List<Character> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class FloatListEntry extends ListEntry<Float>
        {
            /**
             * Construct a new {@link FloatListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public FloatListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Float> get()
            {
                return this.configuration.getFloatList(this.path);
            }

            @Override
            public void set(List<Float> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class MapListEntry extends ListEntry<Map<?, ?>>
        {
            /**
             * Construct a new {@link MapListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public MapListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }

            @Override
            public List<Map<?, ?>> get()
            {
                return this.configuration.getMapList(this.path);
            }

            @Override
            public void set(List<Map<?, ?>> value)
            {
                this.configuration.set(this.path, value);
            }
        }

        public static class ShortListEntry extends ListEntry<Short>
        {
            /**
             * Construct a new {@link ShortListEntry}.
             *
             * @param path          path in the {@link FileConfiguration} of the key.
             * @param configuration given configuration. Nothing to add.
             */
            public ShortListEntry(String path, FileConfiguration configuration)
            {
                super(path, configuration);
            }
            
            @Override
            public List<Short> get()
            {
                return this.configuration.getShortList(this.path);
            }

            @Override
            public void set(List<Short> value)
            {
                this.configuration.set(this.path, value);
            }
        }
    }
}
