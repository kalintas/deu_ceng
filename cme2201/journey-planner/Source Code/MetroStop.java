public class MetroStop {

    private String stopId;

    private String stopName;
    private int arrivalTime;
    private int stopSequence;
    private int directionId;
    private String routeShortName;
    private String routeLongName;
    private int routeType;

    public MetroStop(String stationString) {

        String[] stationSplit = stationString.split(",");

        this.stopId = stationSplit[0];
        this.stopName = stationSplit[1];
        this.arrivalTime = Integer.parseInt(stationSplit[2]);
        this.stopSequence = Integer.parseInt(stationSplit[3]);
        this.directionId = Integer.parseInt(stationSplit[4]);
        this.routeShortName = stationSplit[5];
        this.routeLongName = stationSplit[6];
        this.routeType = Integer.parseInt(stationSplit[7]);
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public void setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
    }

    public int getRouteType() {
        return routeType;
    }

    public void setRouteType(int routeType) {
        this.routeType = routeType;
    }

    public String toString() {
        return String.format("%s[%s]", this.getStopName(), this.getStopId());
    }
}
