
package org.inra.swarm;

import java.io.Serializable;



/**
 *
 * @author ryahiaoui
 */


public class MyPojo implements Serializable {
  
    private final String name ;

    public MyPojo() {
        this.name = " From Pojo .. " ;
    }

    public String getName() {
        return name;
    }
    
}
