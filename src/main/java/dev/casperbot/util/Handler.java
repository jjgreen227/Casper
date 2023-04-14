package dev.casperbot.util;

import net.dv8tion.jda.api.hooks.*;

public abstract class Handler extends ListenerAdapter {

    public abstract void handle();

    public abstract void clear();

}
