package com.froobworld.archivesuite.config;

import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.nabconfiguration.ConfigEntries;
import com.froobworld.nabconfiguration.ConfigEntry;
import com.froobworld.nabconfiguration.ConfigEntryMap;
import com.froobworld.nabconfiguration.NabConfiguration;
import com.froobworld.nabconfiguration.annotations.Entry;
import com.froobworld.nabconfiguration.annotations.EntryMap;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public class ArchiveSuiteConfig extends NabConfiguration {
    public static final int VERSION = 1;


    public ArchiveSuiteConfig(ArchiveSuite archiveSuite) {
        super(
                new File(archiveSuite.getDataFolder(), "config.yml"),
                () -> archiveSuite.getResource("resources/config.yml"),
                i -> archiveSuite.getResource("resources/config-patches/" + i + ".patch"),
                VERSION
        );
    }

    @Entry(key = "enabled-maps")
    public final ConfigEntry<List<String>> enabledMaps = ConfigEntries.stringListEntry();

    @EntryMap(key = "map-blurb", defaultKey = "default")
    public final ConfigEntryMap<String, List<String>> mapBlurb = new ConfigEntryMap<>(Function.identity(), ConfigEntries::stringListEntry, true);

    @Entry(key = "motd")
    public final ConfigEntry<List<String>> motd = ConfigEntries.stringListEntry();

}
