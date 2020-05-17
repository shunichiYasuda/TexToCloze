package com.genSci.tools.MoodleClozeHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PrimaryController {
	String str = "";
	@FXML TextArea srcArea;
	@FXML TextArea modArea;
    @FXML
    private void quitAction() {
    	System.exit(0);
    }
    @FXML
    private void clearScrArea() {
    	srcArea.clear();
    	str = "";
    }
    @FXML
    private void clearModArea() {
    	modArea.clear();
    }
    @FXML
    private void execAction() {
    	str = srcArea.getText();
    	String regex = "\\\\begin\\{enumerate}";
		str = replace(regex,str,"<ol>");
		regex = "\\\\end\\{enumerate}";
		str = replace(regex,str,"</ol>");
		str = str.replace("{\\toi}", "{1:NM:=1}");
		str = str.replace("\\item", "<li>");
		str = replace("\\[label.+?\\]",str,"");
		str = replace("\\\\ban\\{.+?}",str,"");
		regex = "\\\\begin\\{tabular}\\{.+?}";
		str =replace(regex,str,"<table>");
		regex = "\\\\end\\{tabular}";
		str = replace(regex,str,"</table>");
    	regex = "\\$.+?\\$";
    	str =str.replaceAll(" (\\$.+?\\$)", "\\\\($1\\\\)");
    	str = str.replaceAll("\\$", "");
    	modArea.setText(str);
    }
    //
    private String replace(String regex, String str, String rep) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while(m.find()) {
			str = str.replace(m.group(), rep);
		}
		return str;
	}
} //end of PrimaryController.class

