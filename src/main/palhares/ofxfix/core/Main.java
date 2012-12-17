package palhares.ofxfix.core;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Aplicação swing que corrige e facilita a categorização de transações de um OFX.
 * @author rodrigo.palhares
 */
public class Main extends JPanel {
    private static final long serialVersionUID = 6806238886135776531L;

    /** Painel principal. */
    private final JPanel panelMain = new JPanel();

    /** Caixa de dialogo de escolha de arquivo. */
    private JFileChooser fileChooser;

    /** Botão de abertura de arquivo. */
    private JButton openButton;

    /** Botão que salva o novo arquivo. */
    private JButton saveButton;

    private OfxFixManager ofxFixManager;
    private Map<Stmttrn, JComboBox<String>> itensMap = new LinkedHashMap<>();

    public Main() {
        super(new BorderLayout());

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setFileFilter(new FileNameExtensionFilter("Money OFX", "ofx"));

        this.panelMain.setLayout(new BoxLayout(this.panelMain, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelMain);
        this.add(scrollPane);

        this.createCommandPanel();

        try {
            OfxFixManager.getTransactionNames();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir name.cfg", "Open File Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Cria o painel com os comandos de abrir arquivo e salvar.
     */
	private void createCommandPanel() {
		this.openButton = new JButton("Open...");
        this.openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chooseFiles();
            }
        });

        this.saveButton = new JButton("Save");
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                saveFile();
            }
        });

        // Cria o panel com os botões de comando
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(this.openButton);
        panel.add(this.saveButton);
        panel.setPreferredSize(new Dimension(0, 25));
        this.add(panel, BorderLayout.PAGE_END);
	}

    /**
     * Salva o novo arquivo reparado.
     */
    private void saveFile() {
        for (Map.Entry<Stmttrn, JComboBox<String>> entry : this.itensMap.entrySet()) {
            Stmttrn stmt = entry.getKey();
            stmt.setName(entry.getValue().getSelectedItem().toString());
        }

        try {
            this.ofxFixManager.writeToFile();
            JOptionPane.showMessageDialog(this, "Sucesso", "Arquivo grava com sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo", "Save File Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a caixa de dialogo de escolha de arquivo.
     */
    private void chooseFiles() {
        int returnVal = this.fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Arrays.copyOfRange(OfxFixManager.getTransactionNames(), 0, 1);
            this.openFile(this.fileChooser.getSelectedFile());
        }
        this.validate();
    }

    /**
     * Abre o arquivo ofx.
     * @param ofxSourceFile Arquivo ofx
     */
	private void openFile(File ofxSourceFile) {
		try {
			this.ofxFixManager = new OfxFixManager(ofxSourceFile);
			this.panelMain.removeAll();
			this.itensMap.clear();
			
			// Cria a listagem com as transações do ofx.
			for (Stmttrn stmt : this.ofxFixManager.getStmts()) {
				// Cria as celulas da linha
				String[] names = Arrays.copyOf(OfxFixManager.getTransactionNames(), OfxFixManager.getTransactionNames().length);
				names[0] = stmt.getName();
				JComboBox<String> jComboBox = new JComboBox<String>(names);
				jComboBox.setEditable(true);
				jComboBox.setSelectedItem(stmt.getName());
				jComboBox.setEnabled(!stmt.getDeleted());

				JTextField dtposted = new JTextField(stmt.getDtposted());
				dtposted.setEnabled(false);

				JTextField trnamt = new JTextField(stmt.getTrnamt().toString());
				trnamt.setEnabled(false);

				JTextField trnmemo = new JTextField(stmt.getMemo());
				trnmemo.setEnabled(!stmt.getDeleted());

				// Adiciona os itens na linha
				JPanel p = new JPanel(new GridLayout(1, 3));
				p.add(dtposted);
				p.add(trnamt);
				p.add(jComboBox);
				p.add(trnmemo);
				p.setPreferredSize(new Dimension(750, 25));
				p.setMaximumSize(new Dimension(750, 25));
				this.panelMain.add(p);
				this.itensMap.put(stmt, jComboBox);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo", "Open File Erro", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
			e.printStackTrace();
		}

	}

    /**
     * Cria e mostra a GUI. Se for passado 1 arquivo, ele é aberto.
     */
    private static void createAndShowGUI(String[] files) {
        JFrame frame = new JFrame("Regex Rename File");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(790, 700));

        Main newContentPane = new Main();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        if (files.length == 1) {
        	newContentPane.openFile(new File(files[0]));
        }
        frame.setVisible(true);
    }

    /**
     * Inicializa a aplicação swing.
     * Pode receber um arquivo como argumento.
     * @param args argumento
     */
    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }
}
