package com.andersenlab.carservice.application.command;

import com.andersenlab.carservice.port.usecase.ListRepairersUserCase;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;

public final class ListRepairersCommand extends NamedCommandWithDescription {

    private final ListRepairersUserCase userCase;

    public ListRepairersCommand(ListRepairersUserCase userCase) {
        super("list");
        this.userCase = userCase;
    }

    @Override
    List<String> expectedArguments() {
        return List.of("sort");
    }

    @Override
    String desription() {
        return "list all known repairers sorted";
    }

    @Override
    void executeIfMatched(PrintStream output, List<String> arguments) {
        var repairers = userCase.list(
                ListRepairersUserCase.Sort.valueOf(
                        arguments.get(0).toUpperCase(Locale.ROOT)
                )
        );
        for (int i = 0; i < repairers.size(); i++) {
            var repairer = repairers.get(i);
            output.printf("%d) %s, %s%n", i + 1, repairer.id(), repairer.name());
        }
    }
}