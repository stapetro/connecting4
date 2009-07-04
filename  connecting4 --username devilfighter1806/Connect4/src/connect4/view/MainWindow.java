//TUKA SI IGRAQ - ne tarsi smislen/krasiv kod


package connect4.view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {

	public MainWindow(){
		initialize();
	}
	
	private void initialize(){
		int step = 30;
		setVisible(true);
		setMinimumSize(new Dimension(300, 300));
		setResizable(false);
		setTitle("Connect4");
		add(new JPanel());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while(true){
				getContentPane().getGraphics().drawRect(step, step, step*4, step*4);
				getContentPane().getGraphics().drawRect(step*2, step, step*2, step*4);
				getContentPane().getGraphics().drawRect(step*3, step, step, step*4);
				getContentPane().getGraphics().drawRect(step, step*3, step*4, step*2);
				getContentPane().getGraphics().drawRect(step, step*2, step*4, step*2);

		}
	}
	
	
	public static void main(String... args) {
		MainWindow mainWindow = new MainWindow();
	}
}
