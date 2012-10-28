package palhares.ofxfix.test.testcases;

import static org.junit.Assert.*;
import palhares.ofxfix.core.OfxFixManager;
import palhares.ofxfix.core.Stmttrn;
import palhares.ofxfix.test.templates.TemplateUtil;

/**
 * Testa as leituras e escritas de arquivos ofxs completos
 * utiliza os templates test1.ofxx, test2.ofxx e test3.ofxx.
 * @author rodrigo.palhares
 */
class OfxParserTest extends GroovyTestCase {

	/**
	 * Testa a leitura do arquivo ofx test1.ofxx.
	 */
	void testReadOfxTest1 () {
		testReadOfxTest("test1.ofxx", {assertOfxTest1(it)})
	}

	/**
	 * Testa a escrita do arquivo ofx test1.ofxx.
	 */
	void testWriteOfxTest1 () {
		this.testWriteOfxTest("test1.ofxx", {assertOfxTest1(it)})
	}

	/**
	 * Testa a leitura do arquivo ofx test2.ofxx.
	 */
	void testReadOfxTest2 () {
		testReadOfxTest("test2.ofxx", {assertOfxTest2(it)})
	}
	
	/**
	 * Testa a escrita do arquivo ofx test2.ofxx.
	 */
	void testWriteOfxTest2 () {
		this.testWriteOfxTest("test2.ofxx", {assertOfxTest2(it)})
	}

	/**
	 * Testa a leitura do arquivo ofx test3.ofxx.
	 */
	void testReadOfxTest3 () {
		testReadOfxTest("test3.ofxx", {assertOfxTest3(it)})
	}
	
	/**
	 * Testa a escrita do arquivo ofx test3.ofxx.
	 */
	void testWriteOfxTest3 () {
		this.testWriteOfxTest("test3.ofxx", {assertOfxTest3(it)})
	}

	/**
	 * Método genérico para teste de leitura.
	 * @param ofxFile arquivo ofx
	 * @param assertMethod closure para fazer as validações
	 */
	private testReadOfxTest(ofxFile, assertMethod) {
		String ofxText = TemplateUtil.getText(ofxFile)
		OfxFixManager ofxFixManager = new OfxFixManager(ofxText)
		assertMethod(ofxFixManager)
	}
	
	/**
	 * Método genérico para escrita de leitura.
	 * @param ofxFile arquivo ofx
	 * @param assertMethod closure para fazer as validações
	 */
	private testWriteOfxTest(ofxFile, assertMethod) {
		String ofxText = TemplateUtil.getText(ofxFile)
		OfxFixManager ofxOriginal = new OfxFixManager(ofxText)

		def stmtCount = ofxOriginal.stmts.size()
		ofxOriginal.stmts.each {
			it.deleted = false
		}

		// Testa se está gerando todos os itens igual o original.
		OfxFixManager ofxGenerated = new OfxFixManager(ofxOriginal.getOfxString())
		assertMethod(ofxGenerated)
		
		ofxOriginal.stmts[0].deleted = true
		ofxOriginal.stmts[1].deleted = true
		
		ofxGenerated = new OfxFixManager(ofxOriginal.getOfxString())
		assert ofxOriginal.stmts.size() == ofxGenerated.stmts.size() + 2
		assert ofxOriginal.itens.size() == ofxGenerated.itens.size() + 2
	}

