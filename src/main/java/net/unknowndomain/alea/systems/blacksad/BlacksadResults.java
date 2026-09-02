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
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.roll.LocalizedResult;

/**
 *
 * @author journeyman
 */
public class BlacksadResults extends LocalizedResult
{
    private final static String BUNDLE_NAME = "net.unknowndomain.alea.systems.blacksad.RpgSystemBundle";
    
    private final List<SingleResult<Integer>> actionResults;
    private final List<SingleResult<Integer>> tensionResults;
    private final List<SingleResult<Integer>> complimentaryResults;
    private int successes = 0;
    
    public BlacksadResults(List<SingleResult<Integer>> actionResults, List<SingleResult<Integer>> tensionResults, List<SingleResult<Integer>> complimentaryResults)
    {
        List<SingleResult<Integer>> tmp = new ArrayList<>(actionResults.size());
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

    public List<SingleResult<Integer>> getComplimentaryResults()
    {
        return complimentaryResults;
    }

    public List<SingleResult<Integer>> getActionResults()
    {
        return actionResults;
    }

    public List<SingleResult<Integer>> getTensionResults()
    {
        return tensionResults;
    }

    @Override
    protected void formatResults(MsgBuilder messageBuilder, boolean verbose, int indentValue)
    {
        String indent = getIndent(indentValue);
        messageBuilder.append(translate("blacksad.results.successes", getSuccesses())).appendNewLine();
        if (verbose)
        {
            messageBuilder.append(indent).append("Roll ID: ").append(getUuid()).appendNewLine();
            if (!getActionResults().isEmpty())
            {
                messageBuilder.append(translate("blacksad.results.actionDice")).append(" [ ");
                for (SingleResult<Integer> t : getActionResults())
                {
                    messageBuilder.append("( ").append(t.getLabel()).append(" => ");
                    messageBuilder.append(t.getValue()).append(") ");
                }
                messageBuilder.append("]").appendNewLine();
            }
            if (!getTensionResults().isEmpty())
            {
                messageBuilder.append(translate("blacksad.results.tensionDice")).append(" [ ");
                for (SingleResult<Integer> t : getTensionResults())
                {
                    messageBuilder.append("( ").append(t.getLabel()).append(" => ");
                    messageBuilder.append(t.getValue()).append(") ");
                }
                messageBuilder.append("]").appendNewLine();
            }
            if (!getComplimentaryResults().isEmpty())
            {
                messageBuilder.append(translate("blacksad.results.complimentaryDice")).append(" [ ");
                for (SingleResult<Integer> t : getComplimentaryResults())
                {
                    messageBuilder.append("( ").append(t.getLabel()).append(" => ");
                    messageBuilder.append(t.getValue()).append(") ");
                }
                messageBuilder.append("]").appendNewLine();
            }
        }
    }

    @Override
    protected String getBundleName()
    {
        return BUNDLE_NAME;
    }

}
