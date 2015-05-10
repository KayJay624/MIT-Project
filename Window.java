package badania;

import java.awt.EventQueue;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import java.awt.Color;

import javax.swing.JInternalFrame;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.Caret;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.Box;
import javax.swing.JRadioButton;


public class Window {

	private JFrame frame;
	private JTextArea textArea;
	private JTextField popul;
	private JTextField gener;
	private JTextField mutat;
	
	int pop;
	int gen;
	double mut;
	private JTextField wierz;
	private JTextField kraw;
	int wie, kra, chooseFileReturnVal;
	String path;
	JRadioButton rdbtnGenerujGraf;
	JRadioButton rdbtnWczytajZPliku;
	
	JPanel panel_1;
	JProgressBar progressBar;
	private JTextField textField;
	
	private GenAlgorythm alg;
	private JButton btnZapisz;
	private JButton btnPowieksz;
	private JScrollPane scrollPane_1;

	//---------------------------------------------------------
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Window() {
		initialize();
	}

	public void displayMessage(String s)
	{
		 textArea.append(s+"\n");
		 textArea.repaint();
		 textArea.revalidate();
	}
	
	private void initialize() {
		final JFileChooser fc = new JFileChooser();
		
		frame = new JFrame("Problem maksymalnego zbioru niezale�nego");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JButton btnWskazPlik = new JButton("Wska� plik");
		btnWskazPlik.setBounds(34, 285, 110, 20);
		btnWskazPlik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFileReturnVal = fc.showOpenDialog(frame);
			}
		});
		
		final JButton btnUruchom = new JButton("Uruchom");
		btnUruchom.setBounds(5, 400, 155, 25);
		btnUruchom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//gen to jest warunek stopu, nie zmieniam nazwy zmiennej
				
				alg = new GenAlgorythm();
				try {
					pop = Integer.parseInt(popul.getText());
					if(pop < 1) {
						pop = 1;
					}
					gen = Integer.parseInt(gener.getText());
					mut = Double.parseDouble(mutat.getText());
					if(mut < 0) {
						mut = 0;
					}
					else if(mut > 1) {
						mut = 1;
					}
					progressBar.setValue(0);
					textArea.setText("");
					if(rdbtnGenerujGraf.isSelected()) {		
						try{
							kra = Integer.parseInt(kraw.getText());
							wie = Integer.parseInt(wierz.getText());
							if(wie < 0) {
								wie = 1;
							}
							alg.run(pop, gen, mut, "", wie, kra, textArea, progressBar);
						}
						catch(Exception e) {
							displayMessage("Nale�y wpisa� liczby.");
							e.printStackTrace();;
						}
					} else if(rdbtnWczytajZPliku.isSelected()) {
						try
						{
							if (chooseFileReturnVal == JFileChooser.APPROVE_OPTION) {
					            File file = fc.getSelectedFile();
					            path = file.getPath();
					        } 
							alg.run(pop, gen, mut, path, 0, 0, textArea, progressBar);
						}
						catch(Exception e){
							displayMessage("Problem z plikiem.");
						}
					}
					panel_1.removeAll();
					alg.displayGraph(620, 380, panel_1);
					btnPowieksz.setEnabled(true);
					btnZapisz.setEnabled(true);
					panel_1.repaint();
					
				} 
				catch (Exception e) {
					displayMessage("Nale�y wpisa� liczby.");
				} 				
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnUruchom);
		frame.getContentPane().add(btnWskazPlik);
		btnUruchom.setEnabled(false);
		btnWskazPlik.setEnabled(false);
		
		
		
		progressBar = new JProgressBar(0,100);
		progressBar.setBounds(170, 400, 610, 25);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(5, 430, 775, 125);
		frame.getContentPane().add(scrollPane_1);
		
		textArea = new JTextArea(5,20);
		scrollPane_1.setViewportView(textArea);
		//textArea.setCaret((Caret) new Font("Arial", Font.PLAIN, 12));
		//textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		panel_1 = new JPanel();
		panel_1.setBounds(160, 10, 620, 380);
		frame.getContentPane().add(panel_1);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 150, 380);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		popul = new JTextField();
		popul.setText("5");
		popul.setBounds(0, 20, 100, 20);
		panel.add(popul);
		popul.setColumns(10);
		
		JLabel lblLiczebnoPopulacji = new JLabel("Liczebno\u015B\u0107 populacji");
		lblLiczebnoPopulacji.setBounds(0, 5, 150, 15);
		panel.add(lblLiczebnoPopulacji);
		
		JLabel lblLiczbaGeneracji = new JLabel("Warunek stopu");
		lblLiczbaGeneracji.setBounds(0, 50, 150, 15);
		panel.add(lblLiczbaGeneracji);
		
		gener = new JTextField();
		gener.setText("1");
		gener.setBounds(0, 65, 100, 20);
		panel.add(gener);
		gener.setColumns(10);
		
		JLabel lblMutacja = new JLabel("Mutacja");
		lblMutacja.setBounds(0, 95, 150, 15);
		panel.add(lblMutacja);
		
		mutat = new JTextField();
		mutat.setText("0.1");
		mutat.setBounds(0, 110, 100, 20);
		panel.add(mutat);
		mutat.setColumns(10);
		
		rdbtnGenerujGraf = new JRadioButton("Generuj graf");
		rdbtnGenerujGraf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnGenerujGraf.isSelected()) {						
					wierz.setEnabled(true);
					kraw.setEnabled(true);
					btnWskazPlik.setEnabled(false);
					btnUruchom.setEnabled(true);
				} else {
					wierz.setEnabled(false);
					kraw.setEnabled(false);
				}
			}
		});
		rdbtnGenerujGraf.setBounds(0, 129, 144, 23);
		panel.add(rdbtnGenerujGraf);
		
		rdbtnWczytajZPliku = new JRadioButton("Wczytaj z pliku");
		rdbtnWczytajZPliku.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnWczytajZPliku.isSelected()) {						
					wierz.setEnabled(false);
					kraw.setEnabled(false);
					btnWskazPlik.setEnabled(true);
					btnUruchom.setEnabled(true);
				} else {		
					btnWskazPlik.setEnabled(false);
				}
			}
		});
		rdbtnWczytajZPliku.setBounds(0, 249, 144, 23);
		panel.add(rdbtnWczytajZPliku);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnGenerujGraf);
	    group.add(rdbtnWczytajZPliku);
		
		JLabel lblIlocWierzchokow = new JLabel("Liczba wierzcho�k�w");
		lblIlocWierzchokow.setBounds(10, 160, 150, 15);
		panel.add(lblIlocWierzchokow);
		
		wierz = new JTextField();
		wierz.setBounds(10, 175, 90, 20);
		panel.add(wierz);
		wierz.setColumns(10);
		wierz.setEnabled(false);
		
		JLabel lblIloKrawdzi = new JLabel("Liczba kraw�dzi");
		lblIloKrawdzi.setBounds(10, 200, 134, 14);
		panel.add(lblIloKrawdzi);
		
		kraw = new JTextField();
		kraw.setBounds(10, 215, 90, 20);
		panel.add(kraw);
		kraw.setColumns(10);
		kraw.setEnabled(false);
		
		btnPowieksz = new JButton("Powi�ksz");
		btnPowieksz.setBounds(10, 320, 130, 20);
		panel.add(btnPowieksz);
		btnPowieksz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				alg.displayGraph(1000, 1000, null);
			}
		});
		btnPowieksz.setEnabled(false);
		
		btnZapisz = new JButton("Zapisz");
		btnZapisz.setBounds(10, 350, 130, 20);
		panel.add(btnZapisz);
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				alg.writeToFile();
			}
		});
		btnZapisz.setEnabled(false);
		
		//sciezka = new JTextField();
		//sciezka.setBounds(10, 270, 90, 20);
		//panel.add(sciezka);
		//sciezka.setColumns(10);
		//sciezka.setEnabled(false);		
	}
}