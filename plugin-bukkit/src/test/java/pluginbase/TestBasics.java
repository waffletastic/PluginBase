/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package pluginbase;

import pluginbase.bukkit.AbstractBukkitPlugin;
import pluginbase.bukkit.CommandUtil;
import pluginbase.config.serializers.Serializers;
import pluginbase.plugin.BaseConfig;
import pluginbase.plugin.Settings;
import pluginbase.plugin.Settings.Language;
import pluginbase.util.MockConfig;
import pluginbase.util.MockMessages;
import pluginbase.util.MockPlugin;
import pluginbase.util.TestInstanceCreator;
import junit.framework.Assert;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractBukkitPlugin.class, SimplePluginManager.class})
public class TestBasics {
    TestInstanceCreator creator;
    Server mockServer;
    CommandSender mockCommandSender;

    @Before
    public void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertTrue(creator.setUp());
        mockServer = creator.getServer();
        mockCommandSender = creator.getCommandSender();
    }

    @After
    public void tearDown() throws Exception {
        creator.tearDown();
    }

    @Test
    public void testEnableDebugMode() throws Exception {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("PluginBase");
        MockPlugin myPlugin = (MockPlugin) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Make a fake server folder to fool MV into thinking a world folder exists.
        File serverDirectory = new File(creator.getPlugin().getServerInterface().getWorldContainer(), "world");
        serverDirectory.mkdirs();

        // Assert debug mode is off
        Assert.assertEquals(0, (int) myPlugin.getSettings().getDebugLevel());

        Assert.assertFalse(myPlugin.getSettings().isFirstRun());

        // Send the debug command.
        CommandUtil.runCommand(plugin, mockCommandSender, "pb debug", "3");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb test");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb confirm");

        //Assert.assertTrue(MockQueuedCommand.TEST);

        CommandUtil.runCommand(plugin, mockCommandSender, "pb help");
        CommandUtil.runCommand(plugin, mockCommandSender, "pb");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb version", "-p");

        Assert.assertFalse(myPlugin.getSettings().isFirstRun()));

        Assert.assertEquals(3, (int) myPlugin.getSettings().getDebugLevel());
        
        myPlugin.getMessager().message(myPlugin.wrapSender(mockCommandSender), MockMessages.TEST_MESSAGE, "And a test arg");

        Assert.assertEquals(Serializers.getSerializer(Language.LocaleSerializer.class).serialize(BaseConfig.LOCALE.getDefault()), myPlugin.getSettings().getLanguageSettings().getLocale());
        
        myPlugin.getSettings().getLanguageSettings().setLocale(Locale.CANADA);

        Assert.assertEquals(Locale.CANADA, myPlugin.getSettings().getLanguageSettings().getLocale());
        
        myPlugin.saveConfig();

        MockConfig defaults = new MockConfig(myPlugin);

        Assert.assertEquals(defaults.specificTest, myPlugin.getConfig().get("specific_test"));
        Map<String, Integer> testMap = myPlugin.config().get(MockConfig.SPECIFIC_TEST);
        Assert.assertEquals(null, testMap.get("test1"));
        testMap.put("test1", 25);
        myPlugin.config().set(MockConfig.SPECIFIC_TEST, testMap);
        Assert.assertEquals(25, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST).get("test1"));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        testMap = new HashMap<String, Integer>(1);
        testMap.put("test1", 25);
        Assert.assertEquals(testMap, myPlugin.config().get(MockConfig.SPECIFIC_TEST));

        Assert.assertEquals(null, myPlugin.config().get(MockConfig.SPECIFIC_TEST, "test2"));
        myPlugin.config().set(MockConfig.SPECIFIC_TEST, "test2", 50);
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST).get("test2"));
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST, "test2"));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST).get("test2"));
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST, "test2"));

        Assert.assertEquals(MockConfig.LIST_TEST.getNewTypeList(), myPlugin.config().get(MockConfig.LIST_TEST));
        myPlugin.config().set(MockConfig.LIST_TEST, Arrays.asList(25, 41));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        List<Integer> checkList = myPlugin.config().get(MockConfig.LIST_TEST);
        assertTrue(checkList instanceof LinkedList);
        assertTrue(checkList.contains(25) && checkList.contains(41));

        assertTrue(myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        myPlugin.config().get(MockConfig.NESTED_TEST).set(MockConfig.Nested.TEST, false);
        assertTrue(!myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        assertTrue(myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        myPlugin.config().get(MockConfig.NESTED_TEST).set(MockConfig.Nested.TEST, false);
        assertTrue(!myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        assertTrue(!myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
    }
}
