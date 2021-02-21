/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unknowndomain.alea.systems.blacksad;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import net.unknowndomain.alea.systems.annotations.RpgSystemData;
import net.unknowndomain.alea.systems.annotations.RpgSystemOption;


/**
 *
 * @author journeyman
 */
@RpgSystemData(bundleName = "net.unknowndomain.alea.systems.blacksad.RpgSystemBundle")
public class BlacksadOptions extends RpgSystemOptions
{
    @RpgSystemOption(name = "action", shortcode = "a", description = "blacksad.options.action", argName = "actionDice")
    private Integer action;
    @RpgSystemOption(name = "tension", shortcode = "t", description = "blacksad.options.tension", argName = "tensionDice")
    private Integer tension;
                        
    @Override
    public boolean isValid()
    {
        return !(isHelp());
    }

    public Integer getAction()
    {
        return action;
    }

    public Integer getTension()
    {
        return tension;
    }

    public Collection<BlacksadModifiers> getModifiers()
    {
        Set<BlacksadModifiers> mods = new HashSet<>();
        if (isVerbose())
        {
            mods.add(BlacksadModifiers.VERBOSE);
        }
        return mods;
    }
    
}