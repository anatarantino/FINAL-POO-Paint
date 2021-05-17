package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	ToggleButton circleButton = new ToggleButton("Círculo");
	ToggleButton squareButton = new ToggleButton("Cuadrado");
	ToggleButton ellipseButton = new ToggleButton("Elipse");
	ToggleButton lineButton = new ToggleButton("Línea");
	Button removeButton = new Button("Borrar");
	Button backButton = new Button("Al Fondo");
	Button frontButton = new Button("Al Frente");
	//Labels
	Label borderLabel=new Label("Borde");
	Label fillLabel=new Label("Relleno");

	//Border slider
	Slider slider = new Slider(1, 50, 1);

	//Color Picker
	ColorPicker borderColorPicker = new ColorPicker(Color.BLACK);
	ColorPicker fillColorPicker = new ColorPicker(Color.YELLOW);

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	List<Figure> selectedFigures = new ArrayList<>();

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, lineButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999999");
		buttonsBox.setPrefWidth(100);

		removeButton.setMinWidth(90);
		frontButton.setMinWidth(90);
		backButton.setMinWidth(90);

		buttonsBox.getChildren().add(removeButton);
		buttonsBox.getChildren().add(backButton);
		buttonsBox.getChildren().add(frontButton);

		slider.setMajorTickUnit(25);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);



		buttonsBox.getChildren().add(borderLabel);
		buttonsBox.getChildren().add(slider);
		buttonsBox.getChildren().add(borderColorPicker);
		buttonsBox.getChildren().add(fillLabel);
		buttonsBox.getChildren().add(fillColorPicker);


		gc.setLineWidth(1);
		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
			if(event.getButton() == MouseButton.SECONDARY){
				return;
			}
		});
		canvas.setOnMouseReleased(event -> {
			if(event.getButton() == MouseButton.SECONDARY){
				return;
			}

			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(selectionButton.isSelected()){
				selectedFigures.clear();
				Rectangle selectionRectangle = new Rectangle(startPoint, endPoint, Color.WHITE, Color.WHITE, 1);
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for(Figure fig : canvasState) {
					if (fig.isContained(selectionRectangle)) {
						selectedFigures.add(fig);
						label.append(fig.toString() + ", ");
					}
				}
				if(selectedFigures.isEmpty()){
					statusPane.updateStatus("No se seleccionó ninguna figura");
				}else{
					label.setLength(label.length()-2);
					statusPane.updateStatus(label.toString());
				}
				redrawCanvas();
				startPoint = null;
				return;
			}
			Figure newFigure;
			if(lineButton.isSelected()) {
				newFigure = new Line(startPoint, endPoint, borderColorPicker.getValue(), fillColorPicker.getValue(), slider.getValue());
			}
			else {
				if (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
					return;
				}

				if (rectangleButton.isSelected()) {
					newFigure = new Rectangle(startPoint, endPoint, borderColorPicker.getValue(), fillColorPicker.getValue(), slider.getValue());
				} else if (circleButton.isSelected()) {
					newFigure = new Circle(startPoint, endPoint,borderColorPicker.getValue(), fillColorPicker.getValue(),slider.getValue());
				} else if (squareButton.isSelected()) {
					newFigure = new Square(startPoint, endPoint,borderColorPicker.getValue(), fillColorPicker.getValue(), slider.getValue());
				}else if (ellipseButton.isSelected()){
					newFigure = new Ellipse(startPoint, endPoint,borderColorPicker.getValue(), fillColorPicker.getValue(), slider.getValue());
				}else {
					return;
				}
			}
			canvasState.add(newFigure);
			startPoint = null;
			redrawCanvas();
		});
		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState) {
				if(figure.hasPoint(eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				if(event.getButton() == MouseButton.SECONDARY){
					return;
				}
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				String label = " ";
				if(selectedFigures.isEmpty()) {
					for (Figure figure : canvasState) {
						if (figure.hasPoint(eventPoint)) {
							found = true;
							selectedFigures.clear();
							selectedFigures.add(figure);
							label = "Se seleccionó: " + figure.toString();
							slider.setValue(figure.getLineSize());
							borderColorPicker.setValue(figure.getLineColor());
							fillColorPicker.setValue(figure.getFillColor());

						}
					}
					if (found) {
						statusPane.updateStatus(label);
					}
					redrawCanvas();
				}
			}
		});
		canvas.setOnMouseDragged(event -> {
			if(event.getButton() == MouseButton.SECONDARY){
				return;
			}
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				for(Figure fig : selectedFigures) {
					fig.move(diffX, diffY);
					redrawCanvas();
				}
			}
		});

		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(Figure fig : selectedFigures) {
					canvasState.remove(fig);
					redrawCanvas();
				}
			}
		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(Figure fig : selectedFigures){
					canvasState.remove(fig);
					canvasState.addFirst(fig);
				}
				redrawCanvas();
			}
		});

		frontButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(Figure fig : selectedFigures){
					canvasState.remove(fig);
					canvasState.add(fig);
				}
				redrawCanvas();
			}
		});
		//Hacemos que cada opción escuche cambios y si hay una figura seleccionada, cambie el valor
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				for(Figure fig : selectedFigures){
					fig.setLineSize(slider.getValue());
					redrawCanvas();
				}
			}
		});

		borderColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				for(Figure fig: selectedFigures){
					fig.setLineColor(newValue);
					redrawCanvas();
				}
			}
		});

		fillColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				for(Figure fig : selectedFigures){
					fig.setFillColor(newValue);
					redrawCanvas();
				}
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState) {
			if(figure != null) {
				figure.draw(gc, selectedFigures.contains(figure));
			}
		}
	}



}
