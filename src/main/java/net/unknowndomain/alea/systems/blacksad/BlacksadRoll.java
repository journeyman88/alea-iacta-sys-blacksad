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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.unknowndomain.alea.dice.D6;
import net.unknowndomain.alea.pools.DicePool;
import net.unknowndomain.alea.roll.GenericRoll;
import org.javacord.api.entity.message.MessageBuilder;

/**
 *
 * @author journeyman
 */
public class BlacksadRoll implements GenericRoll
{
    
    public enum Modifiers
    {
        VERBOSE
    }
    
    private final DicePool<D6> actionPool;
    private final DicePool<D6> tensionPool;
    private final DicePool<D6> complimentaryPool;
    private final Set<Modifiers> mods;
    
    public BlacksadRoll(Integer action, Integer tension, Modifiers ... mod)
    {
        this(action, tension, Arrays.asList(mod));
    }
    
    public BlacksadRoll(Integer action, Integer tension, Collection<Modifiers> mod)
    {
        this.mods = new HashSet<>();
        this.actionPool = new DicePool<>(D6.INSTANCE, action);
        this.tensionPool = new DicePool<>(D6.INSTANCE, tension);
        int complimentary = (action + tension) >= 6 ? 0 : 6 - action - tension;
        this.complimentaryPool = new DicePool<>(D6.INSTANCE, complimentary);
    }
    
    @Override
    public MessageBuilder getResult()
    {
        List<Integer> actionRes = this.actionPool.getResults();
        List<Integer> tensionRes = this.tensionPool.getResults();
        List<Integer> complimentaryRes = this.complimentaryPool.getResults();
        BlacksadResults results = buildResults(actionRes, tensionRes, complimentaryRes);
        return formatResults(results);
    }
    
    private MessageBuilder formatResults(BlacksadResults results)
    {
        MessageBuilder mb = new MessageBuilder();
        mb.append("Successes: ").append(results.getSuccesses()).appendNewLine();
        if (mods.contains(Modifiers.VERBOSE))
        {
            if (!results.getActionResults().isEmpty())
            {
                mb.append("Action Dice Results: ").append(" [ ");
                for (Integer t : results.getActionResults())
                {
                    mb.append(t).append(" ");
                }
                mb.append("]").appendNewLine();
            }
            if (!results.getTensionResults().isEmpty())
            {
                mb.append("Tension Dice Results: ").append(" [ ");
                for (Integer t : results.getTensionResults())
                {
                    mb.append(t).append(" ");
                }
                mb.append("]").appendNewLine();
            }
            if (!results.getComplimentaryResults().isEmpty())
            {
                mb.append("Complimentary Dice Results: ").append(" [ ");
                for (Integer t : results.getComplimentaryResults())
                {
                    mb.append(t).append(" ");
                }
                mb.append("]").appendNewLine();
            }
        }
        return mb;
    }
    
    private BlacksadResults buildResults(List<Integer> actionRes, List<Integer> tensionRes, List<Integer> complimentaryRes)
    {
        actionRes.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        tensionRes.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        actionRes.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        BlacksadResults results = new BlacksadResults(actionRes, tensionRes, complimentaryRes);
        int skipDice = 0;
        for (Integer t : tensionRes)
        {
            if (t == 1)
            {
                skipDice++;
            }
        }
        for (Integer c : complimentaryRes)
        {
            if (c == 1)
            {
                skipDice++;
            }
        }
        for (Integer a : actionRes)
        {
            if (a >= 4)
            {
                if (skipDice <= 0)
                {
                    results.addSuccess();
                }
                else
                {
                    skipDice--;
                }
            }
        }
        for (Integer t : tensionRes)
        {
            if (t >= 4)
            {
                if (skipDice <= 0)
                {
                    results.addSuccess();
                    if (t >= 6)
                    {
                        results.addSuccess();
                    }
                }
                else
                {
                    skipDice--;
                }
            }
        }
        for (Integer c : complimentaryRes)
        {
            if (c >= 6)
            {
                if (skipDice <= 0)
                {
                    results.addSuccess();
                }
                else
                {
                    skipDice--;
                }
            }
        }
        return results;
    }
}
