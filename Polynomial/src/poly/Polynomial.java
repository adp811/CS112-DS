package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

		Node ptr1 = poly1;
		Node ptr2 = poly2;

		Node list = new Node(0,0,null);
		Node ptr3 = list;

		if(poly1 == null && poly2 != null){ 
			for(Node ptr = poly2; ptr != null; ptr = ptr.next){

				Node temp = new Node(
						ptr.term.coeff,
						ptr.term.degree,
						null);
				
					ptr3.next = temp;
					ptr3 = temp;

			}
		}

		if(poly1 != null && poly2 == null){ //main 
			for(Node ptr = poly1; ptr != null; ptr = ptr.next){

				Node temp = new Node(
						ptr.term.coeff,
						ptr.term.degree,
						null);
				
					ptr3.next = temp;
					ptr3 = temp;

			}
		}
		
		if(poly1 != null && poly2 != null){
			while(ptr1 != null && ptr2 != null){

				int deg1 = ptr1.term.degree;
				int deg2 = ptr2.term.degree;

				Float coeffSum;
				int degree;
				
				if(deg1 == deg2){

					coeffSum = ptr1.term.coeff + ptr2.term.coeff; 
					degree = ptr1.term.degree;

					ptr1 = ptr1.next; //push both pointers
					ptr2 = ptr2.next;

				} else if (deg1 > deg2) {

					coeffSum = ptr2.term.coeff;
					degree = ptr2.term.degree;

					ptr2 = ptr2.next; //push pointer 2 (poly 2)

				} else {

					coeffSum = ptr1.term.coeff;
					degree = ptr1.term.degree;

					ptr1 = ptr1.next; //push pointer 1 (poly1)
				
				} 
				
				if (coeffSum != 0){ //add new node to end of list
					Node temp = new Node(coeffSum, degree, null);
					ptr3.next = temp;
					ptr3 = temp;
				}


				while(ptr1 == null && ptr2 != null){ 

					coeffSum = ptr2.term.coeff;
					degree = ptr2.term.degree;
					ptr2 = ptr2.next;
				
					if(coeffSum != 0){
						Node temp2 = new Node(coeffSum, degree, null);
						ptr3.next = temp2;
						ptr3 = temp2;
					}

				}

				while(ptr2 == null && ptr1 != null){

					coeffSum = ptr1.term.coeff;
					degree = ptr1.term.degree;
					ptr1 = ptr1.next;

					if(coeffSum != 0){
						Node temp3 = new Node(coeffSum, degree, null);
						ptr3.next = temp3;
						ptr3 = temp3;
					}
				}
			
			}
		}


		if(list.next == null) list = null;
		else list = list.next;
		
		return list;
				
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

		Node list =  new Node(0,0,null);
		Node ptr4 = list;

		Node tempList = new Node(0,0,null);
		Node ptr3 = tempList;

	
		//if poly1 and poly2 not null
		if(poly1 != null && poly2 != null){

			int maxDegree = 0;

			for(Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next){
				for(Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next){

					if((ptr1.term.degree * ptr2.term.degree) > maxDegree)
						maxDegree = ptr1.term.degree * ptr2.term.degree;

					Node temp = new Node(
						ptr1.term.coeff * ptr2.term.coeff,
						ptr1.term.degree + ptr2.term.degree,
						null);
				
					ptr3.next = temp;
					ptr3 = temp;

				}
			}

			tempList = tempList.next;

			for(int degree = 0; degree <= maxDegree; degree++){

				float coeff = 0.00f;

				for(Node ptr = tempList; ptr != null; ptr = ptr.next){
					if(ptr.term.degree == degree) coeff = coeff + ptr.term.coeff;
				}

				if(coeff != 0){
					Node temp = new Node(coeff, degree, null);
					ptr4.next = temp;
					ptr4 = temp;
				}

			}


		}

		if(list.next == null) list = null;
		else list = list.next;
		
		return list;
		
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

		Float eval = 0.00f; 
		
		for(Node ptr = poly; ptr != null; ptr = ptr.next){
			eval = eval + 
				(ptr.term.coeff *
				(float)(Math.pow((double) x, (double) ptr.term.degree)));
		}

		return eval;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
