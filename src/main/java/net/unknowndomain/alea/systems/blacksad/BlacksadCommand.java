/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.blacksad;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import net.unknowndomain.alea.command.HelpWrapper;
import net.unknowndomain.alea.expr.Expression;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.roll.GenericRoll;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.javacord.api.entity.message.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class BlacksadCommand extends RpgSystemCommand
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BlacksadCommand.class);
    private static final RpgSystemDescriptor DESC = new RpgSystemDescriptor("Blacksad RPG", "sad", "blacksad");
    
    private static final String ACTION_PARAM = "ability";
    private static final String TENSION_PARAM = "tension";
    
    private static final Options CMD_OPTIONS;
    
    static {
        
        CMD_OPTIONS = new Options();
        CMD_OPTIONS.addOption(
                Option.builder("a")
                        .longOpt(ACTION_PARAM)
                        .desc("Number of action dice to roll")
                        .hasArg()
                        .argName("actionDice")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("t")
                        .longOpt(TENSION_PARAM)
                        .desc("Number of tension dice to roll")
                        .hasArg()
                        .argName("potentialValue")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("h")
                        .longOpt( CMD_HELP )
                        .desc( "Print help")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("v")
                        .longOpt(CMD_VERBOSE)
                        .desc("Enable verbose output")
                        .build()
        );
    }
    
    public BlacksadCommand()
    {
        
    }
    
    @Override
    public RpgSystemDescriptor getCommandDesc()
    {
        return DESC;
    }
    
    @Override
    public MessageBuilder execCommand(String cmdLine)
    {
        MessageBuilder retVal = new MessageBuilder();
        Matcher prefixMatcher = PREFIX.matcher(cmdLine);
        if (prefixMatcher.matches())
        {
            String params = prefixMatcher.group(CMD_PARAMS);
            if (params == null || params.isEmpty())
            {
                return HelpWrapper.printHelp(prefixMatcher.group(CMD_NAME), CMD_OPTIONS, true);
            }
            LOGGER.debug(cmdLine);
            try
            {
                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = parser.parse(CMD_OPTIONS, params.split(" "));
                
                if (
                        cmd.hasOption(CMD_HELP)
                    )
                {
                    return HelpWrapper.printHelp(prefixMatcher.group(CMD_NAME), CMD_OPTIONS, true);
                }
                
                
                Set<BlacksadRoll.Modifiers> mods = new HashSet<>();
                
                int a = 0, t = 0;
                if (cmd.hasOption(CMD_VERBOSE))
                {
                    mods.add(BlacksadRoll.Modifiers.VERBOSE);
                }
                if (cmd.hasOption(TENSION_PARAM))
                {
                    t = Integer.parseInt(cmd.getOptionValue(TENSION_PARAM));
                }
                if (cmd.hasOption(ACTION_PARAM))
                {
                    a = Integer.parseInt(cmd.getOptionValue(ACTION_PARAM));
                }
                GenericRoll roll = new BlacksadRoll(a, t, mods);
                retVal = roll.getResult();
            } 
            catch (ParseException | NumberFormatException ex)
            {
                retVal = HelpWrapper.printHelp(prefixMatcher.group(CMD_NAME), CMD_OPTIONS, true);
            }
        }
        return retVal;
    }
    
}
