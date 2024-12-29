package com.bre.namemanager.client;

import com.bre.namemanager.namemanage.NameManager;
import net.fabricmc.api.ClientModInitializer;

public class NamemanagerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NameManager.loadNameLogs();
    }
}
