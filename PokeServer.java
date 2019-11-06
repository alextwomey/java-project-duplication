import java.awt.*;
import javax.swing.*;

public class TEMPLATE extends JFrame {

	public static void main(String[] args) {
		new TEMPLATE();
	}

	public TEMPLATE() {
		setupWindow();

		this.setVisible(true);
	}

	public void setupWindow() {
		this.setTitle("TEMPLATE");
		this.setSize(400, 400);
		this.setLocation(100,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
