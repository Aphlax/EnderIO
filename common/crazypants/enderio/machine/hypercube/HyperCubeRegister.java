package crazypants.enderio.machine.hypercube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperCubeRegister {

  static final HyperCubeRegister instance = new HyperCubeRegister();

  private final Map<Channel, List<TileHyperCube>> channelMapping = new HashMap<Channel, List<TileHyperCube>>();

  public synchronized void register(TileHyperCube cube, Channel channel) {
    List<TileHyperCube> cubes = innerGetCubesForChannel(channel);
    if (!cubes.contains(cube)) {
      cubes.add(cube);
    }
  }
  
  public synchronized void deregister(TileHyperCube cube, Channel channel) {
    List<TileHyperCube> cubes = innerGetCubesForChannel(channel);    
    cubes.remove(cube);    
  }
  
  public synchronized List<TileHyperCube> getCubesForChannel(Channel channel) {
    return new ArrayList<TileHyperCube>(innerGetCubesForChannel(channel));
  }

  private List<TileHyperCube> innerGetCubesForChannel(Channel channel) {
    List<TileHyperCube> result = channelMapping.get(channel);
    if (result == null) {
      result = new ArrayList<TileHyperCube>();
      channelMapping.put(channel, result);
    }
    return result;
  }

}
