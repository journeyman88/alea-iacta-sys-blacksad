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
import net.unknowndomain.alea.dice.standard.D6;
import net.unknowndomain.alea.pools.DicePool;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.roll.GenericRoll;

/**
 *
 * @author journeyman
 */
public class BlacksadRoll implements GenericRoll
{
    
    private final DicePool<D6> actionPool;
    private final DicePool<D6> tensionPool;
    private final DicePool<D6> complimentaryPool;
    private final Set<BlacksadModifiers> mods;
    
    public BlacksadRoll(Integer action, Integer tension, BlacksadModifiers ... mod)
    {
        this(action, tension, Arrays.asList(mod));
    }
    
    public BlacksadRoll(Integer action, Integer tension, Collection<BlacksadModifiers> mod)
    {
        int a = 0, t = 0;
        if (action != null)
        {
            a = action;
        }
        if (tension != null)
        {
            t = tension;
        }
        this.mods = new HashSet<>();
        if (mod != null)
        {
            this.mods.addAll(mod);
        }
        this.actionPool = new DicePool<>(D6.INSTANCE, a);
        this.tensionPool = new DicePool<>(D6.INSTANCE, t);
        int complimentary = (action + tension) >= 6 ? 0 : 6 - a - t;
        this.complimentaryPool = new DicePool<>(D6.INSTANCE, complimentary);
    }
    
    @Override
    public GenericResult getResult()
    {
        List<Integer> actionRes = this.actionPool.getResults();
        List<Integer> tensionRes = this.tensionPool.getResults();
        List<Integer> complimentaryRes = this.complimentaryPool.getResults();
        BlacksadResults results = buildResults(actionRes, tensionRes, complimentaryRes);
        results.setVerbose(mods.contains(BlacksadModifiers.VERBOSE));
        return results;
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
