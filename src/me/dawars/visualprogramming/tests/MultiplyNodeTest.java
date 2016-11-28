package me.dawars.visualprogramming.tests;

import me.dawars.visualprogramming.nodes.AddNode;
import me.dawars.visualprogramming.nodes.Connection;
import me.dawars.visualprogramming.nodes.ConstantNode;
import me.dawars.visualprogramming.nodes.MultiplyNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dawars on 11/28/16.
 */
public class MultiplyNodeTest {
    private MultiplyNode node;
    private ConstantNode a;
    private ConstantNode b;

    @Before
    public void setUp() throws Exception {
        node = new MultiplyNode();
        a = new ConstantNode();
        b = new ConstantNode();

        a.setValue(2.);

        Connection conn = new Connection(a.getOut(), node.getInA());
        a.getOut().connect(conn);


        conn = new Connection(b.getOut(), node.getInB());
        b.setValue(5.);
        b.getOut().connect(conn);

    }

    @Test
    public void execute() throws Exception {
        node.execute();

        assertEquals(node.getOut().getValue(), (Double) 10.);
    }

    @After
    public void tearDown() throws Exception {

    }

}