	private assertOfxTest1(OfxFixManager ofxFixManager) {
		assert ofxFixManager.itens.size() == 7
		assert ofxFixManager.stmts.size() == 5
		assert ofxFixManager.itens[0] instanceof String
		assert ofxFixManager.itens[1] instanceof Stmttrn
		assert ofxFixManager.itens[2] instanceof Stmttrn
		assert ofxFixManager.itens[3] instanceof Stmttrn
		assert ofxFixManager.itens[4] instanceof Stmttrn
		assert ofxFixManager.itens[5] instanceof Stmttrn
		assert ofxFixManager.itens[6] instanceof String

		assert ofxFixManager.itens[1] == ofxFixManager.stmts[0]
		assert ofxFixManager.itens[2] == ofxFixManager.stmts[1]
		assert ofxFixManager.itens[3] == ofxFixManager.stmts[2]
		assert ofxFixManager.itens[4] == ofxFixManager.stmts[3]
		assert ofxFixManager.itens[5] == ofxFixManager.stmts[4]

		assert ofxFixManager.stmts[0].trntype == "DEBIT"
		assert ofxFixManager.stmts[0].dtposted == "20120601"
		assert ofxFixManager.stmts[0].trnamt == -160.00
		assert ofxFixManager.stmts[0].fitid == "9701156"
		assert ofxFixManager.stmts[0].checknum == "9701156"
		assert ofxFixManager.stmts[0].memo == "Saque cc Autoat"
		assert ofxFixManager.stmts[0].name == null

		assert ofxFixManager.stmts[1].trntype == "DEBIT"
		assert ofxFixManager.stmts[1].dtposted == "20120601"
		assert ofxFixManager.stmts[1].trnamt == -5.07
		assert ofxFixManager.stmts[1].fitid == "0289425"
		assert ofxFixManager.stmts[1].checknum == "0289425"
		assert ofxFixManager.stmts[1].memo == "Visa Electron Mama Farah"
		assert ofxFixManager.stmts[1].name == null

		assert ofxFixManager.stmts[2].trntype == "DEBIT"
		assert ofxFixManager.stmts[2].dtposted == "20120601"
		assert ofxFixManager.stmts[2].trnamt == -56.44
		assert ofxFixManager.stmts[2].fitid == "0874132"
		assert ofxFixManager.stmts[2].checknum == "0874132"
		assert ofxFixManager.stmts[2].memo == "Visa Electron Posto"
		assert ofxFixManager.stmts[2].name == null

		assert ofxFixManager.stmts[3].trntype == "DEBIT"
		assert ofxFixManager.stmts[3].dtposted == "20120604"
		assert ofxFixManager.stmts[3].trnamt == -6.45
		assert ofxFixManager.stmts[3].fitid == "0289431"
		assert ofxFixManager.stmts[3].checknum == "0289431"
		assert ofxFixManager.stmts[3].memo == "Visa Electron Mama Farah"
		assert ofxFixManager.stmts[3].name == null

		assert ofxFixManager.stmts[4].trntype == "DEBIT"
		assert ofxFixManager.stmts[4].dtposted == "20120604"
		assert ofxFixManager.stmts[4].trnamt == -45.00
		assert ofxFixManager.stmts[4].fitid == "0895759"
		assert ofxFixManager.stmts[4].checknum == "0895759"
		assert ofxFixManager.stmts[4].memo == "Visa Electron Club Bar"
		assert ofxFixManager.stmts[4].name == null
	}

	private assertOfxTest2(OfxFixManager ofxFixManager) {
		assert ofxFixManager.itens.size() == 5
		assert ofxFixManager.stmts.size() == 3
		assert ofxFixManager.itens[0] instanceof String
		assert ofxFixManager.itens[1] instanceof Stmttrn
		assert ofxFixManager.itens[2] instanceof Stmttrn
		assert ofxFixManager.itens[3] instanceof Stmttrn
		assert ofxFixManager.itens[4] instanceof String

		assert ofxFixManager.itens[1] == ofxFixManager.stmts[0]
		assert ofxFixManager.itens[2] == ofxFixManager.stmts[1]
		assert ofxFixManager.itens[3] == ofxFixManager.stmts[2]

		assert ofxFixManager.stmts[0].trntype == "CREDIT"
		assert ofxFixManager.stmts[0].dtposted == "20120701120000[-3:BRT]"
		assert ofxFixManager.stmts[0].trnamt == 0
		// arquivo reparado
		assert ofxFixManager.stmts[0].fitid != "0"
		assert ofxFixManager.stmts[0].checknum == "0"
		assert ofxFixManager.stmts[0].memo == "REM BASICA"
		// arquivo reparado
		assert ofxFixManager.stmts[0].name == "Juros"
		// arquivo reparado
		assert ofxFixManager.stmts[0].deleted == true
		
		assert ofxFixManager.stmts[1].trntype == "CREDIT"
		assert ofxFixManager.stmts[1].dtposted == "20120701120000[-3:BRT]"
		assert ofxFixManager.stmts[1].trnamt == 83.91
		// arquivo reparado
		assert ofxFixManager.stmts[1].fitid != "0"
		assert ofxFixManager.stmts[1].checknum == "0"
		assert ofxFixManager.stmts[1].memo == "CRED JUROS"
		// arquivo reparado
		assert ofxFixManager.stmts[1].name == "Juros"
		assert ofxFixManager.stmts[1].deleted == false
		
		assert ofxFixManager.stmts[2].trntype == "CREDIT"
		assert ofxFixManager.stmts[2].dtposted == "20120704120000[-3:BRT]"
		assert ofxFixManager.stmts[2].trnamt == 0.09
		// arquivo reparado
		assert ofxFixManager.stmts[2].fitid != "0"
		assert ofxFixManager.stmts[2].checknum == "0"
		assert ofxFixManager.stmts[2].memo == "REM BASICA"
		// arquivo reparado
		assert ofxFixManager.stmts[2].name == "Juros"
		assert ofxFixManager.stmts[2].deleted == false
	}

