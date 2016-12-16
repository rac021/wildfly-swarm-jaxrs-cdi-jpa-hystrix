
package org.inra.swarm;

import javax.ws.rs.GET;
import org.slf4j.Logger;
import javax.ws.rs.Path;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;
import javax.ws.rs.core.GenericType;
import javax.persistence.EntityManager;
import javax.ws.rs.client.ClientBuilder;
import com.netflix.hystrix.HystrixCommand;
import javax.persistence.PersistenceContext;
import java.util.concurrent.ExecutionException;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommandGroupKey;



@Path("/hello")
@Stateless
public class HelloWorldEndpoint {

  @Inject 
  MyEntity myEntity ;
  
  @Inject 
  MyPojo myPojo ;
    
  @Inject
  Discover discover ;
          
  @PersistenceContext  (unitName = "MyPU")
  private EntityManager entityManager;
    
  @GET
  @Produces("text/plain")
  @Path("/endpoint")
  public Response doGet(  @QueryParam("url") String url, @QueryParam("timeOut") int timeOut ) {
  
      discover.method() ; 
      
      if( url == null ) {
        return Response.ok(" Hello from WildFly Swarm Demo ! ---> " + myEntity.getName() + " - " + 
                                                                      myPojo.getName() +  " - em -> " + 
                                                                      entityManager.getClass().toString() ).build() ;
      }
      
      String data = new CallEndPointCommand(url, timeOut).execute() ;
      return Response.ok(data ).build() ;
      
   }
  

  class CallEndPointCommand extends HystrixCommand<String> {
      
        /* Default URL */
        String url = "http://localhost:8080/rest/hello/endpoint" ;
        
        Integer timeOut ;
        
        final Logger log = LoggerFactory.getLogger(CallEndPointCommand.class);
        
	public CallEndPointCommand( String url, Integer timeOut) {
	
            super(HystrixCommandGroupKey.Factory.asKey("endPoitnData")) ;
                        
            /* Programmatic Configuration */
           
            /*
            ConfigurationManager.getConfigInstance() .setProperty
            ("hystrix.command.CallEndPointCommand.circuitBreaker.requestVolumeThreshold", 30) ;

            ConfigurationManager.getConfigInstance().setProperty
            ("hystrix.command.CallEndPointCommand.execution.isolation.thread.timeoutInMilliseconds", 200) ;
                        
            ConfigurationManager.getConfigInstance().setProperty
            ("hystrix.threadpool.default.coreSize", 8) ;
 
            ConfigurationManager.getConfigInstance().setProperty
            ("hystrix.command.default.metrics.rollingPercentile.numBuckets", 60) ;
            
            */
                       
            if(timeOut != null ) {
                System.out.println(" Overriding timeoutInMilliseconds... ") ;
                ConfigurationManager.getConfigInstance().setProperty
                ("hystrix.command.CallEndPointCommand.execution.isolation.thread.timeoutInMilliseconds", timeOut ) ;
                  
            }
            
             Integer defTimeOut = ConfigurationManager.getConfigInstance().getInt
                                  ("hystrix.command.CallEndPointCommand.execution.isolation.thread.timeoutInMilliseconds") ;
 
            System.out.println(" ++ timeOut for CallEndPointCommand = " + defTimeOut ) ;
           
            this.url = url         ;
            this.timeOut = timeOut ;
            
        }

        
	@Override
	protected String run() throws Exception {
		
         try {

               Future<String> data = ClientBuilder.newClient()
                                                  .target(url)
                                                  .request()
                                                  .async()
                                                  .get( new GenericType<String>(){}) ;
              return data.get() ;
	      
	      /* 
	       Builder request = ClientBuilder.newClient().target(url).request() ;
               return request.get(new GenericType<String>(){}) ;
               */
               
	 } catch ( InterruptedException | ExecutionException e ) {
              throw new RuntimeException(" Oops, Something went wrong ! ") ;
	   }
	}
        
  
        @Override
        protected String getFallback() {
            /* log.error("Fallback {}", this.getFailedExecutionException().getMessage()); */
            return " Client   :  " + url +"   is unreachable !! \n"
                 + " Reason   :  TimeoutException " ;
        }
  }

}

