package smartsensors;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RuleCondition implements Serializable
{
    private String factorName;
    private Boolean isBigger;
    private float value;
    
    private Boolean isSensor;
    private Boolean currentEval;
    
    public RuleCondition(String fn, Boolean b, float v)
    {
        factorName = fn;
        isBigger = b;
        value = v;
        isSensor = (fn.equals("time"));
        currentEval = false;
    }
    
    public String getFactorName() {
        return factorName;
    }

    public Boolean getBigger() {
        return isBigger;
    }

    public float getValue() {
        return value;
    }
    
    public Boolean getPreviousEval() {
        return currentEval;
    }
    
    public void setFactorName(String fn) {
        this.factorName = fn;
        isSensor = (fn.equals("time"));
    }

    public void setBigger(Boolean b) {
        this.isBigger = b;
    }

    public void setValue(String value) {
        if (isSensor)
            this.value = Float.parseFloat(value);
        else
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            try {cal.setTime(sdf.parse(value));}
            catch (ParseException ex) { ex.printStackTrace();}
            
            this.value = cal.get(Calendar.HOUR)*60 + cal.get(Calendar.MINUTE);
        }
    }
    
    public boolean evaluateCondition(String inValue)
    {
        try
        {
            float v = Float.parseFloat(inValue);
            currentEval = (isBigger) ? (v > value) : (v < value);
            return currentEval;
        }
        catch (NumberFormatException e) {return currentEval;}
    }
    
    @Override
    public String toString()
    {
        return factorName + ((isBigger)?" > ":" < ") + value;
    }
}
