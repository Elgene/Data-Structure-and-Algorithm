public class Connection {

    private Stop startPointOfStop;
    private Stop endPointOfStop;
    private boolean isSelected = false;
    private Trip tripConnections;

    public Connection(Trip tripConnections, Stop startPointOfStop, Stop endPointOfStop){
        this.tripConnections = tripConnections;
        this.startPointOfStop = startPointOfStop;
        this.endPointOfStop = endPointOfStop;
    }

    public Stop getStartStop() { // getter
        return startPointOfStop;
    }

    public boolean isSelected() { return isSelected; }

    public void setIsSelected(boolean bool) { this.isSelected = bool; }

    public Stop getEndStop() {  return endPointOfStop; }

}
