package org.nianet.plexil5.maude2java.modelchecking.ltlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.tree.Tree;
import org.antlr.stringtemplate.StringTemplate;

public class Test {

	public static void main(String[] args) {
		//try {
			
			//$globally$($not$(aaa.INACTIVE)) $and$ ($not$(bbb.FINISHED)) $and$ 4=4
			//Tree t=LTLTreeGenerator.parseAndGetTree("$globally$(($not$(aaa.inactive)) $and$ ($not$(bbb.finished) $and$ (5=yy.{varw}+3)) ) ");
			//Tree t=LTLTreeGenerator.parseAndGetTree("$globally$($not$(aaa.INACTIVE)) $and$ ($not$(bbb.FINISHED)) $and$ (4=4)");
			//Tree t=LTLTreeGenerator.parseAndGetTree("$lglobally$($lglobally$(DriveToTarget.waiting $land$ eval(3$equ$3)))");
			
			//Tree t=LTLTreeGenerator.parseAndGetTree("AAA.<inv> $lequivalent$ $ltrue$");
			//System.out.println(t.getChildCount());
			
			/*System.out.println(t.getChild(0).getText());
			System.out.println(t.getChild(1).getText());
			System.out.println(t.getChild(2).getText());
			/*System.out.println(t.getText());*/
			
			/*System.out.println(t.getChildCount());
			System.out.println(LTLTreeGenerator.generateInfixExpression(t));
			
			
			System.out.println("5+3=33>=33".replaceAll("([^><])=", "$1eq"));
			
			*/
		
			//Matcher m=Pattern.compile("([(a-z)(A-Z)(0-9)-_]+)\\.([\\w-]+)").matcher("xxx.22a2_AA-aa");
			
		
			//Matcher m=Pattern.compile("((.)+(/\\\\)(.)+)*").matcher("aaa /\\ vdasd /\\ asdasd");
			//String o=m.replaceAll("((.)+(@)(.)+)*");
			String s="aaa /\\ vdasd /\\ asda [] sd -> asd";
			//Matcher m=Pattern.compile("([(a-z)(A-Z)(0-9)_-]+)\\.<(end|inv|pre|start|post|repeat)>").matcher("qqq.<allChildrenInactive>");
			System.out.println(s.replaceAll("->", "@"));
			
			//StringTemplate st=new StringTemplate(LTLTreeGenerator.generateInfixExpression(t));
			//st.setAttribute("and", "/\\");
			//st.setAttribute("globally", "[]");
			
			//System.out.println(st.toString());
	/*		
		} catch (MalformedLTLExpressionException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getCause().toString());
		}*/
	}
	
}
