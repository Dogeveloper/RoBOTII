package com.robbertt.RoBOT2.config;

import jdk.nashorn.internal.runtime.regexp.joni.Config;

/**
 * Created by Robert on 11/10/2017.
 */
@org.aeonbits.owner.Config.LoadPolicy(org.aeonbits.owner.Config.LoadType.MERGE)
@org.aeonbits.owner.Config.Sources({"file:RoBOT2.properties"})
public interface BotConfiguration extends org.aeonbits.owner.Config {
    @org.aeonbits.owner.Config.Key("clientId")
    int clientId();
    @org.aeonbits.owner.Config.Key("token")
    String token();
    @org.aeonbits.owner.Config.DefaultValue(";")
    @org.aeonbits.owner.Config.Key("prefix")
    String prefix();
    @DefaultValue("RoBOT II Admin")
    String adminRole();
    @DefaultValue("RoBOT II Moderator")
    String modRole();
}
