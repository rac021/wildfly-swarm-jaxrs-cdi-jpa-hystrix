
package org.inra.swarm;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author ryahiaoui
 */

@Entity
public class MyEntity implements Serializable {
  
    @Id
    @GeneratedValue
    private Long id ;
    
    private final String name ;

    public MyEntity() {
        this.name = " From Entity ... " ;
    }

    public String getName() {
        return name ;
    }
    
}
