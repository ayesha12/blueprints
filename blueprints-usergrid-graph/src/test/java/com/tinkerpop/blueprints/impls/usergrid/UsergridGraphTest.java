package com.tinkerpop.blueprints.impls.usergrid;


import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.GraphTest;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.usergrid.drivers.blueprints.UsergridGraph;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ApigeeCorporation on 7/13/15.
 */
public class UsergridGraphTest extends GraphTest {

    @Override
    public Graph generateGraph() {

        return generateGraph("/Users/ayeshadastagiri/blueprints/blueprints-usergrid-graph/src/main/resources/usergrid.properties");

    }

//    @Override
//    public Graph generateGraph(String filepath) {
//        Graph usergridgraph = GraphFactory.open(filepath);
//        return usergridgraph;
//    }

    @Override
    public Graph generateGraph(String filepath) {
//        Graph usergridgraph = GraphFactory.open(filepath);
//        return usergridgraph;
        PropertiesConfiguration conf = null;
        String defaultType = null;
        String appName = null;
        try {
            conf = new PropertiesConfiguration(filepath);
            defaultType = conf.getString("usergrid.defaultType");

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        UsergridGraph graph = null;
        graph = new UsergridGraph(conf);
        return graph;
    }


    public void testVertexTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new VertexTestSuite(this));
        printTestPerformance("VertexTestSuite", this.stopWatch());
    }



    public void testEdgeTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new EdgeTestSuite(this));
        printTestPerformance("EdgeTestSuite", this.stopWatch());
    }



    public void testGraphTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphTestSuite(this));
        printTestPerformance("GraphTestSuite", this.stopWatch());
    }


    /**
     * Closes the client connection. Properly close the graph.
     */
    public void deleteData(Graph graph) {

    /*
    1. Check the client initialized.
    2. Close the connection to Usergrid.
    3. Error handling if closeConnection() failed.
    */
        Iterable<Vertex> vertices = graph.getVertices();
        for (Vertex vertex : vertices){
            graph.removeVertex(vertex);
        }
    }



    @Override
    public void doTestSuite(TestSuite testSuite) throws Exception {
        for (Method method : testSuite.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("Testing " + method.getName() + "...");
                Graph graph = this.generateGraph();
                deleteData(graph);
                try {
                    method.invoke(testSuite);
                }
                catch (InvocationTargetException e ){
                    System.out.println("InvocationTargetException exception : "+ e.getTargetException());

                }
                catch (Exception e)
                {
                    System.out.println("other exception : " + e);
                }
                System.out.println("exectuted tests for : " + method.getName());
            }
        }
    }

}
