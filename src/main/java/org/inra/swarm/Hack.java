
package org.inra.swarm;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;
import javax.enterprise.inject.Produces;
import org.wildfly.swarm.spi.api.ArtifactLookup;
import org.wildfly.swarm.spi.api.SocketBindingGroup;
/**
 *
 * @author ryahiaoui
 */
@Provider
public class Hack {
    
    @Produces
    @Singleton
    ArtifactLookup artifactLookup ;
    
    @Produces
    @Named("standard-sockets")
    SocketBindingGroup bindingGroup ;
}
