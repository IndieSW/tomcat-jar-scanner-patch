package com.tmbsw.jar_scanner_bugzilla_59862;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.catalina.startup.Tomcat;
import org.apache.juli.OneLineFormatter;

public class Main {

    private static final String JARS_TO_SKIP_NAME =
        "tomcat.util.scan.StandardJarScanFilter.jarsToSkip";

    private static final String JARS_TO_SKIP_VALUE =
        "tomcat-embed-core-*.jar,"
    	+ "tomcat-embed-el-*.jar,"
        + "tomcat-embed-logging-juli-*.jar,"
    	+ "tomcat-embed-jasper-*.jar,"
        + "ecj-*.jar";


	public static void main( String[] args ) throws Exception {
		ConsoleHandler consoleHandler;
        Logger tldLogger;
        Logger jarLogger;
        Logger patchedJarLogger;
        Tomcat tomcat;

    	LogManager.getLogManager().reset();

    	consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter( new OneLineFormatter() );
        consoleHandler.setLevel( Level.ALL );

        tldLogger = Logger.getLogger(
    		"org.apache.jasper.servlet.TldScanner" );
        tldLogger.addHandler( consoleHandler );
        tldLogger.setUseParentHandlers( false );
        tldLogger.setLevel( Level.ALL );

        jarLogger = Logger.getLogger(
    		"org.apache.tomcat.util.scan.StandardJarScanner" );
        jarLogger.addHandler( consoleHandler );
        jarLogger.setUseParentHandlers( false );
        jarLogger.setLevel( Level.ALL );

        patchedJarLogger = Logger.getLogger(
    		"com.tmbsw.jar_scanner_bugzilla_59862.PatchedStandardJarScanner" );
        patchedJarLogger.addHandler( consoleHandler );
        patchedJarLogger.setUseParentHandlers( false );
        patchedJarLogger.setLevel( Level.ALL );

        System.setProperty( JARS_TO_SKIP_NAME, JARS_TO_SKIP_VALUE );

        tomcat = new Tomcat();

        tomcat.getServer().setParentClassLoader(
            Main.class.getClassLoader() );

        tomcat.getConnector().setAttribute( "address", "127.0.0.1" );
        tomcat.getConnector().setAttribute( "port", "8080" );

        tomcat.getHost().setAppBase( "." );

        tomcat.addWebapp( "", "." )
//        	.setJarScanner( new PatchedStandardJarScanner() )
        ;

        tomcat.start();
    }

}
