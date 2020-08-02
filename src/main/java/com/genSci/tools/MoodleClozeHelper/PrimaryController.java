package com.genSci.tools.MoodleClozeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PrimaryController {
	String str = "";
	@FXML
	TextArea srcArea;
	@FXML
	TextArea modArea;

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
		// str を頭から1行ずつ読めるか？
		// 改行コードで分割できる？
		String[] originalStr = str.split("\n");
		System.out.println("----original----");
		for (String s : originalStr) {
			// System.out.println(s);
		}
		System.out.println("----original end -----");
		for (String s : originalStr) {
			if (s.contains("選択肢}")) {
				System.out.println("hit:" + s);
			}

		}
		//
		String regex = "\\\\begin\\{enumerate}";
		// \begin{enumerate}から\end{enumerate}までを取り出せるか
		String beginStr = "\\\\begin\\{enumerate}";
		String endStr = "\\\\end\\{enumerate}";
		str = replace(beginStr, str, "<ol>");
		str = replace(endStr, str, "</ol>");
		// str = replace("\\{\\\\toi\\}",str,"<q>");
		// enumerate 環境下の「設問」を保存
		str = replace("\\\\item", str, "<li>");
		regex = "<li>(.+?)\n";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		List<String> questionList = new ArrayList<String>();
		while (m.find()) {
			String str = m.group(1);
			questionList.add(str);
		}
		for (String s : questionList) {
			System.out.println(s);
		}
		// tabular 環境下の「正解」を保存
		String tableStr;
		regex = "\\\\begin\\{tabular}\\{.+?}";
		tableStr = replace(regex, str, "<table>");
		regex = "\\\\end\\{tabular}";
		tableStr = replace(regex, tableStr, "</table>");
		regex = "正解}(.+?)</table>";
		p = Pattern.compile(regex, Pattern.DOTALL);
		m = p.matcher(tableStr);
		String ansStr = null;
		List<String> ansList = new ArrayList<String>();
		while (m.find()) {
			ansStr = m.group(1);
			ansStr = ansStr.replace("\\","");
			modArea.appendText(ansStr+"\n");
			regex = "ban\\{.+?}";
			Pattern p2 = Pattern.compile(regex);
			Matcher m2 = p2.matcher(ansStr);
			
			while (m2.find()) {
				String subStr = m2.group();
				subStr = subStr.replace("ban{", "");
				subStr = subStr.replace("}", "");
				ansList.add(subStr);
				modArea.appendText("sub="+subStr+"\n");
			}
		}
		for(String s:ansList) {
			modArea.appendText(s+"\n");
		}
		modArea.appendText(ansStr);

		/*
		 * str = replace(regex,str,"<ol>"); regex = "\\\\end\\{enumerate}"; str =
		 * replace(regex,str,"</ol>"); regex ="\\\\begin\\{description}"; str =
		 * replace(regex,str,"<ul>"); regex = "\\\\end\\{description}"; str =
		 * replace(regex,str,"</ul>"); str = str.replace("{\\toi}", "{1:NM:=1}");
		 * //\item から改行コードまでを取り出す。 //どうやら "\\item(.+?)\n"という表現は機能しないので、\item を<li>に替えておく
		 * str = str.replace("\\item", "<li>"); regex = "<li>(.+?)\n"; Pattern p =
		 * Pattern.compile(regex); Matcher m = p.matcher(str); List<String> questionList
		 * = new ArrayList<String>(); while(m.find()) { String str = m.group(1);
		 * questionList.add(str); } str = replace("\\[label.+?\\]",str,""); // regex
		 * ="\\\\ban\\{.+?}"; p = Pattern.compile(regex); m = p.matcher(str);
		 * while(m.find()) { String subStr =m.group(); String newStr
		 * =subStr.replace("\\ban{", ""); newStr = newStr.replace("}", "");
		 * //System.out.println(newStr); //newStr = "."+newStr; str =
		 * str.replace(subStr, newStr+"."); } //str = replace("\\\\ban\\{.+?}",str,"");
		 * regex = "\\\\begin\\{tabular}\\{.+?}"; str
		 * =replace(regex,str,"<table><caption><b>選択肢</b></caption>\n<tr>\n<td>"); regex
		 * = "\\\\end\\{tabular}"; str = replace(regex,str,"</table>"); str =
		 * str.replaceAll("\\{\\\\bf.+?選択肢}",""); str = str.replaceAll("\\\\", "");
		 * regex = "\\$.+?\\$"; str =str.replaceAll("(\\$.+?\\$)", "\\\\($1\\\\)"); str
		 * = str.replaceAll("\\$", ""); str = str.replaceAll("&","</td><td>"); //
		 */
		// modArea.setText(str);
	}

	//
	private String replace(String regex, String str, String rep) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			str = str.replace(m.group(), rep);
		}
		return str;
	}
} // end of PrimaryController.class
