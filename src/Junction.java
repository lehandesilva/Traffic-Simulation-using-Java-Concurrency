public class Junction implements Runnable {
    private int greenTime;
    public Junction(Road[] roadArray) {

    }

    @Override
    public void run() {

    }
}

/*
* 12 cars per minute through the junction
* Acts as both a producer and consumer
* Requires a fixed number of entry and exit (max of 4 each)
Should be referred by compass direction and enabled and disabled
* One entry road to any exit road and after a preset time, should
change the road that has mutex
* The green light time should be configurable
* More shit on the brief
*/

/*
* Receives an array of roads and the roads should say where they
  lead to with variables like front and back
* */