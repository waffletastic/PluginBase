/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.util.commandhandler.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface BukkitPlugin<C extends BaseConfig> extends PluginBase<C>, Plugin {
    
    CommandHandler getCommandHandler();

    public Messager getMessager();

    public void setMessager(Messager messager);

    BasePlayer wrapPlayer(Player player);

    BasePlayer wrapSender(CommandSender sender);
}
