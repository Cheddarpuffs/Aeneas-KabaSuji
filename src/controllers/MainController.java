package controllers;

import javax.annotation.PostConstruct;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import controllers.components.PlayLevelController;

import com.jfoenix.controls.JFXRippler;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

@FXMLController(value = "/resources/fxml/Main.fxml", title = "Material Design Example")
public class MainController {

	@FXMLViewFlowContext
	private ViewFlowContext context;

	@FXML
	private StackPane root;
	@FXML
	private StackPane content;

	@FXML
	private StackPane optionsBurger;
	@FXML
	private JFXRippler optionsRippler;

	@FXML
	private JFXPopup toolbarPopup;

	private FlowHandler flowHandler;

	@PostConstruct
	public void init() throws FlowException, VetoException {
		// init Popup
		toolbarPopup.setPopupContainer(root);
		toolbarPopup.setSource(optionsRippler);
		optionsBurger.setOnMouseClicked((e) -> {
			toolbarPopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, -12, 15);
		});

		// create the inner flow and content
		context = new ViewFlowContext();

		// set the default controller 
		Flow innerFlow = new Flow(PlayLevelController.class);

		flowHandler = innerFlow.createHandler(context);
		context.register("ContentFlowHandler", flowHandler);
		context.register("ContentFlow", innerFlow);
		context.register("ContentPane", content);
		content.getChildren().add(flowHandler.start(new DefaultFlowContainer()));

	}
}