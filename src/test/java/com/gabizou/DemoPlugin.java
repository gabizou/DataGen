package com.gabizou;

import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.persistence.SerializationService;

@Plugin(id = "DemoDataTest", name = "DemoDataTest")
public class DemoPlugin {

    public static Game getGame() {
        return instance.demoPlugin.game;
    }

    private static Holder instance;

    @Inject private Game game;

    @Listener
    public void onPreInit(GameInitializationEvent event) {
        instance = new Holder();
        instance.setPlugin(this);
        final SerializationService service = this.game.getServiceManager().provide(SerializationService.class).get();
        service.registerBuilder(DemoTestData.class, new DemoDataBuilder());
        service.registerBuilder(ImmutableDemoTestData.class, new ImmutableDemoDataBuilder());

    }

    private static class Holder {
        private DemoPlugin demoPlugin;

        private void setPlugin(DemoPlugin plugin) {
            this.demoPlugin = plugin;
        }

    }

}
