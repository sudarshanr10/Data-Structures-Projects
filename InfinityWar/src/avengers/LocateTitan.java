package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

        //Declare locateTitan Input and Output Files
        String locateTitanInputFile = args[0];
        String locateTitanOutputFile = args[1];

        StdIn.setFile(locateTitanInputFile);
        int generators = StdIn.readInt();

        //Assigns generator values with functionality values
        int ptr = 0;
        double[] functArr = new double[generators];
        while(ptr < generators)
        {
            int genValue = StdIn.readInt();
            double functionality = StdIn.readDouble();
            functArr[genValue] = functionality;
            ptr++;
        }

        //Populate Adjacency Matrix
        int matrix[][] = new int[generators][generators];
        for(int i=0; i<matrix.length; i++)
        {
            for(int j=0; j<matrix[i].length; j++)
            {
                int weight = StdIn.readInt();
                matrix[i][j] = weight;
            }
        }
        //Updates every edge
        for(int i=0; i<matrix.length; i++)
        {
            for(int j=0; j<matrix[i].length; j++)
            {
                matrix[i][j] = (int)(matrix[i][j]/(functArr[i] * functArr[j]));
            }
        }

        //Dijkstra's Algorithm
        int[] minCost = new int[matrix.length];
        boolean[] dijkstraSet = new boolean[matrix.length];
        for(int x=0; x<minCost.length; x++)
        {
            if(x == 0)
            {
                minCost[x] = 0;
            }
            else{
                minCost[x] = Integer.MAX_VALUE;
            }
        }
        for(int x= 0; x<generators-1; x++) 
        {
            int currentSource = getMinCostNode(minCost, dijkstraSet);
            dijkstraSet[currentSource] = true;
            for(int w = 0; w<generators; w++)
            {
                if(matrix[currentSource][w] > 0)
                {
                    if(dijkstraSet[w] == false && minCost[currentSource] != Integer.MAX_VALUE && minCost[w] > (minCost[currentSource] + matrix[currentSource][w]))
                    {
                        minCost[w] = minCost[currentSource] + matrix[currentSource][w];
                    }
                }
            }    
        }
        StdOut.setFile(locateTitanOutputFile);
        StdOut.print(minCost[minCost.length-1]);
    }

    private static int getMinCostNode(int[] minCost, boolean[] dijkstra)
    {
        int minCostNum = Integer.MAX_VALUE;
        int minCostNode = 0;
        for(int i=0; i<minCost.length; i++)
        {
            if(dijkstra[i] == false)
            {
                if(minCost[i] < minCostNum)
                {
                    minCostNum = minCost[i];
                    minCostNode = i;
                }
            }
        }
        return minCostNode;
    }
}
