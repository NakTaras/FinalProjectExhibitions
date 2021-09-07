package com.my.command;

import com.my.command.impl.*;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private static Map<String, Command> commands;

    static {
        commands = new HashMap<>();

        commands.put("logIn", new LogInCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("logOut", new LogOutCommand());
        commands.put("addExhibition", new AddExhibitionCommand());
        commands.put("addLocation", new AddLocationCommand());
        commands.put("getLocations", new GetLocationCommand());
        commands.put("getExhibitions", new GetExhibitionsCommand());
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }

}
