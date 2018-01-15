package com.robbertt.RoBOT2;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Robert on 11/10/2017.
 */
public class ArgumentValidator {

    private Function<String, Boolean> validation;
    private boolean optional;
    private String commandName;

    public ArgumentValidator(String commandName, boolean optional, Function<String, Boolean> validation) {
        this.validation = validation;
        this.optional = optional;
        this.commandName = commandName;
    }

    public boolean validate(String s) {
        return validation.apply(s);
    }

    public boolean isOptional() {
        return optional;
    }

    public  String getCommandName() {
        return commandName;
    }
}
