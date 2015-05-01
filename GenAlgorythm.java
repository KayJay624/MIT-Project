package badania;

import java.awt.Dimension;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GenAlgorythm {
	private int vertNum = 0;
	private int[][] adjMatrix;
	public Population population;
	public void run(int populationQuantity, int maxGen, double mutationProbability, String path, int verNum, int edgNum, JTextArea pane, JProgressBar pbar) throws FileNotFoundException {
		
		if(path == "") {
			this.genGraph(verNum, edgNum);
		}
		else{
			this.getGraph(path);	
		}
		
		displayAdjMatrix();
		//displayGraph(vertNum);
		//population.adjMatrix = adjMatrix;
		population = new Population(populationQuantity, vertNum, adjMatrix);
		
		population.sort();
		
		
		pbar.setMinimum(0);
		pbar.setMaximum(maxGen);
		int gen = 0;
		while(gen < maxGen) {
			Chromosome newChild1 =  population.get(0).crossover(population.get(1));
			newChild1.mutate(0.1);	
			newChild1.fitness(adjMatrix);
			population.add(newChild1);
			
			Chromosome newChild2 =  population.get(1).crossover(population.get(0));
			newChild2.mutate(mutationProbability);
			newChild2.fitness(adjMatrix);
			population.add(newChild2);
			
			population.sort();
			
			population.removeLast();
			population.removeLast();
			
			population.print(pane, gen);
			//population.print2(gen);
			
			
			gen++;
			pbar.setValue(gen);
			pbar.revalidate();
			pbar.repaint();
		}

	}
	
	private void getGraph(String path) throws FileNotFoundException {
		File file = new File(path);
	    Scanner in = new Scanner(file);
	 
	    String line = in.nextLine();
	    String[] vertices = line.split(",");

	    vertNum = vertices.length;
	      	     	      
	    adjMatrix = new int[vertNum][vertNum];
	    
	    for(int i = 0; i < vertNum; i++) {		  
	    	for(int j = 0; j < vertNum; j++) {
				  adjMatrix[i][j] = 0;
			}
	    }
	           
	   String scanLine;
	   while(in.hasNextLine()) {
		   scanLine = in.nextLine();
	       String[] edges = scanLine.split(",");
	       int i = Integer.parseInt(edges[0]) - 1;
	       int j = Integer.parseInt(edges[1]) - 1;
	       
	       adjMatrix[i][j] = 1;
	       adjMatrix[j][i] = 1; 	    	  
		}
	}
	
	private void genGraph(int verNum, int edgNum) {
		Random rand = new Random();
		adjMatrix = new int[verNum][verNum];
		vertNum = verNum;
		
		int n = 0; 	
		for(int i = 0; i < verNum; i++) {
			for(int j = 0; j < verNum; j++) {									
				adjMatrix[i][j] = 0;
			}
		}
		
		if(edgNum > (verNum * (verNum -1))/2) { // Zeby nikt nie robil dowcipow typu dwa wierzchołki i milion krawedzi
			edgNum = (verNum * (verNum -1))/2;  // Zreszta to i tak konieczne, bo wtedy while moglby trwac wiecznie
		}
			
		while(n < edgNum) {
			int i = rand.nextInt(verNum);
			int j = rand.nextInt(verNum);
				
			if(i == j) {
				adjMatrix[i][j] = 0;
			} else if(adjMatrix[i][j] == 0) {
				adjMatrix[i][j] = 1;
				adjMatrix[j][i] = 1;
					
				n++;
			}
			
		}
	}
	
	private void displayAdjMatrix() {
		System.out.println("Macierz sasiedztwa: ");
		for(int i = 0; i < vertNum; i++) {		  
	    	for(int j = 0; j < vertNum; j++) {
				System.out.print(adjMatrix[i][j] + "  ");
			}
	    	System.out.println();
		}
    	System.out.println();
    	System.out.println();
	}
	
	public void displayGraph(int verNum, JPanel panel)
	{
		Vertexx[] tab = new Vertexx[verNum];
		Graph<Vertexx, Edgee> g = new SparseMultigraph<Vertexx, Edgee>();
		for(int i = 0; i < verNum; i++) {
			Vertexx v = new Vertexx(i);
			g.addVertex(v);
			tab[i] = v;
		}
		for(int i = 0; i < verNum; i++) {
			for(int j = 0; j <= i; j++) {
				if(adjMatrix[i][j] == 1) {
					Edgee e = new Edgee(i, j);
					g.addEdge(e, tab[i], tab[j]);
					
				}
			}
		}
		
        Layout<Integer, String> layout = new KKLayout(g);
        layout.setSize(new Dimension(650,300)); 
        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(650,300)); 
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

        panel.add(vv);
        panel.revalidate();
        //JFrame frame = new JFrame("Graph");
        //frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //frame.getContentPane().add(vv); 
        //frame.pack();
        //frame.setVisible(true);    
	} 
	
}
