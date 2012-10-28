package palhares.ofxfix.test.templates

import palhares.ofxfix.core.Stmttrn;

/**
 * Classe utilitária para os arquivos templates.
 * @author rodrigo.palhares
 *
 */
class TemplateUtil {

	/**
	 * Obtem o conteúdo de um arquivo template.
	 * @param fileName nome do template
	 * @return String com o conteúdo
	 */
	static String getText(String fileName) {
		String content = TemplateUtil.class.getResourceAsStream(fileName).getText();
		return content
	}
}
