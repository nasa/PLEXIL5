package org.nianet.plexil.plexilxml2maude.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.ext.PlexilScriptException;

public class Plexil2MaudeExpressionsTestCase extends TestCase {

	private static String testNodeBegin = null;
	private static String testNodeEnd = null;

	static {

		testNodeBegin = "TestNode:\n";
		testNodeBegin += "{\n";

		testNodeEnd = "}\n";

		// SimpleAssignment:
		// {
		// Integer foo = 0;
		// PostCondition: foo == 3;
		// Assignment: foo = 3;
		// }

	}

	private String createTestFile(String c) throws IOException, PlexilScriptException  {
		String testFilePath = Plexil2MaudeExpressionsTestCase.class
				.getResource(".").getPath();
		BufferedWriter bw = new BufferedWriter(new FileWriter(testFilePath
				+ "/TestNode.ple"));
		bw.write(c);
		bw.close();
		return testFilePath + "/TestNode.ple";
	}

	public void testIntegerLiteral() throws IOException, PlexilScriptException , PlexilScriptException {

		String expression = "Integer foo=0;\nAssignment: foo = 3;";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	
	public void testVarDeclarationWithoutIV() throws IOException, PlexilScriptException  {

		String expression = "Integer foo;\nAssignment: foo = 3;";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}
	
	
	public void testRealLiteral() throws IOException, PlexilScriptException  {

		String expression = "Integer foo=0;\nAssignment: foo = 3.1;";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	public void testBooleanLiteral() throws IOException, PlexilScriptException  {

		String expression = "Boolean foo=false;\nAssignment: foo = true;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {			
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	public void testBooleanExpressionVarsComparison() throws IOException, PlexilScriptException  {

		String expression = "Integer a=2;\nInteger b=3;\nBoolean foo=false;\nAssignment: foo = a < b;";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	public void testArithmeticExpressionsBasicOp() throws IOException, PlexilScriptException  {

		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nAssignment: foo = (a * b) - a;";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	public void testArithmeticExpressionsAdvancedExp() throws IOException, PlexilScriptException  {

		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nAssignment: foo = sqrt (a * b);";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			fail(e.getMessage());
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	public void testBooleanConditionWithArithmeticExpressionsBasicExp()
			throws IOException, PlexilScriptException  {

		//note: this test fails with (a-b) due a bug with Plexil2XML translator
		
		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nPostCondition: b == (foo * (a + b));\nAssignment: foo = 23;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	
	public void testBooleanConditionWithArithmeticExpressionsCompositeExp()
	throws IOException, PlexilScriptException  {
		

		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nPostCondition: (b - 3) * 5 == (foo * (a + b));\nAssignment: foo = 23;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}


	public void testBooleanConditionWithBooleanExpression()
	throws IOException, PlexilScriptException  {
		

		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nPostCondition: (foo < a) || !((b - 3) * 5 == (foo * (a + b)));\nAssignment: foo = 23;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			PlexilPlan.resetPlexilPlansInfo();
		} catch (PlexilPlanLoadException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	
	public void testBooleanConditionWithLiterals()
	throws IOException, PlexilScriptException  {
		
		
		
		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nStartCondition: true;\nEndCondition:true;\nRepeatCondition: true;\nSkipCondition: false;\nPreCondition: true;\nPostCondition: false;\nInvariantCondition: true;\nAssignment: foo = 23;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			PlexilPlan.resetPlexilPlansInfo();
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			
		} catch (PlexilPlanLoadException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	
	public void testBooleanConditionWithBooleanEpressions()
	throws IOException, PlexilScriptException  {
	
		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nStartCondition: a > b;\nEndCondition:a > b;\nRepeatCondition: a > b;\nSkipCondition: a > b;\nPreCondition: a > b;\nPostCondition: a > b;\nInvariantCondition: a > b;\nAssignment: foo = 23;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			PlexilPlan.resetPlexilPlansInfo();
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			
		} catch (PlexilPlanLoadException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}
	
	public void testBooleanConditionWithNodeStatesChecking()
	throws IOException, PlexilScriptException  {
		

		String expression = "Integer foo=2;\nInteger a=2;\nInteger b=3;\nStartCondition: TestNode.outcome == SUCCESS;\nAssignment: foo = 23;\n";

		System.out.println(testNodeBegin + expression + testNodeEnd);

		PlexilPlan pp = null;
		String maudem = null;
		try {
			PlexilPlan.resetPlexilPlansInfo();
			pp = PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(
					createTestFile(testNodeBegin + expression + testNodeEnd),
					null);
			
		} catch (PlexilPlanLoadException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}
		try {
			maudem = pp.generateMaudeModule(null);
			System.out.println(maudem);
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());			
		}

		assertTrue("null expression generated", !maudem.contains("null"));
		assertTrue("wrong expression generated", maudem.contains("foo"));

	}

	
	
	
/*	StartCondition: Node1.outcome == SUCCESS;
	EndCondition: SignalEndOfPlan.state == FINISHED ||
	              SendAbortUpdate.state == FINISHED ||
	              abort_due_to_exception;
	PreCondition: Request_Human_Consent.state == FINISHED &&
	              LookupOnChange(ZZZZCWEC5520J) == 1;
	PostCondition: AtGoal;
	InvariantCondition: LookupOnChange(ZZZZCWEC5520J) == 1;
	RepeatCondition: Count < 10;
	Node outcome, node state
	*/

	
	
	
}
