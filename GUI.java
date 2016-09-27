package application;
import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * This program is the Graphical representation for the word counter 
 * 
 * @version 1.0
 * 
 * @author Greg
 *
 */

public class GUI extends Application {
	
	private Button fileSelect;			//File select button
	private FileChooser fileChooser;	//The file chooser window
	private ReadCount readcount;		//The Back end file reader
	private TextArea display;			//Where the information is Displayed
	private String fileName;
	
	
	
	/**
	 * Default Constructor
	 */
	public GUI(){
		fileSelect = new Button();
		fileChooser = new FileChooser();
		readcount = null;
		display = new TextArea();
		fileName = null;
	}
	
	/**
	 * This is where the main GUI is set up, initialized and displayed.
	 * It's also where i'm setting up the Text Area, DragandDrop, and the fileChooser.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//textfield
			initializeDragDrop();
			display.setEditable(false);
			
			//button
			fileSelect = new Button("Select File");
			
			//file Selection
			fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("Text", "*.txt"));
			
			//on select action listener
			fileSelect.setOnAction(
		            new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                	
		                	//incase the user selected cancel
		                	try{
		                	//where the file is selected from the filechooser
		                    File file = fileChooser.showOpenDialog(primaryStage);
		                    
		                    //saving the name.
		                    fileName = file.getName();
		                    
		                    //call to readCount to read the file.
		                    readcount = new ReadCount(file);
		                    
		                    //change the display to what the ReadCount returns
		                    changeDisplay(readcount.toString());
		                	}
		                	catch(Exception exception){
		                		System.out.println();
		                	}
		                }
		            });
				
			//adding the button to the top and centering it
			FlowPane flow = new FlowPane();
			flow.getChildren().add(fileSelect);
			root.setTop(flow);
			root.getTop().setId("TOP");
			root.getTop().applyCss();
			
			//adding the textfield to the middle
			root.setCenter(display);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This Method changes the display to the current file's output
	 * @param input from the ReadCount file
	 */
	public void changeDisplay(String input){
		String text = input.replace(',', '\n').replace('{', (char) 000).replace('}', (char)000).replaceAll("[=]", " = ");
		display.setText(fileName.replace(".txt", "") + "\n------------\n" +text);
	}
	
	/**
	 * Sets up the Drag Over, Drag Dropped, and Drag Exit event listeners.
	 */
	private void initializeDragDrop(){
		//drag over event
		display.setOnDragOver(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				dragOver(event);
			}
		});
		
		//dropping the file in the field
		display.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(final DragEvent event) {
	            	dragDrop(event);
	            }
	        });
		
		//resetting the border
         display.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                display.setStyle("-fx-border-color: #C6C6C6;");
            }
        });
	}
	
	/**
	 * Deals with the dragover event. It changes the border of the Text field to show the user that
	 * they have a valid file to drop.
	 * @param event -- caught by the dragover action Listener
	 */
	private void dragOver(DragEvent event){
		final Dragboard db = event.getDragboard();
		 
        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".txt");
 
        if (db.hasFiles()) {
            if (isAccepted) {
            	 display.setStyle("-fx-border-color: limegreen;"
			              + "-fx-border-width: 5;"
			              + "-fx-background-color: #C6C6C6;"
			              + "-fx-border-style: solid;");
                event.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            event.consume();
        }	
	}
	/**
	 * Deals with the file when it's dropped in.
	 * @param event -- by caught from the DragDrop Event
	 */
	private void dragDrop(DragEvent event){
		final Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
        	if(db.getFiles().size()==1){
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            fileName = file.getName();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    readcount = new ReadCount(file);
                    changeDisplay(readcount.toString());
                }
            });
        	}
        }
        else
        	System.out.println("Error, one file at a time");
        event.setDropCompleted(success);
        event.consume();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
