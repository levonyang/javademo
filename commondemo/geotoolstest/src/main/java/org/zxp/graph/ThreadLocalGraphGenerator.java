package org.zxp.graph;

import org.apache.htrace.shaded.fasterxml.jackson.databind.JsonNode;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.quadtree.Quadtree;
import org.locationtech.spatial4j.distance.DistanceUtils;

import java.util.HashMap;
import java.util.List;

public class ThreadLocalGraphGenerator implements GraphGenerator {
    public static final String START_NODE_ID = "StartNodeID";
    public static final String END_NODE_ID = "EndNodeID";
    public static final String ID = "ID";
    public static final String id="id";
    public static final String EDGE_WEIGHT = "Weight";
    public static final String EDGE_LIST = "edgeList";
    public static final String X = "x";
    public static final String Y = "y";
    private Quadtree spatialIndex;
    private GraphBuilder graphBuilder;
    private HashMap<String, Node> m_nodeObjectID2node;
    private Coordinate m_coordinate;
    private String currentId;
    //单位是米
    private double tolerance=Double.MAX_VALUE;
    //按照0.001度进行划分 长度在1-2公里之内满足查询查询最近节点节本要求
    //可以满足基本范围设置
    private double minExtent=0.001;
    public ThreadLocalGraphGenerator() {
        super();
        m_nodeObjectID2node = new HashMap<>();
        setGraphBuilder(new ThreadLocalGraphBuilder());
        //创建二叉树空间索引
        spatialIndex=new Quadtree();

    }
    private void insertNewNode(ThreadLocalNode threadLocalNode){
        Coordinate coordinate=threadLocalNode.getCoordinate();
        Envelope envelope=new Envelope(coordinate,coordinate);
        Quadtree.ensureExtent(envelope,minExtent);
        spatialIndex.insert(envelope,threadLocalNode);
    }
    /**
     * 通过HGIS的道路线文件来构建图
     * 添加一个JsonObject 进来，读取ObjectID 中的对象来进行读取
     */
    /**
     * @see GraphGenerator#add(Object)
     */
    public Graphable add(Object obj) {
        //当前的road.sm边和点的存储机构 构建图太复杂了
        // 直接每一个边就好了
        JsonNode nodeJsonNode = (JsonNode) obj;
        String startID = nodeJsonNode.get(START_NODE_ID).asText();
        String endID = nodeJsonNode.get(END_NODE_ID).asText();
        ThreadLocalNode threadLocalNode1=retrievalNode(startID);
        ThreadLocalNode threadLocalNode2=retrievalNode(endID);
        ThreadLocalEdge threadLocalEdge = (ThreadLocalEdge) graphBuilder.buildEdge(threadLocalNode1,
                threadLocalNode2);
        threadLocalEdge.setWeight(nodeJsonNode.get(EDGE_WEIGHT).asDouble());
        threadLocalEdge.setId(nodeJsonNode.get(ID).asText());
        graphBuilder.addEdge(threadLocalEdge);
        //返回创建的边
        return threadLocalEdge;
    }

    private ThreadLocalNode retrievalNode(String id){
        ThreadLocalNode threadLocalNode=(ThreadLocalNode)getNodeByID(id);
        if(threadLocalNode==null){
            threadLocalNode = (ThreadLocalNode) graphBuilder.buildNode();
            threadLocalNode.setId(id);
            m_nodeObjectID2node.put(id, threadLocalNode);
            graphBuilder.addNode(threadLocalNode);
        }
        if(id.equals(currentId)&&threadLocalNode.getCoordinate()==null){
            threadLocalNode.setCoordinate(m_coordinate);
            //具有坐标点的节点进行记录
            insertNewNode(threadLocalNode);
        }
        return threadLocalNode;
    }

    /**
     * @see GraphGenerator#get(Object)
     */
    public Graphable get(Object obj) {
        JsonNode nodeJsonNode = (JsonNode) obj;
        String startID = nodeJsonNode.get(START_NODE_ID).asText();
        String endID = nodeJsonNode.get(END_NODE_ID).asText();
        Node node1 = m_nodeObjectID2node.get(startID);
        Node node2 = m_nodeObjectID2node.get(endID);
        if (node1 == null || node2 == null)
            return null;
        return node1.getEdge(node2);
    }

    /**
     * 移出对应的边
     */
    /**
     * @see GraphGenerator#remove(Object)
     */
    public Graphable remove(Object obj) {
        Edge edge = (Edge) get(obj);
        getGraphBuilder().removeEdge(edge);
        return edge;
    }

    @Override
    public GraphBuilder getGraphBuilder() {
        return graphBuilder;
    }

    @Override
    public void setGraphBuilder(GraphBuilder builder) {
        graphBuilder = builder;
    }

    @Override
    public Graph getGraph() {
        return graphBuilder.getGraph();
    }

    public Node getNodeByID(String id){
        return m_nodeObjectID2node.get(id);
    }

    /**
     * 通过坐标点 查询最近的节点进行分析
     * @param coordinate
     * @return
     */
    public ThreadLocalNode getNearestNodeByCoordinate(Coordinate coordinate){
        Envelope envelope=new Envelope(coordinate,coordinate);
        //空间查询
        List queryResult=spatialIndex.query(envelope);

        double minDistance=tolerance;
        //获取距离最短顶点
        ThreadLocalNode resultthreadLocalNode=null;
        for (Object o : queryResult) {
            ThreadLocalNode threadLocalNode=(ThreadLocalNode)o;
            Coordinate coordinate1=threadLocalNode.getCoordinate();
            //返回是极坐标的弧度单位数值
            double distance=DistanceUtils.distHaversineRAD(coordinate1.getY(),coordinate1.getX(),coordinate.getY(),coordinate.getX());
            //乘以地球半径  当前没有坐标系概念 请款情况下 地球半径取值 6367000
            distance=distance* 6367000;
            if(distance<minDistance){
                minDistance=distance;
                resultthreadLocalNode=threadLocalNode;
            }
        }
        return resultthreadLocalNode;
    }




    /**
     * 设置当前ID的坐标点
     * @param m_coordinate
     */
    public void changeIDAndCoordinate(String id,Coordinate m_coordinate) {
        this.m_coordinate = m_coordinate;
        this.currentId=id;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }
}
