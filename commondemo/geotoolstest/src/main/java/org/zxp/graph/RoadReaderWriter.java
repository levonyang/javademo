package org.zxp.graph;

import org.apache.htrace.shaded.fasterxml.jackson.databind.JsonNode;
import org.apache.htrace.shaded.fasterxml.jackson.databind.ObjectMapper;
import org.geotools.graph.io.standard.AbstractReaderWriter;
import org.geotools.graph.io.standard.FileReaderWriter;
import org.geotools.graph.structure.Graph;
import org.locationtech.jts.geom.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RoadReaderWriter extends AbstractReaderWriter implements FileReaderWriter {

    @Override
    public Graph read() throws Exception {
        ThreadLocalGraphGenerator generator = (ThreadLocalGraphGenerator) getProperty(GENERATOR);
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            // 构造一个BufferedReader类来读取文件
            br = new BufferedReader(new FileReader((File) getProperty(FILENAME)));
            String s;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            String value = result.toString().trim();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(value);
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode nodeJsonNode = jsonNode.get(i);
                Coordinate coordinate=new Coordinate(nodeJsonNode.get(ThreadLocalGraphGenerator.X).
                        asDouble(),nodeJsonNode.get(ThreadLocalGraphGenerator.Y).asDouble());
                JsonNode edgeNode = nodeJsonNode.get(ThreadLocalGraphGenerator.EDGE_LIST);
                generator.changeIDAndCoordinate(nodeJsonNode.get(ThreadLocalGraphGenerator.id).asText(),coordinate);
                for (int j = 0; j < edgeNode.size(); j++) {
                    //增加点和边
                    generator.add(edgeNode.get(j));
                }
            }
        } finally {
            br.close();
        }
        return generator.getGraph();
    }

    @Override
    public void write(Graph g) throws Exception {
        throw new UnsupportedOperationException();
    }
}
