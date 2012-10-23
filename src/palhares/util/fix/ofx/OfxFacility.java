package palhares.util.fix.ofx;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

import palhares.util.common.StringTools;

/**
 * Aplicação swing que permite renomear arquivos usando o regex do java.
 * @author Palhares
 */
public class OfxFacility extends JPanel {
    private static final long serialVersionUID = 6806238886135776531L;

    private JFileChooser fileChooser;

    private final JPanel panelMain = new JPanel();
    private JButton openButton;
    private JButton saveButton;

    private OfxParser ofxParser;
    private Map<Stmttrn, JComboBox<String>> itensMap = new LinkedHashMap<>();
    private File ofxSourceFile;
    String[] transactionNames;

    public OfxFacility() {
        super(new BorderLayout());

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setFileFilter(new FileNameExtensionFilter("Money OFX", "ofx"));

        this.panelMain.setLayout(new BoxLayout(this.panelMain, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelMain);
        this.add(scrollPane);

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

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(this.openButton);
        panel.add(this.saveButton);
        panel.setPreferredSize(new Dimension(0, 25));
        this.add(panel, BorderLayout.PAGE_END);

        try {
            this.loadTransactionNames();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir name.cfg", "Open File Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void loadTransactionNames() throws IOException {
        // InputStream inputstream = this.getClass().getClassLoader().getResourceAsStream("name.cfg");
    	InputStream inputstream = new FileInputStream("name.cfg");
    	
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputstream));
        Set<String> names = new LinkedHashSet<String>();
        names.add("");
        while (bf.ready()) {
            String line = bf.readLine();
            if (!StringTools.isEmpty(line)) {
                names.add(line.trim());
            }
        }
        this.transactionNames = names.toArray(new String[names.size()]);
    }

    private void saveFile() {
        for (Map.Entry<Stmttrn, JComboBox<String>> entry : this.itensMap.entrySet()) {
            Stmttrn stmt = entry.getKey();
            stmt.set(OfxTags.TRN_NAME, entry.getValue().getSelectedItem().toString());
        }
        String fileName = this.ofxSourceFile.getAbsolutePath();
        int dotIdx = fileName.lastIndexOf(".");
        fileName = fileName.substring(0, dotIdx) + "_parsed.ofx";
        try {
            this.ofxParser.write(new FileWriter(fileName));
            JOptionPane.showMessageDialog(this, "Sucesso", "Arquivo grava com sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo", "Save File Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a caixa de dialog de escolha de arquivo.
     */
    private void chooseFiles() {
        int returnVal = this.fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Arrays.copyOfRange(this.transactionNames, 0, 1);
            this.ofxSourceFile = this.fileChooser.getSelectedFile();
            openFile();
        }
        this.validate();
    }

	private void openFile() {
		try {
			this.ofxParser = new OfxParser(new FileReader(this.ofxSourceFile));
			this.panelMain.removeAll();
			this.itensMap.clear();
			for (Object o : this.ofxParser.getItens()) {
				if (o instanceof Stmttrn) {
					Stmttrn stmt = (Stmttrn) o;
					JPanel p = new JPanel(new GridLayout(1, 3));

					String[] names = Arrays.copyOf(this.transactionNames, this.transactionNames.length);
					names[0] = stmt.get(OfxTags.TRN_NAME);
					JComboBox<String> jComboBox = new JComboBox<String>(names);
					jComboBox.setEditable(true);
					jComboBox.setSelectedItem(stmt.get(OfxTags.TRN_NAME));

					JTextField dtposted = new JTextField(stmt.get(OfxTags.TRN_DTPOSTED));
					dtposted.setEnabled(false);
					p.add(dtposted);
					JTextField trnamt = new JTextField(stmt.get(OfxTags.TRN_TRNAMT));
					trnamt.setEnabled(false);
					p.add(trnamt);
					p.add(jComboBox);
					JTextField trnmemo = new JTextField(stmt.get(OfxTags.TRN_MEMO));
					trnmemo.setEnabled(false);
					p.add(trnmemo);
					p.setPreferredSize(new Dimension(750, 25));
					p.setMaximumSize(new Dimension(750, 25));
					this.panelMain.add(p);
					this.itensMap.put(stmt, jComboBox);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo", "Open File Erro", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
			e.printStackTrace();
		}

	}

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(String[] files) {
        //Create and set up the window.
        JFrame frame = new JFrame("Regex Rename File");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(790, 700));

        //Create and set up the content pane.
        OfxFacility newContentPane = new OfxFacility();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        if (files.length == 1) {
        	newContentPane.ofxSourceFile = new File(files[0]);
        	newContentPane.openFile();
        }
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }
}
