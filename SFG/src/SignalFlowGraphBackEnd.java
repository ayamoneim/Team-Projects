import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class SignalFlowGraphBackEnd {

	// ****************************** (HAS_A) INSTANCES,VARIABLES
	// ************************************\\
	private HashMap<Integer, Integer> vertexToIndexMap, indexToVertexMap;
	private ArrayList<ArrayList<Integer>> adjList;
	private Double[][] adjMat;
	private int source, sink;
	private ArrayList<ArrayList<Integer>> forwardPaths;
	private ArrayList<ArrayList<Integer>> loops;
	private ArrayList<ArrayList<Integer>> untouchingLoops;

	private ArrayList<ArrayList<Integer>> forwardPathsIndces;
	private ArrayList<ArrayList<Integer>> loopsIndces;
	private boolean[] visited;
	private ArrayList<Integer> curPath;
	private ArrayList<ArrayList<ArrayList<Integer>>> combinationsOfUntouchedLoops;
	private ArrayList<Double> loopsGain;
	private ArrayList<Double> forwardPathsGain;
	private ArrayList<Double> deltas;
	private Double bigDelta, overallTransferFunction;
	private Collection<GraphElements.MyVertex> vertex;
	private Collection<GraphElements.MyEdge> edges;
	private DirectedSparseMultigraph<GraphElements.MyVertex, GraphElements.MyEdge> g;

	// **********************************************************************************\\

	// **************************** CONSTRUCTORS
	// ********************************************\\
	public SignalFlowGraphBackEnd(
			DirectedSparseMultigraph<GraphElements.MyVertex, GraphElements.MyEdge> g) {
		this.g = g;
		
		initializeMaps();
		initializeSignalFlowGraphStructures();
		if (g.getVertexCount()>0) {
		generateForwardPaths();
		generateLoops();
		findUntouchedLoopsCombinations();
		setUntouchingLoops();

		computeForwardPathsGain();
		computeLoopsGain();
		computeBigDelta();
		computeDeltas();
		computeOverallTransferFunction();
		}

	}

	// ****************************************************************************************\\

	// ***************************************** INITIALIZATIONS
	// ***************************************\\

	// /////////////////////////////////////////////////////////////////////////////
	private void initializeMaps() {
		vertexToIndexMap = new HashMap<>();
		indexToVertexMap = new HashMap<>();
		vertex = g.getVertices();
		edges = g.getEdges();
		adjMat = new Double[g.getVertexCount()][g.getVertexCount()];
		adjList = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < g.getVertexCount(); i++) {
			adjList.add(new ArrayList<Integer>());
		}
		ArrayList<Integer> sortedVerteces = new ArrayList<>();
		for (Iterator<GraphElements.MyVertex> it = vertex.iterator(); it
				.hasNext();) {
			GraphElements.MyVertex myVertex = it.next();

			int node = Integer.parseInt(myVertex.getName());
			sortedVerteces.add(node);
		}
		Collections.sort(sortedVerteces);
		source = 0;
		sink = g.getVertexCount() - 1;
		for (int i = 0; i < sortedVerteces.size(); i++) {
			indexToVertexMap.put(i, sortedVerteces.get(i));
			vertexToIndexMap.put(sortedVerteces.get(i), i);
		}

	}

	// ////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////
	private void initializeSignalFlowGraphStructures() {
		// // adjMatrix , adjList
		for (Iterator<GraphElements.MyEdge> it = edges.iterator(); it.hasNext();) {
			GraphElements.MyEdge myEdge = it.next();

			int startVertex = Integer.parseInt(g.getEndpoints(myEdge)
					.getFirst().getName());

			int endVertex = Integer.parseInt(g.getEndpoints(myEdge).getSecond()
					.getName());

			adjMat[vertexToIndexMap.get(startVertex)][vertexToIndexMap
					.get(endVertex)] = myEdge.getWeight();
			adjList.get(vertexToIndexMap.get(startVertex)).add(
					vertexToIndexMap.get(endVertex));
		}

		// forward paths structures
		forwardPaths = new ArrayList<ArrayList<Integer>>();
		forwardPathsIndces = new ArrayList<ArrayList<Integer>>();
		forwardPathsGain = new ArrayList<Double>();

		// loop structures
		loops = new ArrayList<ArrayList<Integer>>();
		loopsGain = new ArrayList<Double>();
		loopsIndces = new ArrayList<ArrayList<Integer>>();

		// D1 , D2 ... etc
		deltas = new ArrayList<Double>();

		// only needed to be initialized here as backtracking always false it
		// again (Y)
		visited = new boolean[adjList.size()];
		for (int i = 0; i < adjList.size(); i++) {
			visited[i] = false;
		}

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	// ********************************************************************************************************\\

	// ****************************************** FORWARD PATHS
	// *********************************************\\

	// //////////////////////////////////////////////////////////////////
	private void generateForwardPaths() {
		visited[source] = true;
		curPath = new ArrayList<Integer>();
		curPath.add(new Integer(source));
		dfsForwardPaths(source);
		getActualForwardPaths();
	}

	// ////////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////////
	private void dfsForwardPaths(int curNode) {
		if (curNode == sink) {
			ArrayList<Integer> generatedForwardPath = new ArrayList<Integer>();
			for (int i = 0; i < curPath.size(); i++) {
				generatedForwardPath.add(curPath.get(i));
			}
			forwardPaths.add(generatedForwardPath);
			return;
		}
		ArrayList<Integer> curList = adjList.get(curNode);
		for (int i = 0; i < curList.size(); i++) {
			if (visited[curList.get(i)] == false) {

				visited[curList.get(i)] = true;
				curPath.add(curList.get(i));

				dfsForwardPaths(curList.get(i));

				visited[curList.get(i)] = false;
				curPath.remove(curPath.size() - 1);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////
	private void getActualForwardPaths() {
		for (int i = 0; i < forwardPaths.size(); i++) {
			forwardPathsIndces.add(new ArrayList<Integer>());
			for (int j = 0; j < forwardPaths.get(i).size(); j++) {
				forwardPathsIndces.get(i).add(
						indexToVertexMap.get(forwardPaths.get(i).get(j)));
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////

	// *********************************************************************************************************\\

	// ****************************************** LOOPS
	// *************************************\\

	// ////////////////////////////////////////////////////////////////////
	private void generateLoops() {
		visited[source] = false;
		for (int i = 0; i < adjList.size(); i++) {
			curPath = new ArrayList<Integer>();
			curPath.add(new Integer(i));
			dfsLoops(i, i);
		}
		removeEquivilantLoops();
		getActualLoops();
	}

	// /////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////
	public void dfsLoops(int curNode, int loopStart) {
		if (curNode == loopStart && visited[curNode] == true) {
			ArrayList<Integer> curLoopGenerated = new ArrayList<Integer>();
			for (int i = 0; i < curPath.size(); i++) {
				curLoopGenerated.add(curPath.get(i));
			}
			loops.add(curLoopGenerated);
			return;
		}

		ArrayList<Integer> curList = adjList.get(curNode);
		for (int i = 0; i < curList.size(); i++) {
			if (visited[curList.get(i)] == false) {
				visited[curList.get(i)] = true;
				curPath.add(curList.get(i));

				dfsLoops(curList.get(i), loopStart);

				visited[curList.get(i)] = false;
				curPath.remove(curPath.size() - 1);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////
	private void getActualLoops() {
		for (int i = 0; i < loops.size(); i++) {
			loopsIndces.add(new ArrayList<Integer>());
			for (int j = 0; j < loops.get(i).size(); j++) {
				loopsIndces.get(i).add(indexToVertexMap.get(loops.get(i).get(j)));
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////////////////
	private void removeEquivilantLoops() {
		for (int i = 0; i < loops.size(); i++) {
			if (loops.get(i) == null)
				continue;
			for (int j = i + 1; j < loops.size(); j++) {
				if (loops.get(j) == null
						|| loops.get(i).size() != loops.get(j).size())
					continue;

				ArrayList<Integer> doubledFirst = new ArrayList<Integer>();

				for (int k = 0; k < loops.get(i).size() - 1; k++)
					doubledFirst.add(loops.get(i).get(k));
				for (int k = 0; k < loops.get(i).size() - 1; k++)
					doubledFirst.add(loops.get(i).get(k));
				for (int k = 0; k < loops.get(i).size(); k++) {
					ArrayList<Integer> subList = new ArrayList<Integer>(
							doubledFirst.subList(k, k
									+ (loops.get(i).size() - 1)));
					int count = 0;
					for (int t = 0; t < loops.get(i).size() - 1; t++) {
						if (loops.get(j).get(t).intValue() == subList.get(t)
								.intValue()) {
							count++;
						}
					}
					if (count == subList.size()) {
						loops.set(j, null);
						break;
					}
				}
			}
		}

		ArrayList<ArrayList<Integer>> finalLoops = new ArrayList<ArrayList<Integer>>();

		for (int i = 0; i < loops.size(); i++)
			if (loops.get(i) != null)
				finalLoops.add(loops.get(i));
		loops = finalLoops;
		return;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////

	// ***************************************************************************************************\\

	// *********************************** LOOPS COMBINATIONS
	// *****************************************\\

	// ///////////////////////////////////////////////////////////////////////////////////
	private void findUntouchedLoopsCombinations() {
		combinationsOfUntouchedLoops = new ArrayList<ArrayList<ArrayList<Integer>>>();
		combinationsOfUntouchedLoops.add(null);
		combinationsOfUntouchedLoops.add(new ArrayList<ArrayList<Integer>>());
		for (int i = 0; i < loops.size(); i++) {
			combinationsOfUntouchedLoops.get(1).add(new ArrayList<Integer>());
			combinationsOfUntouchedLoops.get(1).get(i).add(i);
		}
		for (int depth = 2; depth <= adjList.size(); depth++) {
			combinationsOfUntouchedLoops
					.add(new ArrayList<ArrayList<Integer>>());
			for (int j = 0; j < loops.size(); j++) {
				curPath = new ArrayList<Integer>();
				curPath.add(new Integer(j));
				combinations(j, depth);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////
	private void combinations(int cur, int depth) {
		if (depth == 1) {
			for (int i = 0; i < curPath.size(); i++) {
				for (int j = i + 1; j < curPath.size(); j++) {
					if (!isUnTouched(loops.get(curPath.get(i)),
							loops.get(curPath.get(j)))) {
						// System.out.println(loops.get(curPath.get(i));
						// System.out.println(loops.get(curPath.get(j).);

						return;
					}
				}
			}

			ArrayList<Integer> curCombination = new ArrayList<Integer>();
			for (int i = 0; i < curPath.size(); i++) {
				curCombination.add(curPath.get(i));
			}
			combinationsOfUntouchedLoops.get(curCombination.size()).add(
					curCombination);

			return;
		}
		for (int k = cur + 1; k < loops.size(); k++) {
			curPath.add(new Integer(k));
			combinations(k, depth - 1);
			curPath.remove(curPath.size() - 1);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private boolean isUnTouched(ArrayList<Integer> first,
			ArrayList<Integer> second) {
		for (int i = 0; i < first.size() - 1; i++) {
			for (int j = 0; j < second.size() - 1; j++) {
				if (first.get(i).intValue() == second.get(j).intValue()) {
					return false;
				}
			}
		}
		return true;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	// ******************************************************************************************************\\

	// ************************************** COMPUTATIONS
	// *****************************************\\

	// /////////////////////////////////////////////////////////////////////////////////////////
	private void computeLoopsGain() {
		int firstEnd, secondEnd;
		double gain;
		for (int i = 0; i < loops.size(); i++) {
			firstEnd = loops.get(i).get(0);
			gain = 1;
			for (int j = 1; j < loops.get(i).size(); j++) {
				secondEnd = loops.get(i).get(j);
				gain *= adjMat[firstEnd][secondEnd];
				firstEnd = secondEnd;
			}
			loopsGain.add(gain);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////

	private void computeDeltas() {
		for (int curForwardPath = 0; curForwardPath < forwardPaths.size(); curForwardPath++) {
			double currentDelta = 1.0, total = 0, product = 1;
			int sign = 1;
			for (int i = 1; i < combinationsOfUntouchedLoops.size(); i++) {
				total = 0;
				sign *= -1;
				for (int j = 0; j < combinationsOfUntouchedLoops.get(i).size(); j++) {
					product = 1;
					for (int k = 0; k < combinationsOfUntouchedLoops.get(i)
							.get(j).size(); k++) {
						boolean untouch = isUnTouched(
								forwardPaths.get(curForwardPath),
								loops.get(combinationsOfUntouchedLoops.get(i)
										.get(j).get(k)));
						if (!untouch) {
							product = 0;
							break;
						}
						product = product
								* loopsGain.get(combinationsOfUntouchedLoops
										.get(i).get(j).get(k));
					}
					total += product;
				}
				currentDelta += sign * total;
			}
			deltas.add(currentDelta);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////
	private void computeBigDelta() {
		int sign = 1;
		double total = 0, product = 1;
		bigDelta = 1.0;
		for (int i = 1; i < combinationsOfUntouchedLoops.size(); i++) {
			sign *= -1;
			total = 0;
			for (int j = 0; j < combinationsOfUntouchedLoops.get(i).size(); j++) {
				product = 1;
				for (int k = 0; k < combinationsOfUntouchedLoops.get(i).get(j)
						.size(); k++) {
					product = product
							* loopsGain.get(combinationsOfUntouchedLoops.get(i)
									.get(j).get(k));
				}
				total += product;
			}
			bigDelta += sign * total;
		}
	}

	// //////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////////
	private void computeForwardPathsGain() {
		int firstEnd, secondEnd;
		double gain;
		for (int i = 0; i < forwardPaths.size(); i++) {
			firstEnd = forwardPaths.get(i).get(0);
			gain = 1;
			for (int j = 1; j < forwardPaths.get(i).size(); j++) {
				secondEnd = forwardPaths.get(i).get(j);
				gain = gain * adjMat[firstEnd][secondEnd];
				firstEnd = secondEnd;
			}
			forwardPathsGain.add(gain);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////////////////
	private void computeOverallTransferFunction() {
		overallTransferFunction = 0.0;
		for (int i = 0; i < forwardPaths.size(); i++) {
			overallTransferFunction += (forwardPathsGain.get(i) * deltas.get(i))
					/ bigDelta;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	// *********************************************************************************************************\\

	private void setUntouchingLoops() {
		untouchingLoops = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < loopsIndces.size(); i++) {
			for (int j = i + 1; j < loopsIndces.size(); j++) {
				boolean untouching = true;
				for (int a = 0; a < loopsIndces.get(i).size(); a++) {
					for (int b = 0; b < loopsIndces.get(j).size(); b++) {
						if (loopsIndces.get(i).get(a) == loopsIndces.get(j)
								.get(b))
							untouching = false;
					}
				}
				if (untouching) {
					ArrayList<Integer> arr = new ArrayList<Integer>();
					for(int k = 0;k<loopsIndces.get(i).size();k++)
						arr.add(loopsIndces.get(i).get(k));
					untouchingLoops.add(arr);
					arr = new ArrayList<Integer>();
					for(int k = 0;k<loopsIndces.get(j).size();k++)
						arr.add(loopsIndces.get(j).get(k));
					untouchingLoops.add(arr);
					
				}
			}
		}
	}

	// *********************************** OUTPUTS GETTERS
	// ******************************************\\
	public ArrayList<ArrayList<Integer>> getForwardPaths() {
	    if (forwardPathsIndces == null ) return new  ArrayList<ArrayList<Integer>> ();
		return forwardPathsIndces;
	}

	public ArrayList<ArrayList<Integer>> getLoops() {
	    if (loopsIndces == null ) return new  ArrayList<ArrayList<Integer>> ();
		return loopsIndces;
	}

	public ArrayList<ArrayList<Integer>> getUntouchedLoops() {
	    if (untouchingLoops == null ) return new  ArrayList<ArrayList<Integer>> ();
		return untouchingLoops;
	}

	public ArrayList<ArrayList<ArrayList<Integer>>> getUntouchedLoopsCombinations() {
	    if (combinationsOfUntouchedLoops == null ) return new ArrayList<ArrayList<ArrayList<Integer>>> ();
		return combinationsOfUntouchedLoops;
		
	}

	public ArrayList<Double> getForwardPathGains() {
	    if (forwardPathsGain == null ) return new ArrayList<Double> ();
		return forwardPathsGain;
	}

	public ArrayList<Double> getLoopsGains() {
	    if (loopsGain == null ) return new ArrayList<Double> (); 
		return loopsGain;
	}

	public ArrayList<Double> getDeltas() {
	    if (deltas == null ) return new ArrayList<Double> (); 
		return deltas;
	}

	public Double getMainDelta() {
	    if (bigDelta == null) return 0.0 ;
		return bigDelta;
	}

	public Double getOverAllTransferFunction() {
	    if (overallTransferFunction == null) return 0.0 ;
		return overallTransferFunction;
	}
	// *******************************************************************************************************\\

}
