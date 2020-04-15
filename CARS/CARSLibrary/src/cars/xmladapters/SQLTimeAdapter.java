/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cars.xmladapters;

import java.sql.Time;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author gem
 */
public class SQLTimeAdapter extends XmlAdapter<String, Time> {

    private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    @Override
    public String marshal(Time v) throws Exception {
        return sdf2.format(v);
    }
    
    @Override
    public Time unmarshal(String v) throws Exception {
        return new Time(sdf2.parse(v).getTime());
    }

    
    
}
