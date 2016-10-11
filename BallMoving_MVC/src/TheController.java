import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TheController {
	private TheView view;
	private TheModel model;
	
	public TheController( TheView view, TheModel model){
		this.view = view;
		this.model = model;
		
		this.view.addUpActionListener(new ButtonUpActionListener());
		this.view.addDownActionListener(new ButtonDownActionListener() );
		this.view.addRightActionListener(new ButtonRightActionListener());
		this.view.addLeftActionListener(new ButtonLeftActionListener());
	}
	
	class ButtonUpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("test UpButton");
			// TODO Auto-generated method stub
			model.moveUp();
			view.setRow(model.getRow());
			view.updateButton();
		}
		
	}
	class ButtonDownActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			model.moveDown();
			view.setRow(model.getRow());
			view.updateButton();
		}
		
	}
	class ButtonLeftActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			model.moveLeft();
			view.setcolumn(model.getColumn());
			view.updateButton();
		}	
	}
	class ButtonRightActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			model.moveRight();
			view.setcolumn(model.getColumn());
			view.updateButton();
		}
		
	}
}
