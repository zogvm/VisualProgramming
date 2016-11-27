package me.dawars.visualprogramming.nodes.pins;

import me.dawars.visualprogramming.nodes.Connection;
import me.dawars.visualprogramming.nodes.INode;
import me.dawars.visualprogramming.nodes.NodePresenter;
import me.dawars.visualprogramming.nodes.view.NodeView;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dawars on 26/09/16.
 */
public class OutputPin<T> implements IPin {
    private T value;
    private NodePresenter node;

    public List<Connection<T>> connections = Collections.synchronizedList(new ArrayList<>());
    private String name;
    private int x;
    private int y;

    public OutputPin(NodePresenter node, String name) {
        this.node = node;
        this.name = name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void connectTo(InputPin<T> in) {
        Connection<T> connection = new Connection<>(this, in);
        breakConnection(in);

        this.connections.add(connection); // add new connection
        in.connection = connection; // set other half of connection
    }

    private void breakConnection(InputPin<T> in) {
        if (in.connection != null) {
            in.connection.outPin.connections.remove(in.connection); // remove other half of connection
        }
        this.connections.remove(in.connection); // remove if already connected
    }

    public String getName() {
        return name;
    }

    @Override
    public void clicked() {
        System.out.println(getName() + " pin clicked");
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    public void calcPosition(int i) {
        setPosition(x + NodeView.WIDTH - NodeView.PADDING - NodeView.PIN_WIDTH,
                y + (NodeView.PIN_HEIGHT + NodeView.PADDING) * (i + 1) + NodeView.HEADER);
    }

    public NodePresenter getNode() {
        return node;
    }
}
