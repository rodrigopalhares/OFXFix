package palhares.util.fix.ofx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class OfxParser {
    private Reader reader;
    private final ArrayList<Object> itens = new ArrayList<Object>();
    
    public OfxParser(Reader reader) throws IOException {
        this.reader = reader;
        this.run();
    }

    public void run() throws IOException {
        BufferedReader br = new BufferedReader(this.reader);
        StringBuilder temp = new StringBuilder();

        while (br.ready()) {
            temp.append((char) br.read());
        }

        String stmtTrnOpen = OfxUtil.getOpenTag(OfxTags.TRN_STMTTRN);
        String stmtTrnClose = OfxUtil.getCloseTag(OfxTags.TRN_STMTTRN);

        //monta uma lista com os itens do ofx
        int lastIndex = 0;
        int beginIndex = temp.indexOf(stmtTrnOpen, lastIndex);
        while(beginIndex > 0) {
            //adiciona o conteudo entre os stmttrn
            itens.add(temp.substring(lastIndex, beginIndex));

            //pega o final do item
            int closeIndex = temp.indexOf(stmtTrnClose, beginIndex) + stmtTrnClose.length();

            //monta o item stmttrn
            String stmttrnStr = temp.substring(beginIndex, closeIndex);
            Stmttrn stmttrn = new Stmttrn(stmttrnStr);
            //adiciona o item apenas se tiver sido reparado
            if (stmttrn.repair()) {
                itens.add(stmttrn);
            }

            //move para o próximo elemento stmttrn
            lastIndex = closeIndex;
            beginIndex = temp.indexOf(stmtTrnOpen, lastIndex);
        }

        //adiciona o conteudo do arquivo apos os itens stmttrn
        itens.add(temp.substring(lastIndex, temp.length() - 1));

    }

    public void write(Writer writer) throws IOException {
        for (Object i : this.itens) {
            writer.write(i.toString());
        }
        writer.flush();
        writer.close();
    }

    public ArrayList<Object> getItens() {
        return this.itens;
    }

    public static void main(String[] args) throws Exception{
        FileReader fileReader = new FileReader(new File("C:\\Users\\Palhares\\desktop\\01.OFX"));
        new OfxParser(fileReader);
    }
}
