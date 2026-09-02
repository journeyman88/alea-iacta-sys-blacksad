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
import java.util.Locale;
import java.util.Set;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.random.SingleResultComparator;
import net.unknowndomain.alea.random.dice.DicePool;
import net.unknowndomain.alea.random.dice.bag.D6;
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
    private final Locale lang;
    
    public BlacksadRoll(Integer action, Integer tension, Locale lang, BlacksadModifiers ... mod)
    {
        this(action, tension, lang, Arrays.asList(mod));
    }
    
    public BlacksadRoll(Integer action, Integer tension, Locale lang, Collection<BlacksadModifiers> mod)
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
        this.lang = lang;
    }
    
    @Override
    public GenericResult getResult()
    {
        List<SingleResult<Integer>> actionRes = this.actionPool.getResults();
        List<SingleResult<Integer>> tensionRes = this.tensionPool.getResults();
        List<SingleResult<Integer>> complimentaryRes = this.complimentaryPool.getResults();
        BlacksadResults results = buildResults(actionRes, tensionRes, complimentaryRes);
        results.setVerbose(mods.contains(BlacksadModifiers.VERBOSE));
        results.setLang(lang);
        return results;
    }
    
    private BlacksadResults buildResults(List<SingleResult<Integer>> actionRes, List<SingleResult<Integer>> tensionRes, List<SingleResult<Integer>> complimentaryRes)
    {
        SingleResultComparator<Integer> comp = new SingleResultComparator(true);
        actionRes.sort(comp);
        tensionRes.sort(comp);
        actionRes.sort(comp);
        BlacksadResults results = new BlacksadResults(actionRes, tensionRes, complimentaryRes);
        int skipDice = 0;
        for (SingleResult<Integer> t : tensionRes)
        {
            if (t.getValue() == 1)
            {
                skipDice++;
            }
        }
        for (SingleResult<Integer> c : complimentaryRes)
        {
            if (c.getValue() == 1)
            {
                skipDice++;
            }
        }
        for (SingleResult<Integer> a : actionRes)
        {
            if (a.getValue() >= 4)
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
        for (SingleResult<Integer> t : tensionRes)
        {
            if (t.getValue() >= 4)
            {
                if (skipDice <= 0)
                {
                    results.addSuccess();
                    if (t.getValue() >= 6)
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
        for (SingleResult<Integer> c : complimentaryRes)
        {
            if (c.getValue() >= 6)
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
