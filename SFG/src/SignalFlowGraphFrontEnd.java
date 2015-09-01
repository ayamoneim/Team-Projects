import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class SignalFlowGraphFrontEnd extends JComponent {

	private int fileNum = 0;
	private int fileNumber = 0;
	JTextArea textArea;
	Toolkit toolkit;
	Timer timer;
	private DirectedSparseMultigraph<GraphElements.MyVertex, GraphElements.MyEdge> mGraph;

	private Graph<GraphElements.MyVertex, GraphElements.MyEdge> path;
	private JTextPane graphAnalysis;

	protected boolean full = true;
	JPanel resultsPanel;
	int edgeCount = 0;

	int nodeCount = 0;
	static JFrame f;
	static int currNode = -1;
	VisualizationViewer<GraphElements.MyVertex, GraphElements.MyEdge> vv;

	public SignalFlowGraphFrontEnd(JFrame frame) {

		this.f = frame;
		this.mGraph = new DirectedSparseMultigraph<GraphElements.MyVertex, GraphElements.MyEdge>();
		setBackground(Color.BLACK);
		Layout<GraphElements.MyVertex, GraphElements.MyEdge> layout = new StaticLayout(
				mGraph);
		layout.setSize(new Dimension(300, 300));
		vv = new VisualizationViewer<GraphElements.MyVertex, GraphElements.MyEdge>(
				layout);
		vv.setPreferredSize(new Dimension(600, 600));
		// Show vertex and edge labels
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.setForeground(Color.WHITE);
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		toolkit = Toolkit.getDefaultToolkit();
		timer = new Timer();
		timer.schedule(new currentNode(), 0, // initial delay
				1 * 1000); // subsequent rate
		// Create a graph mouse and add it to the visualization viewer
		EditingModalGraphMouse gm = new EditingModalGraphMouse(
				vv.getRenderContext(),
				GraphElements.MyVertexFactory.getInstance(this.mGraph),
				GraphElements.MyEdgeFactory.getInstance());
		// Set some defaults for the Edges...
		GraphElements.MyEdgeFactory.setDefaultWeight(1.0);
		// Trying out our new popup menu mouse plugin...
		PopupVertexEdgeMenuMousePlugin myPlugin = new PopupVertexEdgeMenuMousePlugin();
		JPopupMenu edgeMenu = new MyMouseMenus.EdgeMenu(frame);
		JPopupMenu vertexMenu = new MyMouseMenus.VertexMenu();
		myPlugin.setEdgePopup(edgeMenu);
		myPlugin.setVertexPopup(vertexMenu);

		gm.remove(gm.getPopupEditingPlugin());
		
		gm.add(myPlugin);

		vv.setGraphMouse(gm);

		vv.setBackground(new Color(0, 53, 0));

		JMenuBar menuBar = new JMenuBar();
		JMenu modeMenu = gm.getModeMenu();
		modeMenu.setText("Change Mode");
		modeMenu.setIcon(null); // I'm using this in a main menu
		modeMenu.setPreferredSize(new Dimension(100, 20)); // Change the size so
															// I can see the
															// text

		menuBar.add(modeMenu);
		frame.setJMenuBar(menuBar);
		gm.setMode(ModalGraphMouse.Mode.EDITING);

		vv.getRenderContext().setVertexFillPaintTransformer(
				new MyVertexFillPaintFunction<String>());
		vv.getRenderContext().setEdgeDrawPaintTransformer(
				new MyEdgePaintFunction());
		vv.getRenderContext().setEdgeStrokeTransformer(
				new MyEdgeStrokeFunction());

		setLayout(new BorderLayout());
		add(vv, BorderLayout.CENTER);

		add(setUpControls(), BorderLayout.WEST);
		graphAnalysis = new JTextPane();
		graphAnalysis.setForeground(Color.WHITE);
		graphAnalysis.setBackground(Color.BLACK);
		graphAnalysis.setEditable(false);
		graphAnalysis.setBackground(new Color(0, 53, 0));
		graphAnalysis.setBorder(BorderFactory.createLineBorder(Color.black, 3));

		JScrollPane scrollPane = new JScrollPane(graphAnalysis) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 0);
			}
		};

		f.add(scrollPane, BorderLayout.EAST);

	}

	void outputFile(String o){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("Result"+fileNum+".txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileNum++;
		writer.println(o);
		writer.close();
	}
	static int length = 0;
	static int width = 0;

	class currentNode extends TimerTask {

		public void run() {
			if (animationFlag == 1) {
				if(pathList!=null && pathList.size()>0){
					length = (length + 1) ;
					if(width==-1||(width<pathList.size() && length>=pathList.get(width).size()))
					{
						length = 0;
						if(pathList.get(length).size()!=0){
							width = (width + 1) % pathList.size();
	
							currNode = pathList.get(width).get(length);
						}
					}else{
						currNode = pathList.get(width).get(length);
					}
					vv.repaint();

				}
			} else if (animationFlag == 2) {
				if(loops!=null && loops.size()>0){

					length = (length + 1) ;
					if(width==-1||(width<loops.size() && length>=loops.get(width).size()))
					{
						length = 0;
						if(loops.get(length).size()!=0){
							width = (width + 1) % loops.size();
	
							currNode = loops.get(width).get(length);
						}
					}else{
						currNode = loops.get(width).get(length);
					}
					vv.repaint();

				}
			} else if (animationFlag == 3) {
				if(untouchedLoops!=null && untouchedLoops.size()>0){
	

					length = (length + 1) ;
					if(width==-1||(width<untouchedLoops.size() && length>=untouchedLoops.get(width).size()))
					{
						length = 0;
						if(untouchedLoops.get(length).size()!=0){
							width = (width + 1) % untouchedLoops.size();
	
							currNode =untouchedLoops.get(width).get(length);
						}
					}else{
						currNode = untouchedLoops.get(width).get(length);
					}
					vv.repaint();

				}
			} else {
				currNode = -1;
				vv.repaint();

			}
		}
		
	}

	public class MyEdgePaintFunction implements
			Transformer<GraphElements.MyEdge, Paint> {

		public Paint transform(GraphElements.MyEdge e) {
			
		
			
			
			return Color.BLACK;

		}
	}

	public class MyEdgeStrokeFunction implements
			Transformer<GraphElements.MyEdge, Stroke> {
		protected final Stroke THICK = new BasicStroke(2);

		public Stroke transform(GraphElements.MyEdge e) {
			return THICK;
		}

	}

	public void captureScreen(String fileName) throws Exception {
		animationFlag = 0;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRectangle = new Rectangle(screenSize);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		ImageIO.write(image, "png", new File(fileName+".png"));
	}

	public class MyVertexDrawPaintFunction<V> implements Transformer<V, Paint> {

		public Paint transform(V v) {
			return Color.black;
		}

	}

	public class MyVertexFillPaintFunction<V> implements
			Transformer<GraphElements.MyVertex, Paint> {

		public Paint transform(GraphElements.MyVertex v) {
			if(Integer.parseInt(v.getName())==currNode){
				return new Color(70,0,21);

			}

		
			if (v.getName().equals("" + (mGraph.getVertexCount() - 1))) {
				return new Color(128, 0, 0);
			}
			if (v.getName().equals("0")) {
				return new Color(128, 0, 0);
			}
			
			return new Color(255, 153, 51);
			

		}

	}

	private JPanel setUpControls() {
		JPanel panel = new JPanel();

		panel.setBackground(Color.BLACK);
		//////////////////////////////////////////////////////////////////////
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
////////////////////////////////////////////////////////////////////////////////
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		JPanel buttonsPanel = new JPanel(new GridBagLayout());

		buttonsPanel.setBackground(Color.BLACK);
		buttonsPanel.add(getGraphAnalysis(buttonsPanel), c);
		c.gridx = 0;
		c.gridy = 2;
		buttonsPanel.add(getPathButton(buttonsPanel), c);
		c.gridx = 0;
		c.gridy = 3;
		buttonsPanel.add(getLoopButton(buttonsPanel), c);
		c.gridx = 0;
		c.gridy = 4;
		buttonsPanel.add(getUntouchedLoopsButton(buttonsPanel), c);
		c.gridx = 0;
		c.gridy = 5;
		buttonsPanel.add(getScreenshotButton(buttonsPanel), c);
		c.gridx = 0;
		c.gridy = 6;
		buttonsPanel.add(getStopAnimationButton(buttonsPanel), c);
		c.gridx = 0;
		c.gridy = 7;
		ImageIcon g = new ImageIcon("formula.png");

		final JButton formula = new JButton( g);
		formula.setBackground(Color.white);
		formula.setForeground(Color.white);
	
		
		buttonsPanel.add(formula, c);
		panel.add(buttonsPanel);
		return panel;
	}

	private Component getUntouchedLoopsButton(JPanel buttonsPanel) {
		ImageIcon g = new ImageIcon("loop0.png");

		final JButton UntouchingLoops = new JButton("Untouching Loops", g);
		UntouchingLoops.setBackground(new Color(0, 0, 65));
		UntouchingLoops.setForeground(Color.white);
		UntouchingLoops.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				length = -1;
				width = -1;
				animationFlag = 3;
				untouchedLoops =  new SignalFlowGraphBackEnd(mGraph).getUntouchedLoops();

			}
		});

		return UntouchingLoops;
	}

	public Component getLoopButton(JPanel panel) {
		ImageIcon l = new ImageIcon("loop0.png");

		final JButton loop = new JButton("Get Loops", l);
		loop.setBackground(new Color(0, 0, 65));
		loop.setForeground(Color.white);
		loop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				length = -1;
				width = -1;
				animationFlag = 2;
				loops =  new SignalFlowGraphBackEnd(mGraph).getLoops();

			}
		});

		return loop;
	}
	
	public Component getStopAnimationButton(JPanel panel) {
		ImageIcon l = new ImageIcon("stop.png");

		final JButton loop = new JButton("Stop Animation", l);
		loop.setBackground(new Color(0, 0, 65));
		loop.setForeground(Color.white);
		loop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				animationFlag = 0;

			}
		});

		return loop;
	}

	public Component getScreenshotButton(JPanel panel) {
		ImageIcon ss = new ImageIcon("screenshot0.png");

		final JButton screen = new JButton("Take Screenshot", ss);
		screen.setBackground(new Color(0, 0, 65));
		screen.setForeground(Color.white);
		screen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					captureScreen("Screen" + fileNumber);
					fileNumber++;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				repaint();
			}
		});

		return screen;
	}

	public static int animationFlag = 0;

	public Component getPathButton(JPanel panel) {
		ImageIcon p = new ImageIcon("path0.png");

		final JButton path = new JButton("Get Paths", p);
		path.setBackground(new Color(0, 0, 65));
		path.setForeground(Color.white);
		path.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				length = -1;
				width = -1;
				animationFlag = 1;
				pathList =  new SignalFlowGraphBackEnd(mGraph).getForwardPaths();

			}
		});

		return path;
	}

	ArrayList<ArrayList<Integer>> pathList;
	ArrayList<ArrayList<Integer>> loops;
	ArrayList<ArrayList<Integer>> untouchedLoops;

	public Component getGraphAnalysis(JPanel panel) {
		ImageIcon p = new ImageIcon("delta.png");
		final JButton path = new JButton("Analyze Graph", p);
		path.setBackground(new Color(0, 0, 65));
		path.setForeground(Color.white);
		path.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				length = 0;
				width = 0;
				animationFlag = 0;

				SignalFlowGraphBackEnd sfg = new SignalFlowGraphBackEnd(mGraph);

				pathList = sfg.getForwardPaths();

				String output = "PATHS\n-----------\n";
				for (int i = 0; i < pathList.size(); i++) {
					for (int j = 0; j < pathList.get(i).size(); j++) {
						output += "NODE " + pathList.get(i).get(j) + "  ";
					}
					output += '\n';
				}
				output += "----------------------------\n";

				loops = sfg.getLoops();
				output += "LOOPS\n-----------\n";
				for (int i = 0; i < loops.size(); i++) {
					for (int j = 0; j < loops.get(i).size(); j++) {
						if(j==0)
							output+="L"+(i+1)+"-->";
						output += "NODE " + loops.get(i).get(j) + "  ";
					}
					output += '\n';
				}
				output += "----------------------------\n";
				untouchedLoops = sfg.getUntouchedLoops();
				output += "UNTOUCHING LOOPS\n-----------\n";
				for (int i = 0; i < untouchedLoops.size(); i++) {
					for (int j = 0; j < untouchedLoops.get(i).size(); j++) {
						output += "NODE " + untouchedLoops.get(i).get(j) + "  ";
					}
					if ((i + 1) % 2 == 0)
						output += "\n";
					else
						output += "    ";
				}
				output += "----------------------------\n";
				output += "Untouching Loops' Combinations\n----------------------\n";

				
				ArrayList<ArrayList<ArrayList<Integer>>> combinationsOfUntouchedLoops = sfg.getUntouchedLoopsCombinations() ;
			    for (int i = 1; i < combinationsOfUntouchedLoops.size(); i++) {
			    	if(combinationsOfUntouchedLoops.get(i).size()>0)
			    	output+="Combinations of "+i+'\n';
				for (int j = 0; j < combinationsOfUntouchedLoops.get(i).size(); j++) {
					for (int k = 0; k < combinationsOfUntouchedLoops.get(i).get(j).size(); k++) {	
		                     output+="L"+(combinationsOfUntouchedLoops.get(i).get(j).get(k)+1)+"    " ;
					}output+='\n';}}
				output += "----------------------------\n";


				output += "PATHS' GAIN\n-----------------\n";

				for (int i = 0; i < sfg.getForwardPathGains().size(); i++) {
					output += "L" + (i + 1) + " = "
							+ sfg.getForwardPathGains().get(i);
				}
				output += '\n';
				output += "LOOPS' GAIN\n-----------------\n";

				for (int i = 0; i < sfg.getLoopsGains().size(); i++) {
					output += "D" + (i + 1) + " = "
							+ sfg.getLoopsGains().get(i) + " ";
				}
				output += "\n";

				output += "DELTA = " + sfg.getMainDelta() + '\n';
				output += "\n-----------------------\n";

				output += "RESULT = " + sfg.getOverAllTransferFunction() + '\n';
				output += "\n-----------------------\n";
				outputFile(output);
				graphAnalysis.setText(output);

				StyledDocument doc = graphAnalysis.getStyledDocument();
				SimpleAttributeSet center = new SimpleAttributeSet();
				StyleConstants
						.setAlignment(center, StyleConstants.ALIGN_CENTER);
				doc.setParagraphAttributes(0, doc.getLength(), center, false);

				repaint();
			}
		});

		return path;
	}

}