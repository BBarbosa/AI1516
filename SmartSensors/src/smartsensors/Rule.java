package smartsensors;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Rule implements Serializable
{
    private Boolean active;
    private Boolean on;
    private HashMap<String,RuleCondition> conditions;
    private String onString;
    private String offString;

    public Rule(Boolean a, HashMap<String,RuleCondition> c, String oS, String offS)
    {
        active = a;
        on = false;
        conditions = new HashMap<>();
        for (Entry<String,RuleCondition> e : c.entrySet())
            conditions.put(e.getKey(), e.getValue());
        onString = oS;
        offString = offS;
    }

    public Boolean getActive() {
        return active;
    }

    public String getOnString() {
        return onString;
    }

    public String getOffString() {
        return offString;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public void setOnString(String onString) {
        this.onString = onString;
    }

    public void setOffString(String offString) {
        this.offString = offString;
    }

    public Set<String> getRuleSensors()
    {
        return conditions.keySet();
    }

    public String evaluateRule(String sensorName, String inValue)
    {
        // update and evaluate ruleCondition
        System.out.println(sensorName);
        if (sensorName != null)
        {
            if (!conditions.get(sensorName).evaluateCondition(inValue) && on)
            {
                on = false;
                return offString;
            }
            else
            {// check other RuleConditions
                for (RuleCondition rc : conditions.values())
                    if (!rc.getPreviousEval())
                        return null;

                if (!on)
                {
                    on = true;
                    return onString;
                }
            }
        }
        return null;
    }

    public String ruleString()
    {
        String s = null;

        Collection<RuleCondition> rcCol = conditions.values();
        RuleCondition[] rcs = rcCol.toArray(new RuleCondition[rcCol.size()]);

        for (int i = 0; i < rcs.length - 1; i++)
            s += rcs[i].toString() + " AND ";

        s += rcs[rcs.length - 1].toString() + " -> " + onString;

        return s;
    }
}
