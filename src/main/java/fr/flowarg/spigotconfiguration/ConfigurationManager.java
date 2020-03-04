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

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

/**
 * SPIGOT CONFIGURATION HELPER - 2020 by FlowArg
 *
 * This interface have to be implemented by a class for manage a configuration.
 * You must specify few methods :
 *  - {@link ConfigurationManager#saveConfig()} to save the current configuration.
 *  - {@link ConfigurationManager#loadConfig()} to load a configuration.
 *  - {@link ConfigurationManager#getConfig()} to get the current configuration.
 *
 * @author FlowArg - 2020
 */
@SuppressWarnings("unused")
public interface ConfigurationManager
{
    /**
     * Save the current configuration.
     */
    void saveConfig() throws IOException;

    /**
     * Load a configuration.
     */
    void loadConfig() throws IOException, InvalidConfigurationException;

    /**
     * Get the current configuration.
     * @return the current configuration.
     */
    FileConfiguration getConfig();
}
