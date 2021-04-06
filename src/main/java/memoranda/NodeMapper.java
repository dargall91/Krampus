package main.java.memoranda;

public class NodeMapper {
    private static final int mapDefaultX=640;
    private static final int mapDefaultY=480;
    private int mapX;
    private int mapY;
    private NodeColl nodeColl;
    private Coordinate origin;
    private Coordinate outlier;
    private Double lonScale =1.0;
    private Double latScale =1.0;

    public NodeMapper(NodeColl nodeColl){
        this.nodeColl=nodeColl;
        findBaseline();
        findOutlier();
        setMapSize(mapDefaultX, mapDefaultY);
    }

    public void setScale(int xSize, int ySize){
        lonScale =mapX / origin.lonDelta(outlier);
        latScale =mapY / origin.latDelta(outlier);
    }

    public void setMapSize(int mapX, int mapY){
        this.mapX=mapX;
        this.mapY=mapY;
        setScale(mapX, mapY);
    }

    public NodeColl getNodeColl(){
        return nodeColl;
    }

    public Coordinate getScaled(Node n){
        double lat, lon;
        lat=origin.latDelta(n.getCoords()) * latScale;
        lon=origin.lonDelta(n.getCoords()) * lonScale;
        return new Coordinate(lat, lon);
    }

    private void findBaseline(){
        double lat = Coordinate.latMax;
        double lon = Coordinate.lonMax;
        for (Node n:nodeColl){
            if (n.getLat()<lat){
                lat=n.getLat();
            }
            if (n.getLon()<lon){
                lon=n.getLon();
            }
        }
        origin=new Coordinate(lat, lon);
    }

    private void findOutlier(){
        double lat = Coordinate.latMin;
        double lon = Coordinate.lonMin;
        for (Node n:nodeColl){
            if (n.getLat()<lat){
                lat=n.getLat();
            }
            if (n.getLon()<lon){
                lon=n.getLon();
            }
        }
        outlier=new Coordinate(lat,lon);
    }
}
