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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.unknowndomain.alea.messages.MsgBuilder;
import net.unknowndomain.alea.roll.GenericResult;

/**
 *
 * @author journeyman
 */
public class BlacksadResults extends GenericResult
{
    private final List<Integer> actionResults;
    private final List<Integer> tensionResults;
    private final List<Integer> complimentaryResults;
    private int successes = 0;
    
    public BlacksadResults(List<Integer> actionResults, List<Integer> tensionResults, List<Integer> complimentaryResults)
    {
        List<Integer> tmp = new ArrayList<>(actionResults.size());
        tmp.addAll(actionResults);
        this.actionResults = Collections.unmodifiableList(tmp);
        tmp = new ArrayList<>(tensionResults.size());
        tmp.addAll(tensionResults);
        this.tensionResults = Collections.unmodifiableList(tmp);
        tmp = new ArrayList<>(complimentaryResults.size());
        tmp.addAll(complimentaryResults);
        this.complimentaryResults = Collections.unmodifiableList(tmp);
    }
    
    public void addSuccess()
    {
        successes++;
    }

    public int getSuccesses()
    {
        return successes;
    }

    public List<Integer> getComplimentaryResults()
    {
        return complimentaryResults;
    }

    public List<Integer> getActionResults()
    {
        return actionResults;
    }

    public List<Integer> getTensionResults()
    {
        return tensionResults;
    }

    @Override
    protected void formatResults(MsgBuilder messageBuilder, boolean verbose, int indentValue)
    {
        String indent = getIndent(indentValue);
        messageBuilder.append("Successes: ").append(getSuccesses()).appendNewLine();
        if (verbose)
        {
            messageBuilder.append(indent).append("Roll ID: ").append(getUuid()).appendNewLine();
            if (!getActionResults().isEmpty())
            {
                messageBuilder.append("Action Dice Results: ").append(" [ ");
                for (Integer t : getActionResults())
                {
                    messageBuilder.append(t).append(" ");
                }
                messageBuilder.append("]").appendNewLine();
            }
            if (!getTensionResults().isEmpty())
            {
                messageBuilder.append("Tension Dice Results: ").append(" [ ");
                for (Integer t : getTensionResults())
                {
                    messageBuilder.append(t).append(" ");
                }
                messageBuilder.append("]").appendNewLine();
            }
            if (!getComplimentaryResults().isEmpty())
            {
                messageBuilder.append("Complimentary Dice Results: ").append(" [ ");
                for (Integer t : getComplimentaryResults())
                {
                    messageBuilder.append(t).append(" ");
                }
                messageBuilder.append("]").appendNewLine();
            }
        }
    }

}
