package io.github.wysohn.triggerreactor.sponge.manager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.github.wysohn.triggerreactor.core.main.TriggerReactor;
import io.github.wysohn.triggerreactor.core.manager.AbstractPlaceholderManager;
import io.github.wysohn.triggerreactor.tools.JarUtil;
import io.github.wysohn.triggerreactor.tools.JarUtil.CopyOption;

public class PlaceholderManager extends AbstractPlaceholderManager implements SpongeScriptEngineInitializer {
    private static final String JAR_FOLDER_LOCATION = "assets"+JarUtil.JAR_SEPARATOR+
            "triggerreactor"+JarUtil.JAR_SEPARATOR+"Placeholder"+JarUtil.JAR_SEPARATOR+"Sponge";

    private File placeholderFolder;

    public PlaceholderManager(TriggerReactor plugin) throws ScriptException, IOException {
        super(plugin);
        this.placeholderFolder = new File(plugin.getDataFolder(), JAR_FOLDER_LOCATION);
        JarUtil.copyFolderFromJar(JAR_FOLDER_LOCATION, plugin.getDataFolder(), CopyOption.REPLACE_IF_EXIST, (original) -> {
            return original.substring(0, original.indexOf("!"+JarUtil.JAR_SEPARATOR)).replace("."+JarUtil.JAR_SEPARATOR, "");
        });

        reload();
    }

    @Override
    public void reload() {
        FileFilter filter = new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".js");
            }
        };

        jsPlaceholders.clear();
        for(File file : placeholderFolder.listFiles(filter)){
            try {
                reloadPlaceholders(file, filter);
            } catch (ScriptException | IOException e) {
                e.printStackTrace();
                plugin.getLogger().warning("Could not load placeholder "+file.getName());
                continue;
            }
        }
    }

    @Override
    public void saveAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initScriptEngine(ScriptEngineManager sem) throws ScriptException {
        super.initScriptEngine(sem);
        SpongeScriptEngineInitializer.super.initScriptEngine(sem);
    }

}
