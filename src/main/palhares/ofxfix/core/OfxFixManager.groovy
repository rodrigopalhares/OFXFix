package palhares.ofxfix.core

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.mvel2.MVEL;
import org.mvel2.integration.impl.CachedMapVariableResolverFactory;

import static OfxTags.*;

/**
 * Gerência as operações de correções do ofx.  
 * @author rodrigo.palhares
 *
 */
public class OfxFixManager {

	/** Todos os itens contidos no ofx. É uma lista de String e Stmttrn. */
	List<Object> itens = []
	
	/** Todos os itens Stmttrn do ofx. */
	List<Stmttrn> stmts = []
	
	/** Arquivo ofx original. */
	File ofxFile;
	
	/** Expressão mvel compilada. */
	def compileExpression = null;
	
	static String[] transactionNames = null;

	/**
	 * Construtor.
	 * @param ofxFile arquivo ofx
	 */
	public OfxFixManager() {
	}
	
	/**
	 * Construtor.
	 * @param ofxFile arquivo ofx
	 */
	public OfxFixManager(File ofxFile) {
		this(ofxFile.getText())
		this.ofxFile = ofxFile
	}
	
	/**
	 * Construtor.
	 * @param ofxText conteudo ofx
	 */
	public OfxFixManager(String ofxText) {
		this.createOfxItens(ofxText)
	}
	
	/**
	 * Cria os itens do ofx informado no parâmetro.
	 * @param ofxText texto do ofx
	 */
	void createOfxItens(String ofxText) {
		String stmtTrnOpen = "<${TRN_STMTTRN}>";
		String stmtTrnClose = "</${TRN_STMTTRN}>";

		//monta uma lista com os itens do ofx
		int lastEndIndex = -1;
		int beginIndex = ofxText.indexOf(stmtTrnOpen, lastEndIndex);
		while(beginIndex > 0) {
			//adiciona o conteudo entre os stmttrn
			if (lastEndIndex + 1 < beginIndex - 1) {
				String contentBetweenStmttrn = ofxText[(lastEndIndex+1)..(beginIndex-1)].trim();
				if (!contentBetweenStmttrn.empty) { 
					this.itens.add(contentBetweenStmttrn);
				}
			}

			//pega o final do item
			int closeIndex = ofxText.indexOf(stmtTrnClose, beginIndex) + stmtTrnClose.length();

			//monta o item stmttrn
			String stmttrnStr = ofxText[beginIndex..(closeIndex-1)];
			Stmttrn stmttrn = new Stmttrn(stmttrnStr);
			repair(stmttrn)
			this.stmts.add(stmttrn);
			this.itens.add(stmttrn);

			//move para o próximo elemento stmttrn
			lastEndIndex = closeIndex;
			beginIndex = ofxText.indexOf(stmtTrnOpen, lastEndIndex);
		}

		//adiciona o conteudo do arquivo apos os itens stmttrn
		this.itens.add(ofxText[lastEndIndex..-1]);
	}

	/**
	 * Monta um o conteudo do arquivo ofx com base nos itens.
	 * @return string
	 */
	String getOfxString() {
		StringBuilder sb = new StringBuilder();

		this.itens.each {
			if (!(it instanceof Stmttrn) || !it.deleted) {
				sb << "${it}\r\n"
			}
		}

		return sb.toString();
	}
	
	/**
	 * Escreve os itens para um arquivo com base nos itens.
	 * Caso o arquivo informado seja <code>null</code> é gravado no mesmo local do arquivo de origem concatenado com "_parsed".
	 * @param destFile arquivo de destino. Parâmetro obrigatório quando não for utilizado o {@link OfxManager#OfxManager(File)}
	 */
	void writeToFile(File destFile = null) {
		if (destFile == null) {
			String fileName = this.ofxFile.getAbsolutePath();
			int dotIdx = fileName.lastIndexOf(".");
			destFile = new File(fileName.substring(0, dotIdx) + "_parsed.ofx");
		}

		destFile.write(this.getOfxString());
	}

	static String[] getTransactionNames() {
		if (transactionNames == null) {
			List<String> names = []
			new File("transaction-names.cfg").eachLine {
				if (!it.isEmpty()) {
					names.add(it.trim());
				}
			}
			transactionNames = names.toArray(new String[0])
		}
		return transactionNames;
	}
	
	/**
	 * Executa a expressão mvel no stmttrn para reparar o stmt.
	 * @param stmt stmttrn a ser reparado
	 */
	void repair(Stmttrn stmt) {
		if (compileExpression == null) {
			def repairFile = new File("repair-stmttrn.mvel");
			compileExpression = MVEL.compileExpression(repairFile.getText());
		}

		// Por algum motivo sem esse sleep ocorre o erro na abertura de alguns arquivos. "org.mvel2.optimizers.OptimizationNotSupported"
		Thread.sleep(1);

		def vars = new CachedMapVariableResolverFactory(["stmt" : stmt]);
		MVEL.executeExpression(compileExpression, vars)
	}
}
