import java.util.*;

public class DirectedGraph {
	private HashMap<String, Vertex> vertices;

	public DirectedGraph() {
		this.vertices = new HashMap<>();
	}

	public void addEdge(Vertex source, Vertex destination, int weight) {

		Vertex sourceVertex = vertices.get(source.getName());
		Vertex destinationVertex = vertices.get(destination.getName());

		if (sourceVertex != null && destinationVertex != null && sourceVertex.hasEdge(destination.getName())) {
			return;
		}

		if (sourceVertex == null) {
			vertices.put(source.getName(), source);
			sourceVertex = source;
		}

		if (destinationVertex == null) {
			vertices.put(destination.getName(), destination);
			destinationVertex = destination;
		}

		Edge edge = new Edge(sourceVertex, destinationVertex, weight);
		sourceVertex.addEdge(edge);

	}

	public boolean hasEdge(String source, String destination) {

		Vertex sourceVertex = vertices.get(source);
		Vertex destinationVertex = vertices.get(destination);

		if (sourceVertex == null || destinationVertex == null) {
			return false;
		}

		Iterator<Vertex> neighbors = sourceVertex.getNeighborIterator();
		while (neighbors.hasNext()) {
			Vertex current = neighbors.next();
			if (current.equals(destinationVertex)) {
				return true;
			}
		}

		return false;
	}

	public Vertex getVertex(String vertexName) {
		return this.vertices.get(vertexName);
	}

	public void print() {

		for (Vertex v : vertices.values()) {
			System.out.print(v.getName() + " -> ");
			
			Iterator<Vertex> neighbors = v.getNeighborIterator();
			while (neighbors.hasNext())
			{
				Vertex n = neighbors.next();
				System.out.print(n.getName() + " ");
			}
			System.out.println();
		}
	}

	public Iterable<Vertex> vertices() {
		return vertices.values();
	}

	public int size() {
		return vertices.size();
	}

	private void resetVertices() {
		for (Vertex v : vertices.values()) {
			v.unvisit();
			v.setCost(0);
			v.setParent(null);
		}
	}

	/**
	 * Find the shortest path in the graph starting from parameter begin and ending in parameter end.
	 * @param begin name of the vertex that is the begging of the path.
	 * @param end name of the vertex that is the end of the path.
	 * @param path an empty linked list that is going to be filled by this function.
	 * @param lambdaInterface a constraint for the pathfinder to restrict paths.
	 *                        lambda must return true if vertex is allowed and return false if it is not.
	 * @return the total cost in the way of the path.
	 */
	public int getShortestPath(String begin, String end, LinkedList<Vertex> path, PathFinderLambdaInterface lambdaInterface) {

		this.resetVertices();
		Queue<Vertex> vertexQueue = new LinkedList<>(); // Queue of Vertex objects

		Vertex beginVertex = this.vertices.get(begin);
		Vertex endVertex = this.vertices.get(end);

		if (beginVertex == null || end == null) {
			return 0;
		}

		beginVertex.visit();
		vertexQueue.add(beginVertex); // Enqueue vertex

		boolean done = false;

		while (!done && !vertexQueue.isEmpty()) {
			Vertex frontVertex = vertexQueue.remove();
			Iterator<Vertex> neighbors = frontVertex.getNeighborIterator();

			if (frontVertex.equals(endVertex)) {
				continue;
			}

			while (neighbors.hasNext()) {

				Vertex nextNeighbor = neighbors.next();
				if (!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					nextNeighbor.setCost(frontVertex.getCost() + frontVertex.getEdge(nextNeighbor.getName()).getWeight());
					nextNeighbor.setParent(frontVertex);
					vertexQueue.add(nextNeighbor);
				}

				if (nextNeighbor.equals(endVertex)) {
					if (lambdaInterface.isValid(nextNeighbor)) {
						done = true;
						break;
					} else {
						nextNeighbor.unvisit();
					}
				}
			}
		}

		return getPathFindResult(path, endVertex, done);
	}

	/**
	 * Find the cheapest path in the graph starting from parameter begin and ending in parameter end. This function
	 * will optimize cost instead of path length.
	 * @param begin name of the vertex that is the begging of the path.
	 * @param end name of the vertex that is the end of the path.
	 * @param path an empty linked list that is going to be filled by this function.
	 * @param lambdaInterface a constraint for the pathfinder to restrict paths.
	 *                        lambda must return true if vertex is allowed and return false if it is not.
	 * @return the total cost in the way of the path.
	 */
	public int getCheapestPath(String begin, String end, LinkedList<Vertex> path, PathFinderLambdaInterface lambdaInterface) {
		this.resetVertices();
		PriorityQueue<PriorityQueueEntry> vertexQueue = new PriorityQueue<>(); // Queue of Vertex objects

		Vertex beginVertex = this.vertices.get(begin);
		Vertex endVertex = this.vertices.get(end);

		if (beginVertex == null || end == null) {
			return 0;
		}

		vertexQueue.add(new PriorityQueueEntry(beginVertex, 0, null)); // Enqueue vertex

		boolean done = false;

		while (!vertexQueue.isEmpty()) {
			PriorityQueueEntry frontEntry = vertexQueue.remove();

			Vertex frontVertex = frontEntry.frontVertex;

			frontVertex.setParent(frontEntry.parentVertex);
			frontVertex.setCost(frontEntry.cost);

			if (!frontVertex.isVisited()) {
				frontVertex.visit();
				if (frontVertex == endVertex) {
					if (lambdaInterface.isValid(frontVertex)) {
						done = true;
						break;
					} else {
						frontVertex.unvisit();
					}
				} else {
					Iterator<Vertex> neighbors = frontVertex.getNeighborIterator();

					while (neighbors.hasNext()) {

						Vertex nextNeighbor = neighbors.next();
						if (!nextNeighbor.isVisited()) {

							int nextCost = frontVertex.getCost() + frontVertex.getEdge(nextNeighbor.getName()).getWeight();
							vertexQueue.add(new PriorityQueueEntry(nextNeighbor, nextCost, frontVertex));
						}
					}
				}
			}
		}

		return getPathFindResult(path, endVertex, done);
	}

	private int getPathFindResult(LinkedList<Vertex> path, Vertex endVertex, boolean done) {
		if (!done) {
			return 0;
		}

		int result = endVertex.getCost();

		Vertex currentVertex = endVertex;
		while (currentVertex != null) {
			path.push(currentVertex);
			currentVertex = currentVertex.getParent();
		}

		return result;
	}

	public interface PathFinderLambdaInterface {
		boolean isValid(Vertex currentVertex);
	}

	/**
	 * A class to hold vertex information in getCheapestPath function.
	 */
	private static class PriorityQueueEntry implements Comparable<PriorityQueueEntry> {
		Vertex frontVertex;
		int cost;
		Vertex parentVertex;

		public PriorityQueueEntry(Vertex frontVertex, int cost, Vertex parentVertex) {
			this.frontVertex = frontVertex;
			this.parentVertex = parentVertex;
			this.cost = cost;
		}

		@Override
		public int compareTo(PriorityQueueEntry o) {
			return Integer.compare(cost, o.cost);
		}
	}

}
