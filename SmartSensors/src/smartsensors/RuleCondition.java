package smartsensors;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RuleCondition implements Serializable
{
    private String factorName;
    private Boolean isBigger;
    private float value;
    private Boolean previousEval;
    
    public RuleCondition(String fn, Boolean b, String v)
    {
        factorName = fn;
        isBigger = b;
        setValue(v);
        previousEval = false;
    }
    
    public String getFactorName() {
        return factorName;
    }

    public Boolean getBigger() {
        return isBigger;
    }
    
    public Boolean getPreviousEval() {
        if (factorName.contains("time"))
        {
            String currTime = new SimpleDateFormat("HH:mm").format(new Date());
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            try {cal.setTime(sdf.parse(currTime));}
            catch (ParseException ex) { ex.printStackTrace();}
            
            currTime = ""+cal.get(Calendar.HOUR)*60 + cal.get(Calendar.MINUTE);
            evaluateCondition(currTime);
        }           
        return previousEval;
    }
    
    public void setFactorName(String fn) {
        this.factorName = fn;
    }

    public void setBigger(Boolean b) {
        this.isBigger = b;
    }

    public void setValue(String value)
    {
        if (!factorName.equals("time"))
            this.value = Float.parseFloat(value);
        else
        {   // if condition factor is time, save value as minutes
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
            previousEval = (isBigger) ? (v > value) : (v < value);
            return previousEval;
        }
        catch (NumberFormatException e) {return previousEval = false;}
    }
    
    @Override
    public String toString()
    {
        return "( " + factorName + ((isBigger)?" > ":" < ") + value + " )";
    }
}
