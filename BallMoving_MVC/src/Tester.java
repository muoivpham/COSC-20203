
public class Tester {
	public static void main(String[] args){
		TheView view = new TheView();
		TheModel model = new TheModel();
		TheController controller = new TheController(view, model);
		view.setVisible(true);
	}
	
}
