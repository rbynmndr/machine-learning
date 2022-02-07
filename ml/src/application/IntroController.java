package application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class IntroController extends ImageProcessing implements Initializable {

	@FXML
	private ImageView imgView;
	@FXML
	private Label lbl, lbl0, lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, lbl7, lbl8, lbl9, lblPred;
	@FXML
	private ProgressBar pb;
	@FXML
	private ProgressIndicator pi;
	@FXML
	private Button btnTrain;
	@FXML
	private Button btnCancel;
	@FXML
	private TextField txtNum, txtNumTest;
	@FXML
	private Canvas canvas;	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		txtNum.setFont(Nepali(22));
		
		txtNumTest.setFont(Nepali(22));
		lblPred.setFont(Nepali(72));
		lblPred.setStyle("-fx-font-weight: bold");
	}
	
	private Font Nepali(int fontSize) {
		return Font.loadFont("file:resources/fonts/preeti.otf", fontSize);
	}
	
	NeuralNetwork n = new NeuralNetwork(784, 100, 10, 0.2);
	
	Task<Void> task = new Task<Void>() {

		@Override
		protected Void call() throws Exception {
			// TODO Auto-generated method stub
			try (BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\mnist_train.csv")))) {

		        String in = reader.readLine();
		        List<String> lines = new ArrayList<String>();
		        while(in != null) {
		        	lines.add(in);
		        	in = reader.readLine();
		        }
		        reader.close();
		        int x = 1;
		        for (String line : lines) {
					String[] all = line.split(",");
					double[][] inputLists = new double[1][784];
					double[][] targetLists = new double[1][10];
					for(int i=1; i<all.length; i++) {
						inputLists[0][i-1] = (Double.parseDouble(all[i]) / 255 * 0.99) + 0.01;
					}
					for(int i=0; i<10; i++) {
						targetLists[0][i] = 0.01;
					}
					//all[0] contain the target label.
					targetLists[0][Integer.parseInt(all[0])] = 0.99;
					n.train(inputLists, targetLists);
					updateProgress(x, lines.size());
					x++;
					Thread.sleep(100);
				}
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
			return null;
		}
		
	};
	
	public void trainNumber(ActionEvent e) {
		WritableImage img = new WritableImage(150, 150);
		canvas.snapshot(null, img);
		BufferedImage buffImg = SwingFXUtils.fromFXImage(img, null);
		BufferedImage processImg = processedImage(buffImg);
		
		double[][] inputLists = imageToArray(processImg);
		double[][] targetLists = setOutputArray(txtNum.getText());
		
		imgView.setImage(SwingFXUtils.toFXImage(processImg, null));
		n.train(inputLists, targetLists);
		lbl.setText("Trained.");
	}
	
	public void train(ActionEvent e) {
		btnTrain.setDisable(true);
		btnCancel.setDisable(false);
		pb.progressProperty().unbind();
		pb.progressProperty().bind(task.progressProperty());
		pi.progressProperty().unbind();
		pi.progressProperty().bind(task.progressProperty());
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				// TODO Auto-generated method stub
				btnTrain.setDisable(false);
				btnCancel.setDisable(true);
			}
		});
		new Thread(task).start();
	}
	
	public void cancel(ActionEvent e) {
		btnTrain.setDisable(false);
		btnCancel.setDisable(true);
		task.cancel(true);
		pb.progressProperty().unbind();
		pi.progressProperty().unbind();

		pb.setProgress(0);
		pi.setProgress(0);
	}
	
	public void test(ActionEvent e) {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\mnist_test.csv")))) {
			String in = reader.readLine();
	        List<String> lines = new ArrayList<String>();
	        while(in != null) {
	        	lines.add(in);
	        	in = reader.readLine();
	        }
	        reader.close();
	        String[] Inputline = new String[785];
	        for(int i=0; i<lines.size(); i++) {
	        	if(lines.get(i).charAt(0) == txtNumTest.getText().toCharArray()[0]) Inputline = lines.get(i).split(",");
	        }
	        double[][] inputLists = new double[1][784];
	        for(int i=1; i<Inputline.length; i++) {
	        	inputLists[0][i-1] = (Double.parseDouble(Inputline[i]) / 255 * 0.99) + 0.01;
	        }
	        BufferedImage img = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
	        int x = 1;
	        for(int i=0; i<28; i++) {
	        	for(int j=0; j< 28; j++) {
	        		int rgb = Integer.parseInt(Inputline[x]);
	        		img.setRGB(j, i, new Color(rgb,rgb,rgb).getRGB());
	        		System.out.print(Inputline[x] + "\t");
	        		x++;
	        	}
	        	System.out.println();
	        }
	        System.out.println();
	        Image sceneImage = SwingFXUtils.toFXImage(img, null);
	        imgView.setImage(sceneImage);
	        double[][] outputResult = n.query(inputLists);
	        lbl0.setText(String.format("%.2f", outputResult[0][0] * 100));
			lbl1.setText(String.format("%.2f", outputResult[1][0] * 100));
			lbl2.setText(String.format("%.2f", outputResult[2][0] * 100));
			lbl3.setText(String.format("%.2f", outputResult[3][0] * 100));
			lbl4.setText(String.format("%.2f", outputResult[4][0] * 100));
			lbl5.setText(String.format("%.2f", outputResult[5][0] * 100));
			lbl6.setText(String.format("%.2f", outputResult[6][0] * 100));
			lbl7.setText(String.format("%.2f", outputResult[7][0] * 100));
			lbl8.setText(String.format("%.2f", outputResult[8][0] * 100));
			lbl9.setText(String.format("%.2f", outputResult[9][0] * 100));
			double max = Double.MIN_VALUE;
			Integer a = 0;
			for(int i = 0; i<outputResult.length; i++) {
				if(outputResult[i][0] > max) {
					max = outputResult[i][0];
					a = i;
				}
			}
			lblPred.setText(a.toString());
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void testDrawing(ActionEvent e) {
		WritableImage img = new WritableImage(150, 150);
		canvas.snapshot(null, img);
		BufferedImage buffImg = SwingFXUtils.fromFXImage(img, null);
		BufferedImage processImg = processedImage(buffImg);
		
		double[][] outputLists = imageToArray(processImg);
		imgView.setImage(SwingFXUtils.toFXImage(processImg, null));
		
		double[][] outputResult = n.query(outputLists);
		lbl0.setText(String.format("%.2f", outputResult[0][0] * 100));
		lbl1.setText(String.format("%.2f", outputResult[1][0] * 100));
		lbl2.setText(String.format("%.2f", outputResult[2][0] * 100));
		lbl3.setText(String.format("%.2f", outputResult[3][0] * 100));
		lbl4.setText(String.format("%.2f", outputResult[4][0] * 100));
		lbl5.setText(String.format("%.2f", outputResult[5][0] * 100));
		lbl6.setText(String.format("%.2f", outputResult[6][0] * 100));
		lbl7.setText(String.format("%.2f", outputResult[7][0] * 100));
		lbl8.setText(String.format("%.2f", outputResult[8][0] * 100));
		lbl9.setText(String.format("%.2f", outputResult[9][0] * 100));
		double max = Double.MIN_VALUE;
		Integer a = 0;
		for(int i = 0; i<outputResult.length; i++) {
			if(outputResult[i][0] > max) {
				max = outputResult[i][0];
				a = i;
			}
		}
		lblPred.setText(getNepaliNumberFromNumber(Integer.parseInt(a.toString())));
	}
	
	public void dragged(MouseEvent e) {
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.lineTo(e.getX(), e.getY());
		graphicsContext.stroke();
	}
	
	public void released(MouseEvent e) {
	}
	
	public void pressed(MouseEvent e) {
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.beginPath();
		graphicsContext.moveTo(e.getX(), e.getY());
		graphicsContext.stroke();
	}
	
	public void initDraw() {
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(javafx.scene.paint.Color.BLACK);
        graphicsContext.setLineWidth(7);
	}
	
	public void clearCanvas(ActionEvent e) {
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();
        graphicsContext.setStroke(javafx.scene.paint.Color.BLACK);
        graphicsContext.setLineWidth(7);
        graphicsContext.fill();
        graphicsContext.clearRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle
	}

}
