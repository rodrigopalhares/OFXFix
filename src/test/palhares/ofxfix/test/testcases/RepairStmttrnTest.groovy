package palhares.ofxfix.test.testcases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;

import palhares.ofxfix.core.OfxFixManager;
import palhares.ofxfix.core.Stmttrn;
import palhares.ofxfix.test.templates.TemplateUtil;

import groovy.util.GroovyTestCase;

/**
 * Testa o mvel que repara o ofx.
 * @author rodrigo.palhares
 *
 */
class RepairStmttrnTest extends GroovyTestCase {

	/** Stmttrn base usado nos testes. */
	Stmttrn stmt;
	
	/**
	 * Teste b�sico de controle. Caso esse caso falhe, nenhum dos outros casos tem validade. 	
	 */
	void testInitialStatus() {
		recreateStmttrn();
		assert !stmt.deleted
		assert stmt.name != 'Juros'
		assert stmt.trnamt != 0
	}

	/**
	 * Testa se as transa��es est�o sendo deletados para os nomes de transa��es inv�lidos.
	 */
	void testSetDeletedByName() {
		recreateStmttrn();
		stmt.name = "SALDO ANTERIOR"
		executeExpression()
		assert stmt.deleted

		recreateStmttrn();
		stmt.name = "RECURSOS EM C/C"
		executeExpression()
		assert stmt.deleted

		recreateStmttrn();
		stmt.name = "Compra normal"
		executeExpression()
		assert !stmt.deleted
	}
	
	/**
	 * Testa se as transa��es est�o sendo deletados para as transa��es com valor 0.
	 */
	void testSetDeletedByAmount() {
		recreateStmttrn()
		stmt.trnamt = 0
		executeExpression()
		assert stmt.deleted, "trnamt = 0"

		recreateStmttrn()
		stmt.trnamt = 1
		executeExpression()
		assert !stmt.deleted, "trnamt = 1"

		recreateStmttrn()
		stmt.trnamt = -1
		executeExpression()
		assert !stmt.deleted, "trnamt = -1"
	}
	
	/**
	 * Testa se o nome das transa��es predefinidas est�o sendo setadas.
	 */
	void testSetName() {
		recreateStmttrn();
		stmt.deleted = false
		stmt.memo = 'REM BASICA'
		executeExpression()
		assert stmt.name == 'Juros'
		assert !stmt.deleted
	}
	
	/**
	 * Testa se os identificadores das transa��es est�o sendo devidamente corrigidos.
	 */
	void testRepairFitId() {
		recreateStmttrn()
		stmt.fitid = null
		executeExpression()
		assert stmt.fitid.startsWith("20120701")

		recreateStmttrn()
		stmt.fitid = ''
		executeExpression()
		assert stmt.fitid.startsWith("20120701")
		
		recreateStmttrn()
		stmt.fitid = '0'
		executeExpression()
		assert stmt.fitid.startsWith("20120701")

		recreateStmttrn()
		stmt.fitid = '000000'
		executeExpression()
		assert stmt.fitid.startsWith("20120701")
	}
	
	/**
	 * Recria o stmttrn base usado nos testes unit�rios.
	 */
	private void recreateStmttrn() {
		stmt = new Stmttrn(TemplateUtil.getText("stmttrn1.xml"))
	}

	/**
	 * Executa a express�o mvel no stmttrn base.
	 */
	private void executeExpression() {
		new OfxFixManager().repair(this.stmt);
	}
}
