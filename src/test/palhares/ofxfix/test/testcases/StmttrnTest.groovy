package palhares.ofxfix.test.testcases;

import static org.junit.Assert.*

import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.integration.impl.*;

import palhares.ofxfix.core.Stmttrn;
import palhares.ofxfix.test.templates.TemplateUtil;

/**
 * Verifica o parse e a geração do código de fragmentos Stmttrn de um ofx.
 * @author rodrigo.palhares
 *
 */
class StmttrnTest extends GroovyTestCase {

	/**
	 * Testa o parse e geração do fragmento stmttrn do template stmttrn1.xml.
	 */
	void testStmttrn1() {
		testStmttrn("stmttrn1.xml", {assertStmttrn1(it)})
	}
	
	/**
	 * Testa o parse e geração do fragmento stmttrn do template stmttrn2.xml.
	 */
	void testStmttrn2() {
		testStmttrn("stmttrn2.xml", {assertStmttrn2(it)})
	}

	/**
	 * Testa o parse e geração do fragmento stmttrn do template stmttrn3.xml.
	 */
	void testStmttrn3() {
		testStmttrn("stmttrn3.xml", {assertStmttrn3(it)})
	}
	
	/**
	 * Testa o método equals do Stmttrn.
	 */
	void testStmttrnEquals() {
		Stmttrn s1 = new Stmttrn(TemplateUtil.getText("stmttrn1.xml"))
		Stmttrn s2 = new Stmttrn(TemplateUtil.getText("stmttrn1.xml"))

		assert s1 == s2
		assert !s1.is(s2)
		
		s2.trntype = "wrong_type"
		assert s1 != s2
	}

	/**
	 * Testa o parse e geração do fragmento stmttrn informado no parâmetro.
	 * @param ofxFile nome do fragmento a ser testado
	 * @param assertStmttrn closure que valida o fragmento
	 */
	private testStmttrn(ofxFile, assertStmttrn) {
		Stmttrn stmttrn = new Stmttrn(TemplateUtil.getText(ofxFile))
		assertStmttrn(stmttrn)
		assertStmttrn(new Stmttrn(stmttrn.toString()))
	}

	private assertStmttrn1(Stmttrn stmttrn) {
		assert stmttrn.trntype == "CREDIT"
		assert stmttrn.dtposted == "20120701120000[-3:BRT]"
		assert stmttrn.trnamt == 83.91
		assert stmttrn.fitid == "0"
		assert stmttrn.checknum == "0"
		assert stmttrn.memo == "CRED JUROS"
		assert stmttrn.name == null
	}
	
	private assertStmttrn2(Stmttrn stmttrn) {
		assert stmttrn.trntype == "DEBIT"
		assert stmttrn.dtposted == "20120604"
		assert stmttrn.trnamt == -6.45
		assert stmttrn.fitid == "0289431"
		assert stmttrn.checknum == "0289431"
		assert stmttrn.memo == "Visa Electron Mama Farah"
		assert stmttrn.name == null
	}
	
	private assertStmttrn3(Stmttrn stmttrn) {
		assert stmttrn.trntype == "CREDIT"
		assert stmttrn.dtposted == "20120806120000"
		assert stmttrn.trnamt == 6227.18
		assert stmttrn.fitid == "201208060001"
		assert stmttrn.memo == null
		assert stmttrn.name == "TED RECEBIDA 653697"
	}
}
