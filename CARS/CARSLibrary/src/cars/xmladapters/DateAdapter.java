/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cars.xmladapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author gem
 */
public class DateAdapter extends XmlAdapter<String, Date>{
    
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String marshal(Date v) throws Exception {
        return sdf2.format(v);
    }
    
    @Override
    public Date unmarshal(String v) throws Exception {
        Date date = sdf2.parse(v);
        return date;
    }

    
    
    
}
