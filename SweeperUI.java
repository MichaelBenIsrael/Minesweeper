package mines;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class SweeperUI {
	private Mines m;
	
    @FXML
    private StackPane root;

    @FXML
    private Button resetButton;

    @FXML
    private Label label1;

    @FXML
    private TextField widthInput;

    @FXML
    private Label label2;

    @FXML
    private TextField heightInput;

    @FXML
    private ProgressBar bar;
    
    @FXML
    private Label label3;

    @FXML
    private TextField minesInput;

    @FXML
    private StackPane spane;

    //extended button class, contains x,y location
	private class ButtonXy extends Button{
		private int x,y;
		public ButtonXy(int x, int y) {
			super();
			this.x=x;
			this.y=y;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}

	}
	//progress bar progress value:
	private int prog,max;
	//methods:
    @FXML
    void reset(ActionEvent event) {
    	//sets up a new mines game with the given values on the stage
    	int height,width,mines;
		height=width=mines=0;
    	try {
    	height=Integer.parseInt(heightInput.getText());
    	width=Integer.parseInt(widthInput.getText());
    	mines=Integer.parseInt(minesInput.getText());
    	m=new Mines(height,width,mines);
    	}catch(Exception e) {
        	m=new Mines(height,width,mines);
    	}
    	//progress bar init:
    	prog=0;
    	max=(height*width)-mines;
    	bar.setProgress((double)prog/max);
    	System.out.println(bar.getStyle());
    	//grid init
    	GridPane grid=new GridPane();
    	//following line removes the previous spane children incase of a several resets.
    	spane.getChildren().remove(0, spane.getChildren().size());
    	spane.getChildren().add(grid);
    	//setting up buttons:
    	for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				ButtonXy b=new ButtonXy(i, j);
				b.resize(20, 20);
				b.setStyle("-fx-font-weight: bold");
				b.setText(".");
				b.setOnMouseClicked(new ButtonClick());
				grid.add(b, j, i);
			}
		}
    	for(Node n:grid.getChildren()) {
    		((ButtonXy) n).setMinSize(50, 50);
    	}
    	//makes the stage resize automatically according the to grid size:
    	MinesFX.getStage().sizeToScene();
    }

	
	class ButtonClick implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			int x,y,cnt=0,gameover=0;
			x=((ButtonXy)event.getSource()).getX();
			y=((ButtonXy)event.getSource()).getY();
			//if left click:
			if (event.getButton() == MouseButton.PRIMARY)
            {
				
				if(m.get(x, y).equals("F")) return;
				if(m.open(x,y)==false) {
					m.setShowAll(true);
					gameover=1;
				}
				else {// if true, opened.
					m.open(x,y);
                	//change text:
					setView(((ButtonXy)event.getSource()),m.get(x, y));
                	//((ButtonXy)event.getSource()).setText(m.get(x, y));
                	}
            	//check all button values and fix them:
            	//spane only got grid, so we're grabbing him:
            	GridPane g= ((GridPane)spane.getChildren().get(0));
            	for(Node n:g.getChildren()) { 
            		String s=m.get(((ButtonXy)n).getX(),((ButtonXy)n).getY());
            		setView((Button)n,s);
            		/*
            		if(s.equals("F"))
            		{
                    	Button b=((ButtonXy)event.getSource());
                    	b.setText("");
                    	b.setGraphic(new ImageView(new Image("mines/flag.png",25,25,false,false)));
            		}
            		else {
            			((ButtonXy)n).setGraphic(null);
                		((ButtonXy)n).setText(s);
            		}
            		((ButtonXy)n).setText(s);*/
            		if(!s.contains(".") && !s.contains("F")) cnt++;
                }

            } 
			//if right click:
			else if (event.getButton() == MouseButton.SECONDARY)
            {
                m.toggleFlag(x, y);
                setView(((Button)event.getSource()),m.get(x,y));
            }
			//num of buttons in grid:
			int gridSize = ((GridPane)spane.getChildren().get(0)).getChildren().size();
			if(cnt>prog) prog=cnt;
			bar.setProgress((double)prog/gridSize);
			//check if lost/won:
			if(gameover==1) {
				//game is lost
				Alert alert = new Alert(AlertType.CONFIRMATION, "You just lost !\n Game over, restart?", ButtonType.YES, ButtonType.NO);
				Image image = new Image("https://s6.gifyu.com/images/ezgif.com-gif-makerf8a11a1dc60bcd77.gif");
				ImageView iv = new ImageView(image);
				alert.setGraphic(iv);
				alert.setTitle("Game over !");
				alert.setHeaderText(null);

				
				alert.showAndWait();
				if (alert.getResult() == ButtonType.YES) {
				    reset(null);
				}
			}
			else if(m.isDone()) {
				bar.setProgress(1);
				Alert alert = new Alert(AlertType.CONFIRMATION, "You just Won !\nrestart?", ButtonType.YES, ButtonType.NO);
				Image image = new Image("https://s6.gifyu.com/images/ezgif.com-gif-maker-1f47dcf78ebf3cebe.gif");
				ImageView iv = new ImageView(image);
				alert.setGraphic(iv);
				
				alert.setTitle("Congratulations !");
				alert.setHeaderText(null);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.YES) {
				    reset(null);
				}
				//game won
			}
		}
		
	}
	
	void setView(Button b, String s) {
		if(s.equals("F")) {
        	b.setText("");
        	b.setGraphic(new ImageView(new Image("mines/flag.png",25,25,false,false)));
		}
		else if(s.equals("X")) {
        	b.setText("X");
        	b.setGraphic(new ImageView(new Image("mines/bomb.png",25,25,false,false)));
		}
		else {
			b.setText(s);
			b.setGraphic(null);
		}
	}
}
