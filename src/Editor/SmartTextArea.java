package Editor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import javafx.geometry.Point2D;
import javafx.scene.control.*;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class SmartTextArea extends TextArea {
    private static final int NUM_COMPLETIONS = 6;
    private static final int NUM_SUGGESTIONS = 6;

    private boolean needUpdate = true;

    private boolean matchCase = true;

    private int startIndex;
    private int endIndex;

    private List<String> options;

    private ContextMenu entriesPopup;

    private AutoComplete autoComp;
    private SpellingSuggest spellSug;
    private Dictionary dict;

    public SmartTextArea(AutoComplete ac, SpellingSuggest ss, Dictionary dict) {
        super();
        this.autoComp = ac;
        this.spellSug = ss;
        this.dict = dict;
        entriesPopup = new ContextMenu();
        options = new ArrayList<String>();

        /*
        this.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {

                int index = this.getCaretPosition();
                String possibleWord = getWordAtIndex(index);
                showSuggestions(possibleWord, e);
            }
        });
        */

        this.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (e.getClickCount() == 2) {
                    String word = getWordAtIndex(this.getCaretPosition());
                    showSuggestions(word, e);
                }
            }
        });

        this.caretPositionProperty().addListener(e -> {
            //get cursor position
            TextInputControl control = (TextInputControl) this;
            Point2D pos = control.getInputMethodRequests().getTextLocation(0);

            String prefix = getWordAtIndex(this.getCaretPosition());
            showCompletions(prefix, pos);
        });
    }

    private String getWordAtIndex(int i) {
        String text = this.getText().substring(0, i);
        int index;

        for (index = text.length() - 1; index >= 0 && !Character.isWhitespace(text.charAt(index)); index--);
        startIndex = index + 1;
        String prefix = text.substring(startIndex, text.length());

        for (index = i; index < this.getLength() && !Character.isWhitespace(this.getText().charAt(index)); index++);
        String suffix = this.getText().substring(i, index);
        endIndex = index;

        prefix = prefix.replaceAll("\\.", "\\.");
        suffix = suffix.replaceAll("\\.", "\\.");

        prefix = prefix + suffix;

        return prefix;
    }

    private void showSuggestions(String word, MouseEvent click) {
        List<String> suggestions = spellSug.suggestions(word, NUM_SUGGESTIONS);
        boolean[] caseFlags = null;

        Label sugLabel;
        CustomMenuItem item;
        entriesPopup.getItems().clear();

        List<CustomMenuItem> menuItems = createOptions(suggestions, caseFlags);

        if (suggestions.size() == 0) {
            sugLabel = new Label("No suggestions.");
            item = new CustomMenuItem(sugLabel, true);
            item.setDisable(true);
            ((LinkedList<CustomMenuItem>) menuItems).addFirst(item);
        }

        sugLabel = new Label("Add to dictionary");
        item = new CustomMenuItem(sugLabel, true);
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dict.addWord(word);
                ((Dictionary)autoComp).addWord(word);
                setStyle(word);
            }
        });
        menuItems.add(item);

        entriesPopup.getItems().addAll(menuItems);
        entriesPopup.show(getScene().getWindow(), click.getScreenX(), click.getScreenY());
    }

    private List<CustomMenuItem> createOptions(List<String> suggestions, boolean[] caseFlags) {
        List<CustomMenuItem> menuItems = new LinkedList<CustomMenuItem>();
        int numToAdd = Math.min(suggestions.size(), NUM_COMPLETIONS);

        for (int i = 0; i < numToAdd; i++) {
            final String word = suggestions.get(i);
            Label opt = new Label(word);
            CustomMenuItem item = new CustomMenuItem(opt, true);

            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    needUpdate = false;
                    replaceText(startIndex, endIndex, word);
                    getWordAtIndex(startIndex); //To update the startIndex and endIndex
                    setStyle(word);
                    needUpdate = true;
                }
            });

            menuItems.add(item);
        }

        return menuItems;
    }

    private void showCompletions(String prefix, Point2D pos) {
        if (!prefix.equals("")) {
            options = autoComp.predictCompletions(prefix, NUM_COMPLETIONS);

            if (options.size() > 0) {
                boolean[] caseFlags = null;
                if (matchCase) {
                    //TODO
                }

                List<CustomMenuItem> menuItems = createOptions(options, caseFlags);
                entriesPopup.getItems().clear();
                entriesPopup.getItems().addAll(menuItems);
                if (!entriesPopup.isShowing()) {
                    //entriesPopup.show(getScene().getWindow());
                    entriesPopup.show(getScene().getWindow(), pos.getX(), pos.getY());
                }
            }else {
                entriesPopup.hide();
            }
        }else {
            entriesPopup.hide();
        }
    }
}
