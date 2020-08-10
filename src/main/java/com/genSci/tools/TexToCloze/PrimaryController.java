package com.genSci.tools.TexToCloze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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
	private void copyToClipboard() {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(modArea.getText());
		clipboard.setContent(content);
	}
	@FXML
	private void execAction() {
		str = srcArea.getText();
		//\begin{enumerate} までの「問題文」を切り出す。
		String questionFirst=null;
		String regex = "(^.+?)\\\\begin";
		Pattern p =Pattern.compile(regex,Pattern.DOTALL);
		Matcher m= p.matcher(str);
		if(m.find()) {
			questionFirst = m.group(1);
		}
		//$$を\(\)に変える。
		regex = "\\$(.+?)\\$";
		p = Pattern.compile(regex);
		m = p.matcher(questionFirst);
		while (m.find()) {
			String ss = m.group(1);
			String rep ="\\\\("+ss+"\\\\)";
			questionFirst =questionFirst.replaceFirst(regex,rep);
		}
		//最初の問題文を書き出す。
		modArea.appendText(questionFirst+"\n");
		// \begin{enumerate}から\end{enumerate}までを取り出せるか
		String beginStr = "\\\\begin\\{enumerate}";
		String endStr = "\\\\end\\{enumerate}";
		regex = beginStr+"(.+?)"+endStr;
		p = Pattern.compile(regex,Pattern.DOTALL);
		m = p.matcher(str);
		String questionStr = null;
		while(m.find()) {
			questionStr = m.group(1).trim();
			questionStr = questionStr.replaceAll("\t", "");
			questionStr = questionStr.replaceAll("\\\\item", "<li>");
			questionStr = questionStr.replaceAll("\n","</li>\n");
			//trim()で末尾を消してしまっているので末尾に付加
			questionStr =questionStr+"</li>\n";
			//modArea.appendText(questionStr+"\n");
		}
		//ここまででenumerate環境で{\toi}を含んだ文がquestionStr に保存
		// tabular 環境下の「選択肢」を保存
		regex = "選択肢.+?\\\\begin\\{tabular}\\{[clr]+}(.+?)\\\\end\\{tabular}";
		p = Pattern.compile(regex, Pattern.DOTALL);
		m = p.matcher(str);
		String selectStr = null;
		while (m.find()) {
			selectStr = m.group(1);
			//modArea.appendText(selectStr + "\n");
		}
		// ここまでで、selectStr に選択肢番号とその内容が入る。
		// selectStr を区切って内容を List に保存する。
		// \ban{*}を削除
		selectStr = selectStr.replaceAll("\\\\ban\\{.+?}", "");
		// modArea.appendText(selectStr+"\n");
		// 2行以上の時は文末に「\\」+改行コードが入っているのでこれを&に変換
		selectStr = selectStr.replaceAll("\\\\.+?\\n", "&");
		// 前後の空白・改行コードを消す
		selectStr = selectStr.trim();
		// 文末の&を消す。
		selectStr = selectStr.replaceAll("(?!^)&+$", "");
		//modArea.appendText(selectStr + "\n");

		// これで選択肢が&区切りの String になったので、List に内容を格納
		List<String> selectList = new ArrayList<String>();
		String[] selectArray = selectStr.split("&");
		for (String s : selectArray) {
			// 内容が数式の場合はSSを\(\) に変える必要
			regex = "\\$(.+?)\\$";
			p = Pattern.compile(regex);
			m = p.matcher(s);
			while (m.find()) {
				String ss = m.group(1);
				s = "\\(" + ss + "\\)";
			}
			selectList.add(s.trim());
		}
		/*
		for (String s : selectList) {
			modArea.appendText(s + "\n");
		}
		modArea.appendText("選択肢数=" + selectList.size() + "\n");
		*/
		// 続いて、正解についても同じように処理をする。
		regex = "正解.+?\\\\begin\\{tabular}\\{[clr]+}(.+?)\\\\end\\{tabular}";
		p = Pattern.compile(regex, Pattern.DOTALL);
		m = p.matcher(str);
		String ansStr = null;
		while (m.find()) {
			ansStr = m.group(1);
		}
		//{\toi}を削除する
		ansStr = ansStr.replaceAll("\\{\\\\toi}", "");
		// \ban{}を削除して、中身だけを残す
		regex ="\\\\ban\\{(.+?)}";
		p=Pattern.compile(regex);
		m=p.matcher(ansStr);
		List<String> ansList = new ArrayList<String>();
		while(m.find()) {
			String ansNum = m.group(1);
			//modArea.appendText(ansNum+"\n");
			ansList.add(ansNum.trim());
		}
		/*
		for(String s:ansList) {
			modArea.appendText(s+"\n");
		}
		modArea.appendText("正解数"+ansList.size()+"\n");
		*/
		//ここまでで「選択肢内容」「正解番号」がListに保存されている。
		//正解番号と選択肢内容を対応させる
		//正解内容だけをList にする。
		List<String> ansItemList = new ArrayList<String>();
		for(String s:ansList) {
			int ansNum = Integer.parseInt(s);
			int ansPos = ansNum-1; //selectList の場所
			//modArea.appendText(s+":"+ selectList.get(ansPos)+"\n");
			ansItemList.add(selectList.get(ansPos));
		}
		/*
		for(String s: ansItemList) {
			modArea.appendText(s+"\n");
		}
		*/
		//選択肢をランダムに並べ替える。
		/*
		modArea.appendText("----before selectList---\n");
		for(String s: selectList) {
			modArea.appendText(s+"\n");
		}
		*/
		Collections.shuffle(selectList);
		/*
		modArea.appendText("----after selectList---\n");
		for(String s: selectList) {
			modArea.appendText(s+"\n");
		}
		*/
		//並び替えた選択肢に基づいて正解番号を生成ansListを作り替え
		int count = 0;
		for(String s: ansItemList) {
			for(int i=0;i<selectList.size();i++) {
				String s2 = selectList.get(i);
				if(s.contentEquals(s2)) {
					ansList.set(count,""+(i+1));
					//System.out.println(s+":"+s2+"num="+(i+1));
					count++;
				}
			}
		}
		//作り替えられた ansList
		/*
		System.out.println("----ansList----");
		for(String s: ansList) {
			System.out.println(s);
		}
		*/
		//
		//問題の{\toi}に正解番号を埋め込んでいく。
		//questionStr から{\toi}を探す。
		regex = "\\{\\\\toi}";
		p = Pattern.compile(regex);
		m = p.matcher(questionStr);
		count =0;
		while(m.find()) {
			String rep ="{1:SA:="+ ansList.get(count)+"}";
			//modArea.appendText("rep="+rep+"\n");
			questionStr = questionStr.replaceFirst(regex, rep);
			count++;
		}
		//ここで問題リストを書き出す
		modArea.appendText("<ol>\n");
		modArea.appendText(questionStr+"\n");
		modArea.appendText("</ol>\n");
		//選択tableをつくる。
		String tableStr = "<table>\n<caption><b>選択肢</b></caption>\n";
		//選択肢の数
		int num = selectList.size();
		int index = 0;
		tableStr += ("<tr>");
		while (index < num) {
			tableStr += "<td>" + (index + 1) + ". " +selectList.get(index) + "</td>";
			if (((index + 1) % 5) == 0) {
				tableStr += ("</tr>\n");
				tableStr += ("<tr>");
			}
			if (index == num - 1) {
				tableStr += ("</tr>\n");
			}
			index++;
		}
		tableStr += ("</table>\n");
		modArea.appendText(tableStr+"\n");
	}//end of execAction()

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
