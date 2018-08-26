package pl.wozniaktomek.widget;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Widget {
    private HashMap<WidgetStyle, ArrayList<String>> styles;

    private VBox mainContainer;
    private HBox titleContainer;
    private HBox titleTextContainer;
    VBox contentContainer;

    private Text title;
    private boolean isMinimized;
    private Button minimizationButton;

    Widget() {
        initializeStyles();
        initializeMainContainer();
        initializeTitleContainer();
        initializeTitleTextContainer();
        initializeMinimizationButton();
        initializeContentContainer();
        initializeWidgetSizeListener();
        setMinimizationVisibility(true);
        setStyle(WidgetStyle.PRIMARY);
    }

    public VBox getWidget() {
        return mainContainer;
    }

    void setStyle(WidgetStyle widgetStyle) {
        for (WidgetStyle style : WidgetStyle.values()) {
            if (widgetStyle.equals(style)) {
                mainContainer.getStyleClass().add(styles.get(style).get(0));
                titleContainer.getStyleClass().add(styles.get(style).get(1));
            } else {
                mainContainer.getStyleClass().remove(styles.get(style).get(0));
                titleContainer.getStyleClass().remove(styles.get(style).get(1));
            }
        }
    }

    void setTitle(String titleText) {
        title.setText(titleText);
    }

    void setMinimizationVisibility(boolean isButtonVisible) {
        minimizationButton.setVisible(isButtonVisible);
    }

    private void minimizeWidget() {
        contentContainer.setVisible(false);
        setMainContainerHeight(96d);
        minimizationButton.setText("+");
        isMinimized = true;
    }

    private void maximizeWidget() {
        contentContainer.setVisible(true);
        setMainContainerHeight(null);
        minimizationButton.setText("-");
        isMinimized = false;
    }

    private void initializeStyles() {
        styles = new HashMap<>();

        ArrayList<String> styleClasses = new ArrayList<>();
        styleClasses.add("widget-primary");
        styleClasses.add("widget-primary-background-fill");
        styles.put(WidgetStyle.PRIMARY, styleClasses);

        styleClasses = new ArrayList<>();
        styleClasses.add("widget-secondary");
        styleClasses.add("widget-secondary-background-fill");
        styles.put(WidgetStyle.SECONDARY, styleClasses);

        styleClasses = new ArrayList<>();
        styleClasses.add("widget-success");
        styleClasses.add("widget-success-background-fill");
        styles.put(WidgetStyle.SUCCESS, styleClasses);

        styleClasses = new ArrayList<>();
        styleClasses.add("widget-failure");
        styleClasses.add("widget-failure-background-fill");
        styles.put(WidgetStyle.FAILURE, styleClasses);
    }

    private void initializeMainContainer() {
        mainContainer = new VBox();
        mainContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        isMinimized = false;
    }

    private void initializeTitleContainer() {
        titleContainer = new HBox();
        titleContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        titleContainer.setPadding(new Insets(12));
        titleContainer.setSpacing(12d);
        mainContainer.getChildren().add(titleContainer);
    }

    private void initializeTitleTextContainer() {
        titleTextContainer = new HBox();

        title = new Text("Widget");
        title.getStyleClass().add("section-title-background");

        titleTextContainer.getChildren().add(title);
        titleContainer.getChildren().add(titleTextContainer);
    }


    private void initializeMinimizationButton() {
        minimizationButton = getButton("-", 32d);
        minimizationButton.getStyleClass().add("button-white");
        minimizationButton.setOnAction(event -> {
            if (isMinimized) {
                maximizeWidget();
            } else {
                minimizeWidget();
            }
        });

        titleContainer.getChildren().add(minimizationButton);
    }


    private void initializeContentContainer() {
        contentContainer = new VBox();
        contentContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        contentContainer.setPadding(new Insets(12));
        contentContainer.setSpacing(12d);

        mainContainer.getChildren().add(contentContainer);
    }

    private void initializeWidgetSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> titleTextContainer.setPrefWidth(contentContainer.getWidth() - 72);
        contentContainer.widthProperty().addListener(stageSizeListener);
    }

    private void setMainContainerHeight(Double height) {
        if (height != null) {
            mainContainer.setMaxHeight(height);
            mainContainer.setMinHeight(height);
            mainContainer.setPrefHeight(height);
        } else {
            mainContainer.setMaxHeight(Region.USE_COMPUTED_SIZE);
            mainContainer.setMinHeight(Region.USE_COMPUTED_SIZE);
            mainContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        }

    }

    HBox getHBoxContainer() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(2, 12, 2, 12));
        hBox.setSpacing(12);
        return hBox;
    }

    Button getButton(String text, Double size) {
        Button button = new Button();
        button.setText(text);

        if (size != null) {
            button.setPrefSize(size, size);
        }

        return button;
    }

    TextFlow getActionText(String name) {
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(96d);
        textFlow.setPadding(new Insets(6, 0, 6, 0));
        textFlow.setTextAlignment(TextAlignment.LEFT);

        Text text = new Text(name);
        text.getStyleClass().add("action-status");

        textFlow.getChildren().add(text);
        return textFlow;
    }

    Text getActionBoldText(String name) {
        Text text = new Text(name);
        text.getStyleClass().add("action-bold-status");
        return text;
    }

    public enum WidgetStyle {PRIMARY, SECONDARY, SUCCESS, FAILURE}
}
