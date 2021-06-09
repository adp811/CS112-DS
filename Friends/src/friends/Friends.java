package friends;

import java.util.ArrayList;


import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		

		boolean[] marked = new boolean[g.members.length]; 	
		
		int[] edgeTo = new int[g.members.length]; 			
				
		int source = g.map.get(p1);
		int endpoint = g.map.get(p2);

		//int source = 0; 		//for testing  
		//int endpoint = 0; 	//for testing

		Queue<Integer> q = new Queue<Integer>(); 

		q.enqueue(source); 			
		marked[source] = true; 				

		while(!q.isEmpty()){

			int v = q.dequeue();

			Person person = g.members[v];
			
			for(Friend friend = person.first; friend != null; friend = friend.next){

				int w = friend.fnum;

				if(!marked[w]){

					q.enqueue(w);
					marked[w] = true;
					edgeTo[w] = v;

				}

			}

		}

		if(marked[endpoint] == false) return null; 			//return null if path is not present

		ArrayList<String> arr = new ArrayList<String>();
		Stack<Integer> s = new Stack<Integer>();

		s.push(endpoint);

		while(s.peek() != source){
			endpoint = edgeTo[endpoint];
			s.push(endpoint);

		}
		
		while(!s.isEmpty()){
			arr.add(g.members[s.pop()].name);
		}				

		
		return arr;

	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		

		ArrayList<Integer> peopleAtSchool = new ArrayList<Integer>();

		for(Person person : g.members){
			
			int index = g.map.get(person.name);

			if (person.school != null){
				if(person.school.equals(school)) peopleAtSchool.add(index);
			}

		}

		if(peopleAtSchool.isEmpty()) return null; //return if no students found at given school

		ArrayList<ArrayList<String>> totalCliques = new ArrayList<ArrayList<String>>();

		boolean[] marked = new boolean[g.members.length];

		for(int i : peopleAtSchool){

			if(marked[i] == false) {

				ArrayList<String> clique = new ArrayList<String>();

				Queue<Integer> q = new Queue<Integer>(); 

				q.enqueue(i); 			
				marked[i] = true; 	

				while(!q.isEmpty()){

					int v = q.dequeue();
					
					Person person = g.members[v];
					clique.add(person.name);

					for(Friend friend = person.first; friend != null; friend = friend.next){
		
						int w = friend.fnum;
		
						if(!marked[w]){
		
							if(g.members[w].school != null) {
								if(g.members[w].school.equals(school)){
		
									q.enqueue(w);
									marked[w] = true;
									
								}
							}
						}
					}
				}

				totalCliques.add(clique);
							
			} 


		}


		return totalCliques;
		
	}
	

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		ArrayList<String> connectors = new ArrayList<String>();

		boolean[] marked = new boolean[g.members.length];
		boolean[] isConnector = new boolean[g.members.length];

		int[] dfsnum = new int[g.members.length]; 
		int[] back = new int[g.members.length]; 
		int[] edgeTo = new int[g.members.length]; 

		for(int i = 0; i < edgeTo.length; i++) { edgeTo[i] = -1; }

		for(Person person : g.members) {

			int index = g.map.get(person.name);

			if(marked[index] == false){
				dfs(g, index, 0, dfsnum, back, edgeTo, marked, isConnector);

			}

		}
		
		for(int j = 0; j < isConnector.length; j++){
			if(isConnector[j] == true) connectors.add(g.members[j].name);
		}


		return connectors;
		
	}


	private static void dfs (Graph g, int v, int count, int[] dfsnum, int[] back, int[] edgeTo, 
		boolean[] marked, boolean[] isConnector) {

			marked[v] = true;

			dfsnum[v] = count;
			back[v] = count;

			int children = 0;

			for(Friend friend = g.members[v].first; friend != null; friend = friend.next) {

				int w = friend.fnum;

				if(!marked[w]) {

					edgeTo[w] = v;
					children += 1;

					dfs(g, w, count + 1, dfsnum, back, edgeTo, marked, isConnector);

					back[v] = Integer.min(back[v], back[w]);

					if(edgeTo[v] == -1 && children > 1) isConnector[v] = true;
					if(edgeTo[v] != -1 && dfsnum[v] <= back[w]) isConnector[v] = true;

				} else if (w != edgeTo[v]) {

					back[v] = Integer.min(back[v], dfsnum[w]);

				} 

			}

		
	}


}

