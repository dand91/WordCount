package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Counter.SentenceCounter;
import common.PasteInputException;
import common.SentenceEntry;
import common.fileNotFoundException;

/**
 * @author DAnd91
 *
 */
@SuppressWarnings("serial")
public class GUI extends JPanel implements ClipboardOwner {

	private static JFrame frame;

	protected JFileChooser fileChooser;
	
	protected SentenceCounter counter;
	
	protected JButton wordButton;
	protected JButton charButton;
	protected JButton pasteButton;
	protected JButton openButton;
	protected JButton URLButton;

	private JTextField minInput;
	private JTextField maxInput;
	private JTextField searchInput;
	private JTextField URLInput;
	
	private DefaultListModel<String> textListModel;
	private JList<String> textList;
	private JScrollPane textPane;
	
	private DefaultListModel<String> commonListModel;
	private JList<String> commonList;
	private JScrollPane commonPane;
	
	protected JTextArea sentenceTextArea;
	private JScrollPane sentencePane;
	
	private List<SentenceEntry> sentenceList;
	private List<SentenceEntry> commonWordList;
	
	
	/**
	 * Creates a panel for all content.
	 *
	 */

	public GUI() {
		
		super(new BorderLayout());
		
		Color color = new Color(238,238,238);

		this.setBackground(color);

		// Displays word/char-count and sentences

		textListModel = new DefaultListModel<String>();

		textList = new JList<String>(textListModel);
		textList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textList.setPrototypeCellValue("123456789012");
		textList.addListSelectionListener(new chooseTextAction());
		textList.addKeyListener(new chooseTextAction());

		textPane = new JScrollPane(textList);
		textPane.setMaximumSize(new Dimension(700, 550));
		textPane.setPreferredSize(new Dimension(700, 550));

		// Displays messages and sentences

		sentenceTextArea = new JTextArea(5, 20);
		sentenceTextArea.setMargin(new Insets(5, 5, 5, 5));
		sentenceTextArea.setEditable(false);
		sentenceTextArea.setText(stringInfo("", ""));

		sentencePane = new JScrollPane(sentenceTextArea);
		sentencePane.setMaximumSize(new Dimension(300, 550));
		sentencePane.setPreferredSize(new Dimension(300, 550));

		// Displays common words

		commonListModel = new DefaultListModel<String>();

		commonList = new JList<String>(commonListModel);
		commonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commonList.setPrototypeCellValue("123456789012");
		commonList.addListSelectionListener(new chooseCommonAction());

		commonPane = new JScrollPane(commonList);
		commonPane.setMaximumSize(new Dimension(150, 550));
		commonPane.setPreferredSize(new Dimension(150, 550));

		// Add FileChooser

		fileChooser = new JFileChooser();

		// Add buttons

		openButton = new JButton("Open file");
		openButton.addActionListener(new fileAction(this));
//		openButton.putClientProperty("JButton.buttonType", "gradient");

		wordButton = new JButton("WordCount");
		wordButton.setEnabled(false);
		wordButton.addActionListener(new wordAction());
//		wordButton.putClientProperty("JButton.buttonType", "gradient");

		
		charButton = new JButton("CharCount");
		charButton.setEnabled(false);
		charButton.addActionListener(new charAction());
//		charButton.putClientProperty("JButton.buttonType", "gradient");


		pasteButton = new JButton("Paste");
		pasteButton.addActionListener(new pasteAction());
//		pasteButton.putClientProperty("JButton.buttonType", "gradient");
		
		
		URLButton = new JButton("URL search");
		URLButton.addActionListener(new URLAction());
//		URLButton.addActionListener(new );
//		URLButton.putClientProperty("JButton.buttonType", "gradient");


		// Add input fields

		JLabel minLabel = new JLabel("Input min: ");

		minInput = new JTextField();
		minInput.setMaximumSize(new Dimension(80, 30));
		minInput.setPreferredSize(new Dimension(70, 30));
		minInput.setText("0");

		JLabel maxLabel = new JLabel("Input max: ");

		maxInput = new JTextField();
		maxInput.setMaximumSize(new Dimension(80, 30));
		maxInput.setPreferredSize(new Dimension(70, 30));
		maxInput.setText("1000");

		JLabel searchLabel = new JLabel("Search words: ");

		searchInput = new JTextField();
		searchInput.setMaximumSize(new Dimension(430, 30));
		searchInput.setPreferredSize(new Dimension(430, 30));
		
		
		JLabel URLLabel = new JLabel("input URL: ");

		URLInput = new JTextField();
		URLInput.setMaximumSize(new Dimension(450, 30));
		URLInput.setPreferredSize(new Dimension(450, 30));

		// Create and add panels

		JPanel firstPanel = new JPanel();
		JPanel secondPanel = new JPanel();
		
		JPanel inputPanel = new JPanel();
		JPanel searchPanel = new JPanel();
		JPanel URLPanel = new JPanel();
		JPanel sizePanel = new JPanel();
		JPanel sizeInputPanel = new JPanel();

		inputPanel.add(openButton);
		inputPanel.add(pasteButton);
		inputPanel.setBackground(color);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		
		sizePanel.add(wordButton);
		sizePanel.add(charButton);
		sizePanel.setBackground(color);
		sizePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		
		sizeInputPanel.add(minLabel);
		sizeInputPanel.add(minInput);
		sizeInputPanel.add(maxLabel);
		sizeInputPanel.add(maxInput);
		sizeInputPanel.setBackground(color);
		sizeInputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		
		searchPanel.add(searchLabel);
		searchPanel.add(searchInput);
		searchPanel.setBackground(color);
		
		URLPanel.add(URLLabel);
		URLPanel.add(URLInput);
		URLPanel.setBackground(color);
		
		firstPanel.add(inputPanel);
		firstPanel.add(URLPanel);
		firstPanel.add(URLButton);
		firstPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 310));

		secondPanel.add(sizePanel);
		secondPanel.add(sizeInputPanel);
		secondPanel.add(searchPanel);
		secondPanel.setBackground(color);
		secondPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
		
		setLayout(new FlowLayout());
		add(firstPanel, BorderLayout.PAGE_START);
		add(secondPanel, BorderLayout.WEST);
		add(textPane, BorderLayout.CENTER);
		add(commonPane, BorderLayout.CENTER);
		add(sentencePane, BorderLayout.EAST);

	}

	private void setCommonWords() {

		commonWordList = counter.getCommonList();
		
		for (SentenceEntry e : commonWordList) {

			commonListModel.addElement("[ " + e.size + " ] " + e.text);
		}
	}

	private void setWordSentences() {

		String min = minInput.getText();
		String max = maxInput.getText();
		String search = searchInput.getText();

		if (min.length() > 0) {

			sentenceList = counter.nbrWords(Integer.valueOf(min),
					Integer.valueOf(max), search);

			for (SentenceEntry entry : sentenceList) {

				String string = entry.text;

				textListModel.addElement("[ " + entry.size + " ] " + string);

			}

		} else {

			sentenceTextArea.setText(stringInfo("Input amount", ""));
		}
	}

	private void setCharSentences() {

		String min = minInput.getText();
		String max = maxInput.getText();
		String search = searchInput.getText();

		if (min.length() > 0) {

			sentenceList = counter.nbrChars(Integer.valueOf(min),
					Integer.valueOf(max), search);

			for (SentenceEntry entry : sentenceList) {

				textListModel.addElement("[ " + entry.size + " ] "
						+ entry.text);
			}

		} else {

			sentenceTextArea.setText(stringInfo("Input amount", ""));
		}
	}

	private void fillSearch() {

		String string = "";

		try {

			string = commonWordList.get(commonList.getSelectedIndex()).text;
			searchInput.setText(searchInput.getText() + " " + string);

		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();

		}

	}

	/**
	 * Displays sentence selected from sentenceList
	 *
	 */
	
	private  void setSentence() {

		String string = "";

		try {

			string = sentenceList.get(textList.getSelectedIndex()).text;

		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();
			
		}

		setClipboardContents(string);

		sentenceTextArea.setText(stringInfo("", ""));

		StringTokenizer st = new StringTokenizer(string);

		StringBuilder sb = new StringBuilder();

		Boolean runOuter = true;
		Boolean runInner = true;

		String rest = "";
		String temp = "";
		string = "";

		while (runOuter) {

			runInner = true;

			if (rest.length() > 0) {

				sb.append(rest + " ");
				rest = "";

			}

			while (runInner) {

				try {

					temp = st.nextToken();

				} catch (NoSuchElementException e) {

					runOuter = false;
					runInner = false;

				}

				if (sb.length() + temp.length() > 41) {

					rest = temp;
					runInner = false;

				} else if(runInner){

					sb.append(temp + " ");
				}
			}

				string = string + sb.toString() + "\n";
				sb = new StringBuilder();
		}

		sentenceTextArea.setText(stringInfo("", string));

	}

	/**
	 * CTRL-C marks the inputed string.
	 * 
	 */

	private  void setClipboardContents(String aString) {
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}

	/**
	 * Clear text fields.
	 *
	 */

	private void clear() {

		textListModel.clear();
		commonListModel.clear();

	}
	
	/**
	 * returns a string with info/text plus some graphics,
	 *
	 */

	private String stringInfo(String string1, String string2) {

		return "<---------------Info---------------> \n\n" + string1
				+ "\n\n<---------------Text--------------->\n\n" + string2
				+ "\n<--------------------------------->\n\n";
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {

	}

	/**
	 * Listen for pressing of "WordCount" button.
	 *
	 */

	class wordAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			clear();
			setWordSentences();
			setCommonWords();
		}
	}

	/**
	 * Listen for pressing of "CharCount" button.
	 *
	 */

	class charAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			clear();
			setCharSentences();
			setCommonWords();
		}
	}

	
	/**
	 * Listen for pressing of "URL search" button.
	 *
	 */

	class URLAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				
				counter = new SentenceCounter();
				counter.sentenceBuilder("U", URLInput.getText());
				
			} catch (fileNotFoundException e1) {
				
				e1.printStackTrace();
			} catch (PasteInputException e1) {
				
				e1.printStackTrace();
			}
			
			wordButton.setEnabled(true);
			charButton.setEnabled(true);

		}
	}

	/**
	 * Listen for choosing of text word from textWordList.
	 *
	 */

	class chooseTextAction implements ListSelectionListener, KeyListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if(e.getValueIsAdjusting()){
				setSentence();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			setSentence();
		}
	}

	/**
	 * Listen for choosing of common word from commonWordList.
	 *
	 */

	class chooseCommonAction implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {

			if (e.getValueIsAdjusting()) {

				fillSearch();
			}
		}
	}

	/**
	 * Listen for pressing of button "Load file".
	 *
	 */

	class fileAction implements ActionListener {

		private GUI gui;

		public fileAction(GUI gui) {

			this.gui = gui;
		}

		public void actionPerformed(ActionEvent e) {

			int returnVal = fileChooser.showOpenDialog(gui);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();

				sentenceTextArea.setText(stringInfo("Opening: " + file.getName() + ".",
						""));

				counter = new SentenceCounter();

				try {

					counter.sentenceBuilder("F", file.getName());

				} catch (fileNotFoundException e1) {

					sentenceTextArea.append(e1.getMessage());

				} catch (PasteInputException e2) {

					sentenceTextArea.append(e2.getMessage());
				}
				
				wordButton.setEnabled(true);
				charButton.setEnabled(true);

			} else {

				sentenceTextArea.setText(gui.stringInfo(
						"Open command cancelled by user.", ""));
			}
		}
	}

	/**
	 * Listen for pressing of button "Load file".
	 *
	 */

	class pasteAction implements ActionListener, ClipboardOwner {

		@Override
		public void actionPerformed(ActionEvent e) {

			String string = getClipboardContents();

			counter = new SentenceCounter();

			try {

				counter.sentenceBuilder("P", string);

			} catch (fileNotFoundException e1) {

			} catch (PasteInputException e2) {

				sentenceTextArea.setText(stringInfo(e2.getMessage(), ""));

			}

			wordButton.setEnabled(true);
			charButton.setEnabled(true);
		}

		public String getClipboardContents() {

			String result = "";
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();

			Transferable contents = clipboard.getContents(null);
			boolean hasTransferableText = (contents != null)
					&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
			if (hasTransferableText) {
				try {
					result = (String) contents
							.getTransferData(DataFlavor.stringFlavor);

				} catch (UnsupportedFlavorException e1) {

                    e1.printStackTrace();

                }catch(IOException e2){
					e2.printStackTrace();
				}
			}
			return result;
		}

		@Override
		public void lostOwnership(Clipboard clipboard, Transferable contents) {

		}
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				frame = new JFrame("TwitterHelper");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocation(10, 10);
				frame.setMinimumSize(new Dimension(1200, 700));
				frame.setPreferredSize(new Dimension(1200, 700));
				frame.add(new GUI());
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}