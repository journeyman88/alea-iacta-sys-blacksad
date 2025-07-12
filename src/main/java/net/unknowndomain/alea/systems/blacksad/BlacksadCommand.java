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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Locale;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.unknowndomain.alea.registry.HistoryRegistry;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.roll.GenericRoll;
import net.unknowndomain.alea.systems.RpgSystemOptions;

/**
 *
 * @author journeyman
 */
@Named
@ApplicationScoped
@Slf4j
@NoArgsConstructor
public class BlacksadCommand extends RpgSystemCommand
{
    private static final RpgSystemDescriptor DESC = new RpgSystemDescriptor("Blacksad RPG", "sad", "blacksad");
    
    @Inject
    private Instance<HistoryRegistry> historyRegistry;

    @Override
    protected Optional<HistoryRegistry> getHistoryRegistry() {
        return historyRegistry.stream().findFirst();
    }
    
    @Override
    public RpgSystemDescriptor getCommandDesc()
    {
        return DESC;
    }

    @Override
    protected Optional<GenericRoll> safeCommand(RpgSystemOptions options, Locale lang)
    {
        Optional<GenericRoll> retVal;
        if (options.isHelp() || !(options instanceof BlacksadOptions) )
        {
            retVal = Optional.empty();
        }
        else
        {
            BlacksadOptions opt = (BlacksadOptions) options;
            BlacksadRoll roll = new BlacksadRoll(opt.getAction(), opt.getTension(), lang, opt.getModifiers());
            retVal = Optional.of(roll);
        }
        return retVal;
    }

    @Override
    public RpgSystemOptions buildOptions()
    {
        return new BlacksadOptions();
    }
    
}
