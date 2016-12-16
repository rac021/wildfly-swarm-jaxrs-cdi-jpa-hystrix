
package org.inra.swarm;

import java.util.Set;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;


/**
 *
 * @author ryahiaoui
 */
@Provider
public class Discover {
    

@Inject
BeanManager beanManager;

    public void method() {
        
        Set<Bean<?>> beans = beanManager.getBeans(Object.class,new AnnotationLiteral<Any>() {});
        for (Bean<?> bean : beans) {
            System.out.println( "++++ Injected Bean ---> " + bean.getBeanClass().getName()) ;
        }

    }
    
}