	private assertOfxTest3(OfxFixManager ofxFixManager) {
		assert ofxFixManager.itens.size() == 10
		assert ofxFixManager.stmts.size() == 8
		assert ofxFixManager.itens[0] instanceof String
		assert ofxFixManager.itens[1] instanceof Stmttrn
		assert ofxFixManager.itens[2] instanceof Stmttrn
		assert ofxFixManager.itens[3] instanceof Stmttrn
		assert ofxFixManager.itens[4] instanceof Stmttrn
		assert ofxFixManager.itens[5] instanceof Stmttrn
		assert ofxFixManager.itens[6] instanceof Stmttrn
		assert ofxFixManager.itens[7] instanceof Stmttrn
		assert ofxFixManager.itens[8] instanceof Stmttrn
		assert ofxFixManager.itens[9] instanceof String

		assert ofxFixManager.itens[1] == ofxFixManager.stmts[0]
		assert ofxFixManager.itens[2] == ofxFixManager.stmts[1]
		assert ofxFixManager.itens[3] == ofxFixManager.stmts[2]
		assert ofxFixManager.itens[4] == ofxFixManager.stmts[3]
		assert ofxFixManager.itens[5] == ofxFixManager.stmts[4]
		assert ofxFixManager.itens[6] == ofxFixManager.stmts[5]
		assert ofxFixManager.itens[7] == ofxFixManager.stmts[6]
		assert ofxFixManager.itens[8] == ofxFixManager.stmts[7]

		assert ofxFixManager.stmts[0].trntype == "CREDIT"
		assert ofxFixManager.stmts[0].dtposted == "20120803120000"
		assert ofxFixManager.stmts[0].trnamt == 834.68
		assert ofxFixManager.stmts[0].fitid == "201208030001"
		assert ofxFixManager.stmts[0].name == "SALDO ANTERIOR"

		assert ofxFixManager.stmts[1].trntype == "DEBIT"
		assert ofxFixManager.stmts[1].dtposted == "20120803120000"
		assert ofxFixManager.stmts[1].trnamt == -745.93
		assert ofxFixManager.stmts[1].fitid == "201208030002"
		assert ofxFixManager.stmts[1].name == "*DOC C ENV/SACADO 948590"

		assert ofxFixManager.stmts[2].trntype == "CREDIT"
		assert ofxFixManager.stmts[2].dtposted == "20120803120000"
		assert ofxFixManager.stmts[2].trnamt == 834.68
		assert ofxFixManager.stmts[2].fitid == "201208030003"
		assert ofxFixManager.stmts[2].name == "RECURSOS EM C/C"

		assert ofxFixManager.stmts[3].trntype == "CREDIT"
		assert ofxFixManager.stmts[3].dtposted == "20120803120000"
		assert ofxFixManager.stmts[3].trnamt == 088.75
		assert ofxFixManager.stmts[3].fitid == "201208030004"
		assert ofxFixManager.stmts[3].name == "SALDO FINAL"

		assert ofxFixManager.stmts[4].trntype == "CREDIT"
		assert ofxFixManager.stmts[4].dtposted == "20120806120000"
		assert ofxFixManager.stmts[4].trnamt == 227.18
		assert ofxFixManager.stmts[4].fitid == "201208060001"
		assert ofxFixManager.stmts[4].name == "TED RECEBIDA 653697"

		assert ofxFixManager.stmts[5].trntype == "CREDIT"
		assert ofxFixManager.stmts[5].dtposted == "20120806120000"
		assert ofxFixManager.stmts[5].trnamt == 315.93
		assert ofxFixManager.stmts[5].fitid == "201208060002"
		assert ofxFixManager.stmts[5].name == "RECURSOS EM C/C"

		assert ofxFixManager.stmts[6].trntype == "CREDIT"
		assert ofxFixManager.stmts[6].dtposted == "20120806120000"
		assert ofxFixManager.stmts[6].trnamt == 315.93
		assert ofxFixManager.stmts[6].fitid == "201208060003"
		assert ofxFixManager.stmts[6].name == "SALDO FINAL"

		assert ofxFixManager.stmts[7].trntype == "DEBIT"
		assert ofxFixManager.stmts[7].dtposted == "20120809120000"
		assert ofxFixManager.stmts[7].trnamt == -12.00
		assert ofxFixManager.stmts[7].fitid == "201208090001"
		assert ofxFixManager.stmts[7].name == "PACOTE TOP"
	}
}
