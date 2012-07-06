Proxy certificate enabled HTTP server and Servlet container
===========================================================

Convenience library for getting a Jetty Server configured with an SSL
connector that supports proxy certificates (http://www.ietf.org/rfc/rfc3820.txt).

It uses the Java binding of the caNl library developed in the EMI project (http://www.eu-emi.eu/).

Install
-------

This is not yet on a Maven repository. You have to compile and install locally

    mvn install
    
before you can reference it in your Maven projects

    <dependency>
      <groupId>org.italiangrid</groupId>
      <artifactId>https-utils</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>    

Usage
-----

Create an SSLOption object providing the location of the service credentials, 
and the directory containing the trusted certification authorities 

    SSLOptions options = new SSLOptions();
    options.setCertificateFile("/etc/grid-security/hostcert.pem");
    options.setKeyFile("/etc/grid-security/hostkey.pem");
    options.setTrustStoreDirectory("/etc/grid-security/certificates");
    
Then get an org.eclipse.jetty.server.Server object and use it as usual
    
    Server server = ServerFactory.newServer("localhost", 443, options);
    
    s.setHandler(new HelloHandler());
    
    s.start();
    s.join();   

The SSLOption class also provides methods for configuring whether client authentication 
must be requested or required

    options.setWantClientAuth(wantClientAuth)
    options.setNeedClientAuth(needClientAuth)

and for setting the time interval for refreshing the certification authorities information

    options.setTrustStoreRefreshIntervalInMsec(60000L);

