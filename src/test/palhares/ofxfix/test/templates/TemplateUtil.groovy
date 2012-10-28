package palhares.ofxfix.test.templates

import palhares.ofxfix.core.Stmttrn;

/**
 * Classe utilit�ria para os arquivos templates.
 * @author rodrigo.palhares
 *
 */
class TemplateUtil {

	/**
	 * Obtem o conte�do de um arquivo template.
	 * @param fileName nome do template
	 * @return String com o conte�do
	 */
	static String getText(String fileName) {
		String content = TemplateUtil.class.getResourceAsStream(fileName).getText();
		return content
	}
}
