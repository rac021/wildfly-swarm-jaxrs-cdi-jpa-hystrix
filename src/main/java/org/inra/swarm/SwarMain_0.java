package org.inra.swarm ;

/**
 *
 * @author ryahiaoui
 */

import org.wildfly.swarm.Swarm;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.management.ManagementFraction;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.datasources.DatasourcesFraction;

// import org.jboss.shrinkwrap.api.asset.StringAsset;
// import org.wildfly.swarm.management.console.ManagementConsoleFraction;

public class SwarMain_0 {

    public static void main(String[] args) throws Exception {

        Swarm swarm = new Swarm();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "my-app.war");

        deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", SwarMain_0.class.getClassLoader()), "classes/META-INF/persistence.xml");
        deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/beans.xml" , SwarMain_0.class.getClassLoader()), "beans.xml" ) ;

        deployment.addClass(HelloWorldEndpoint.class);
        deployment.addClass(MyEntity.class);
        deployment.addClass(Hack.class);
        deployment.addClass(RestApplication.class);

        ManagementFraction securityRealm = ManagementFraction.createDefaultFraction()
                .httpInterfaceManagementInterface((iface) -> {
                    iface.allowedOrigin("http://localhost:8080");
                    iface.securityRealm("ManagementRealm");
                })
                .securityRealm("ManagementRealm", (realm) -> {
                    realm.inMemoryAuthentication((authn) -> {
                        authn.add("rya", "rac021", true);
                    });
                    realm.inMemoryAuthorization((authz) -> {
                        authz.add("rya", "admin");
                    });
                });

        deployment.addAllDependencies();

        swarm.fraction(securityRealm);

       /* 
       deployment.addAsWebInfResource( new StringAsset(
        "<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        " xsi:schemaLocation=\"\n" +
        " http://xmlns.jcp.org/xml/ns/javaee\n" +
        " http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
        "</beans>"), "beans.xml" 
       ) ;
       */
       
       /*
        swarm.fraction(new ManagementConsoleFraction()
             .contextRoot("/general-console")
        );
       */
       
        swarm.fraction(new DatasourcesFraction()
                .jdbcDriver("org.postgresql",
                        (d) -> {
                            d.driverClassName("org.postgresql.Driver");
                            d.xaDatasourceClass("org.postgresql.xa.PGXADataSource");
                            d.driverModuleName("org.postgresql");
                        }).
                dataSource("MyPU",
                        (ds) -> {
                            ds.driverName("org.postgresql");
                            ds.connectionUrl("jdbc:postgresql://127.0.0.1/ola?sendBufferSize=5000");
                            ds.userName("ryahiaoui");
                            ds.password("yahiaoui");
                            ds.jndiName("java:jboss/datasources/Scheduler");
                        })).
                start().
                deploy(deployment);
        ;
    }

}
