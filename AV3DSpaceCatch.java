/*
 * Proprietário: Antonio Vandré Pedrosa Furtunato Gomes.
 * 
 * Game AV3DSpaceCatch.
 * 
 * Dependências: AntonioVandre >= 20231101.
 * 
 * Motor Gráfico: AV3DLite.
 * 
 * Sugestões ou comunicar erros: "a.vandre.g@gmail.com".
 * 
 * Licença de uso: Atribuição-NãoComercial-CompartilhaIgual (CC BY-NC-SA).
 * 
 * Última atualização: 13-02-2026.
 */

import java.lang.IllegalThreadStateException;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.event.AWTEventListener;
import java.awt.Paint;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.lang.Math;

import java.io.*;

import javax.sound.sampled.*;

import AntonioVandre.*;

public class AV3DSpaceCatch extends JComponent
	{
	public static String ArquivoAV3DSpaceCatchVersao = "AV3DSpaceCatchVersao.txt";

	// Variáveis globais.

	public int TamanhoPlanoX = 400; // Default: 400.
	public int TamanhoPlanoY = 400; // Default: 400.
	public static int MinTamanhoPlanoX = 400; // Default: 400.
	public static int MinTamanhoPlanoY = 400; // Default: 400.
	public static double MargemMaxValue = 1000; // Default: 1000;
	public static double TetaMax = Double.MAX_VALUE; // Opção: Math.PI / 3.
	public static double PhiMax = Double.MAX_VALUE; // Opção: Math.PI / 3.
	public static double MargemAnguloVisao = 0; // Default: 0.
	public double Velocidade = 10; // Default inicial: 10.
	public static double LimiteSuperiorVelocidade = 100; // Default: 100.
	public static double LimiteInferiorVelocidade = 10; // Default: 10.
	public static double IncrementoVelocidade = 10; // Default: 10.
	public static int FramesPorSegundo = 60; // Default: 60.
	public static String AV3DSpaceCatchIconFilePath = "AV3DSpaceCatch - Logo - 200p.png";
	public static int TamanhoEspacoLabelStatus = 280; // Default: 280.
	public static int TamanhoFonteLabelStatus = 11; // Default: 11.
	public static int TamanhoFonteLabelDistancia = 10; // Default: 10.
	public static double DistanciaTela = 3; // Default: 3.
	public static double DeslocamentoLinear = 1; // Default: 1.
	public static double DeslocamentoAngularStatic = 0.08; // Default: 0.08.
	public static double FatorDeslocamentoTrick = 4; // Default: 4.
	public static int TamanhoAlvo = 2; // Default: 2.
	public static int DivisoesAlvo = 1; // Default: 1.
	public int TipoAlvo = 0; // Default: 0.
	public static double FatorArestasNaoOrtogonaisAlvo = 0.3; // Default: 0.3.
	public static double LimiteXalvo = 1000; // Default: 1000.
	public static double LimiteYalvo = 1000; // Default: 1000.
	public static double LimiteZalvo = 1000; // Default: 1000.
	public static double DistanciaCapturaAlvo = 5; // Default: 5.
	public static double AnguloDirecaoIr = Math.PI / 2; // Default: Math.PI / 2.
	public static Color BackgroundCor = Color.BLACK; // Default: Color.BLACK.
	public static Color CorAlvo = Color.WHITE; // Default: Color.WHITE.
	public static Color CorGuias = Color.GREEN; // Default: Color.GREEN.
	public static Color StatusBackgroundCor = new Color(32, 32, 32); // Default: Color(32, 32, 32).
	public static Color StatusTextoCor = Color.WHITE; // Default: Color.WHITE.
	public static Color StatusDistanciaCor = Color.GREEN; // Default: Color.GREEN.
	public static double FatorTonalidadeAproximacao = 30; // Default: 30.
	public static double TricksFactor = 0.2; // Default: 0.2.
	public static String ArquivoSomCatch = "ES_PREL Hit Laser 4 - SFX Producer.wav";
	public static String ArquivoSomBGM10x = "ES_Wind Drone Winter - SFX Producer - 1x.wav";
	public static String ArquivoSomBGM15x = "ES_Wind Drone Winter - SFX Producer - 1.5x.wav";
	public static String ArquivoSomBGM20x = "ES_Wind Drone Winter - SFX Producer - 2x.wav";
	public static String MensagemErroAntonioVandreLib = "Requer AntonioVandre >= 20231101.";

	// Variáveis de funcionamento interno. Evite alterar.

	public double DeslocamentoAngular = DeslocamentoAngularStatic;
	public long ValorInteiroLong;
	public static int CorrecaoX = 10;
	public static int CorrecaoY = 0;
	public double AnguloVisao;
	public int FlagTetaSuperior = 0;
	public int FlagTetaInferior = 0;
	public int FlagPhiSuperior = 0;
	public int FlagPhiInferior = 0;
	public int Sair = 0;
	public long Pontuacao = 0;
	public long TempoR = System.currentTimeMillis();
	public long TempoR2 = System.currentTimeMillis();
	public String Espaco = "";
	public int FlagPausa = 1;
	public int FlagFlashCatch = 0;
	public int FlagCatchSound = 0;
	public int FlagBGM = 0;
	public Clip Catch = null;
	public Clip BGM = null;

	public double x = 50;
	public double y = 50;
	public double z = 50;
	public double Xalvo;
	public double Yalvo;
	public double Zalvo;
	public double Teta = 0;
	public double Phi = 0;
	public double Rot = 0;

	public int FlagArquivo;

	public class GradientLabel extends JLabel
		{
		private Color CorInicial;
		private Color CorFinal;

		public GradientLabel(String Texto)
			{
			super(Texto);

			CorInicial = new Color(0, 100, 0);
			CorFinal = Color.BLACK;
			this.setForeground(Color.WHITE);
			}

		public GradientLabel(String Texto, Color CorInicial, Color CorFinal)
			{
			super(Texto);
			this.CorInicial = CorInicial;
			this.CorFinal = CorFinal;
			this.setForeground(Color.WHITE);
			}

		public GradientLabel(String Texto, Color CorInicial, Color CorFinal, Color CorForeground)
			{
			super(Texto);
			this.CorInicial = CorInicial;
			this.CorFinal = CorFinal;
			this.setForeground(CorForeground);
			}

		public void paint(Graphics g)
			{
			int width = getWidth();
			int height = getHeight();

			GradientPaint paint = new GradientPaint(0, 0, CorInicial, width, height, CorFinal, true);
			Graphics2D g2d = (Graphics2D) g;
			Paint oldPaint = g2d.getPaint();
			g2d.setPaint(paint);
			g2d.fillRect(0, 0, width, height);
			g2d.setPaint(oldPaint);
			super.paint(g);
			}
		}

	private static class LineType extends Object
		{
		final int x1; 
		final int y1;
		final int x2;
		final int y2;
		final Color color;

		public LineType(int x1, int y1, int x2, int y2, Color color)
			{
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
			}			   
		}

	private final LinkedList<LineType> Linhas = new LinkedList<LineType>();
	private final LinkedList<LineType> LinhasGuia = new LinkedList<LineType>();

	public void addLine(int x1, int x2, int x3, int x4, Color color, int n)
		{
		Linhas.add(new LineType(x1, x2, x3, x4, color));

		if (n == Integer.MAX_VALUE) repaint();
		}

	public void addLineG(int x1, int x2, int x3, int x4, Color color, int n)
		{
		LinhasGuia.add(new LineType(x1, x2, x3, x4, color));

		if (n == Integer.MAX_VALUE) repaint();
		}

	public void clearLines()
		{Linhas.clear();}

	public void clearLinesG()
		{LinhasGuia.clear();}

	protected void paintComponent(Graphics g)
		{
		try {
			for (int i = 0; i < Linhas.size(); i++)
				{
				g.setColor(Linhas.get(i).color);
				g.drawLine(Linhas.get(i).x1, Linhas.get(i).y1, Linhas.get(i).x2, Linhas.get(i).y2);
				}

			for (int i = 0; i < LinhasGuia.size(); i++)
				{
				g.setColor(LinhasGuia.get(i).color);
				g.drawLine(LinhasGuia.get(i).x1, LinhasGuia.get(i).y1, LinhasGuia.get(i).x2, LinhasGuia.get(i).y2);
				}
			} catch (Exception e) {}
		}

	public static void main (String[] args) {AV3DSpaceCatch mainc = new AV3DSpaceCatch(); if (args.length == 0) mainc.mainrun(""); else mainc.mainrun(args[0]);}

	public void mainrun (String ArquivoEstatisticas)
		{
		String Versao = "Versão desconhecida.";

		try
			{
			ValorInteiroLong = Long.parseLong(String.valueOf(AntonioVandre.Versao));
			}
		catch (NumberFormatException e)
			{
			System.out.println(MensagemErroAntonioVandreLib);
			return;
			}

		if (AntonioVandre.Versao < 20231101)
			{
			System.out.println(MensagemErroAntonioVandreLib);
			return;
			}

		File fileVersao = new File(ArquivoAV3DSpaceCatchVersao);

		if (! ArquivoEstatisticas.equals(""))
			{Pontuacao = LerEstatisticas (ArquivoEstatisticas); FlagArquivo = 0;}

		if (Pontuacao < 0) Pontuacao = 0;

		try
			{
			BufferedReader brVersao = new BufferedReader(new FileReader(fileVersao));
			Versao = brVersao.readLine();
			} catch (IOException e) {}

		JFrame FameAV3DSpaceCatch = new JFrame("AV3DSpaceCatch - " + Versao);
		FameAV3DSpaceCatch.setIconImage(new ImageIcon(getClass().getResource(AV3DSpaceCatchIconFilePath)).getImage());

		FameAV3DSpaceCatch.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		FameAV3DSpaceCatch.setMinimumSize(new Dimension(MinTamanhoPlanoX, MinTamanhoPlanoY + TamanhoEspacoLabelStatus));
		FameAV3DSpaceCatch.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY + TamanhoEspacoLabelStatus));
		AV3DSpaceCatch comp = new AV3DSpaceCatch();
		comp.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY));
		FameAV3DSpaceCatch.getContentPane().add(comp, BorderLayout.PAGE_START);

		GradientLabel LabelStatus;
		JLabel LabelDistancia = new JLabel("");

		if (FlagPausa == 0)
			LabelStatus = new GradientLabel("<html>&nbsp;Pontuação = " + String.valueOf(Pontuacao) + ".<br><br>&nbsp;Velocidade = " + String.valueOf(Velocidade) + "<br><br>&nbsp;Setas para direcionar.<br><br>&nbsp;\"A\" para aumentar velocidade. \"Z\" para reduzir.<br><br>&nbsp;\"P\" para pausar.<br><br>&nbsp;Barra de espaço para resetar as<br>&nbsp;variáveis de localização.<br><br>&nbsp;ESC para sair.</html>", new Color(0,100,0), Color.BLACK, Color.WHITE);
		else
			LabelStatus = new GradientLabel("<html>&nbsp;Pontuação: " + String.valueOf(Pontuacao) + ".<br><br>&nbsp;Jogo pausado.<br><br>&nbsp;Aperte \"P\" para continuar.</html>", new Color(0,100,0), Color.BLACK, Color.WHITE);

		LabelStatus.setFont(new Font("DialogInput", Font.BOLD | Font.ITALIC, TamanhoFonteLabelStatus));

		LabelDistancia.setFont(new Font("DialogInput", Font.PLAIN, TamanhoFonteLabelDistancia));
		LabelDistancia.setForeground(StatusDistanciaCor);
		LabelDistancia.setBounds(TamanhoPlanoX - 120 - CorrecaoX, TamanhoPlanoY - 30 - CorrecaoY, 120, 30);

		FameAV3DSpaceCatch.add(LabelDistancia);
		FameAV3DSpaceCatch.add(LabelStatus);

		FameAV3DSpaceCatch.getContentPane().setBackground(BackgroundCor);

		FameAV3DSpaceCatch.addKeyListener(new KeyListener()
			{
			public void keyPressed(KeyEvent ke)
				{
				int keyCode = ke.getKeyCode();

				if (keyCode == KeyEvent.VK_ESCAPE)
					Sair = 1;

				if (keyCode == KeyEvent.VK_SPACE)
					{
					x = 50;
					y = 50;
					z = 50;
					Teta = 0;
					Phi = 0;
					Rot = 0;

					FlagPausa = 1;
					}

				if (keyCode == KeyEvent.VK_P)
					{if (FlagPausa == 1) FlagPausa = 0; else {BGM.stop(); BGM.close(); FlagBGM = 0; FlagPausa = 1;}}

				if (keyCode == KeyEvent.VK_A) {if (FlagPausa == 0) 
					if (Velocidade < LimiteSuperiorVelocidade)
						{
						Velocidade += IncrementoVelocidade;

						while (! BGM.isRunning()) try{Thread.sleep(10);} catch (InterruptedException e) {}
						BGM.stop();
						BGM.close();
						FlagBGM = 0;
						}}

				if (keyCode == KeyEvent.VK_Z) {if (FlagPausa == 0) 
					if (Velocidade > LimiteInferiorVelocidade)
						{
						Velocidade -= IncrementoVelocidade;

						try
							{
							while (! BGM.isRunning()) try{Thread.sleep(10);} catch (InterruptedException e) {}
							BGM.stop();
							BGM.close();
							} catch (Exception e) {}
						FlagBGM = 0;
						}}

				if (keyCode == KeyEvent.VK_Q) 
					{TipoAlvo++; TipoAlvo %= 2;}

				if (keyCode == KeyEvent.VK_UP) if (FlagPausa == 0) 
					{if (ke.isShiftDown()) {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular / 10 * Math.sin(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi)) && Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular / 10 * Math.sin(Rot) * Math.sin(Phi)) {Teta += DeslocamentoAngular / 10 * Math.sin(Rot); Phi += DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi + DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi)); Rot += DeslocamentoAngular / 10 * Math.sin(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}} else {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular * Math.sin(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular * Math.cos(Rot) * Math.cos(Phi)) && Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular * Math.sin(Rot) * Math.sin(Phi)) {Teta += DeslocamentoAngular * Math.sin(Rot); Phi += DeslocamentoAngular * Math.cos(Rot) * Math.cos(Phi + DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi)); Rot += DeslocamentoAngular * Math.sin(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}}}

				if (keyCode == KeyEvent.VK_DOWN) if (FlagPausa == 0) 
					{if (ke.isShiftDown()) {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.sin(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi)) && Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.sin(Rot) * Math.sin(Phi)) {Teta -= DeslocamentoAngular / 10 * Math.sin(Rot); Phi -= DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi - DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi)); Rot -= DeslocamentoAngular / 10 * Math.sin(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}} else {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular * Math.sin(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular * Math.cos(Rot) * Math.cos(Phi)) && Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular * Math.sin(Rot) * Math.sin(Phi)) {Teta -= DeslocamentoAngular * Math.sin(Rot); Phi -= DeslocamentoAngular * Math.cos(Rot) * Math.cos(Phi - DeslocamentoAngular / 10 * Math.cos(Rot) * Math.cos(Phi)); Rot -= DeslocamentoAngular * Math.sin(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}}}

				if (keyCode == KeyEvent.VK_LEFT) if (FlagPausa == 0) 
					{if (ke.isShiftDown()) {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular / 10 * Math.cos(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi - DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi))) && (Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.cos(Rot) * Math.sin(Phi))) {Teta += DeslocamentoAngular / 10 * Math.cos(Rot); Phi -= DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi - DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi)); Rot += DeslocamentoAngular / 10 * Math.cos(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}} else {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular * Math.cos(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi - DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi))) && (Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular * Math.cos(Rot) * Math.sin(Phi))) {Teta += DeslocamentoAngular * Math.cos(Rot); Phi -= DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi - DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi)); Rot += DeslocamentoAngular * Math.cos(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}}}

				if (keyCode == KeyEvent.VK_RIGHT) if (FlagPausa == 0) 
					{if (ke.isShiftDown()) {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.cos(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE + MargemMaxValue + DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi)) && (Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular / 10 * Math.cos(Rot) * Math.sin(Phi))) {Teta -= DeslocamentoAngular / 10 * Math.cos(Rot); Phi += DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi + DeslocamentoAngular / 10 * Math.sin(Rot) * Math.cos(Phi)); Rot -= DeslocamentoAngular / 10 * Math.cos(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}} else {if ((Math.abs(Teta) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular * Math.cos(Rot)) && (Math.abs(Phi) < Double.MAX_VALUE - MargemMaxValue + DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi + DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi))) && (Math.abs(Rot) < Double.MAX_VALUE - MargemMaxValue - DeslocamentoAngular * Math.cos(Rot) * Math.sin(Phi))) {Teta -= DeslocamentoAngular * Math.cos(Rot); Phi += DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi + DeslocamentoAngular * Math.sin(Rot) * Math.cos(Phi)); Rot -= DeslocamentoAngular * Math.cos(Rot) * Math.sin(Phi);} else {Teta = 0; Phi = 0; Rot = 0;}}}
				}

			public void keyReleased(KeyEvent ke){}
			public void keyTyped(KeyEvent ke){}
			});

		FameAV3DSpaceCatch.pack();
		FameAV3DSpaceCatch.setVisible(true);

		while (Sair == 0)
			{
			int FlagRedimensionarOver = 0;

			int width = FameAV3DSpaceCatch.getWidth();
			int height = FameAV3DSpaceCatch.getHeight();

			if (FlagRedimensionarOver == 0)
				if ((width != TamanhoPlanoX) || (height != TamanhoPlanoY + TamanhoEspacoLabelStatus))
					{
					TamanhoPlanoX = width;
					TamanhoPlanoY = height - TamanhoEspacoLabelStatus;

					FameAV3DSpaceCatch.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY + TamanhoEspacoLabelStatus));
					comp.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY));
					LabelDistancia.setBounds(TamanhoPlanoX - 120 - CorrecaoX, TamanhoPlanoY - 30 - CorrecaoY, 120, 30);
					FameAV3DSpaceCatch.pack();
					DesenharEspaco(comp);

					FlagPausa = 1;
					}

			LabelDistancia.setText("<html>" + String.valueOf(Math.sqrt(((2 * Xalvo + TamanhoAlvo) / 2 - x) * ((2 * Xalvo + TamanhoAlvo) / 2 - x) + ((2 * Yalvo + TamanhoAlvo) / 2 - y) * ((2 * Yalvo + TamanhoAlvo) / 2 - y) + ((2 * Zalvo + TamanhoAlvo) / 2 - z) * ((2 * Zalvo + TamanhoAlvo) / 2 - z))) + "</html>");

			try {
				if (FlagPausa == 0)
					{
					if (FlagBGM == 0)
						{
						InputStream BGMIS = null;

						if ((Velocidade - LimiteInferiorVelocidade) / (LimiteSuperiorVelocidade - LimiteInferiorVelocidade) < FatorArestasNaoOrtogonaisAlvo)
							BGMIS = getClass().getResourceAsStream(ArquivoSomBGM10x);
						else if ((Velocidade - LimiteInferiorVelocidade) / (LimiteSuperiorVelocidade - LimiteInferiorVelocidade) < 0.6)
							BGMIS = getClass().getResourceAsStream(ArquivoSomBGM15x);
						else
							BGMIS = getClass().getResourceAsStream(ArquivoSomBGM20x);

						InputStream BGMBIS = new BufferedInputStream(BGMIS);
						AudioInputStream BGMAIS = AudioSystem.getAudioInputStream(BGMBIS);
						BGM = AudioSystem.getClip();  
						BGM.open(BGMAIS);
						BGM.loop(Clip.LOOP_CONTINUOUSLY);
						BGM.start();
						FlagBGM = 1;
						}
					}
				else if (FlagBGM == 1)
					{
					BGM.stop();
					BGM.close();
					FlagBGM = 0;
					}
			} catch(Exception ex) {}

			if (FlagPausa == 0) if (Espaco.equals(""))
				{
				do
					{
					Xalvo = (int) (Math.random() * LimiteXalvo * Math.signum(Math.random() - 0.5));

					Yalvo = (int) (Math.random() * LimiteYalvo * Math.signum(Math.random() - 0.5));

					Zalvo = (int) (Math.random() * LimiteZalvo * Math.signum(Math.random() - 0.5));

					} while (Math.sqrt(((2 * Xalvo + TamanhoAlvo) / 2 - x) * ((2 * Xalvo + TamanhoAlvo) / 2 - x) + ((2 * Yalvo + TamanhoAlvo) / 2 - y) * ((2 * Yalvo + TamanhoAlvo) / 2 - y) + ((2 * Zalvo + TamanhoAlvo) / 2 - z) * ((2 * Zalvo + TamanhoAlvo) / 2 - z)) <= DistanciaCapturaAlvo);
				}

			switch (TipoAlvo)
				{
				case 0:
					Espaco = String.valueOf(Xalvo + FatorArestasNaoOrtogonaisAlvo * TamanhoAlvo) + "," + String.valueOf(Yalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo * TamanhoAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo * TamanhoAlvo) + "|" + String.valueOf(Xalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo * TamanhoAlvo) + "," + String.valueOf(Yalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + ";" + String.valueOf(Xalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "|" + String.valueOf(Xalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Yalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "|" + String.valueOf(Xalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Yalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo - FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo) + "," + String.valueOf(Zalvo + FatorArestasNaoOrtogonaisAlvo  * TamanhoAlvo);

					break;
				case 1:
					Espaco = "";

					for (int i = 0; i < DivisoesAlvo; i++)
						{
						Espaco = Espaco + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo) + ";" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo) + "|" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo + i * TamanhoAlvo / DivisoesAlvo) + ";" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "|" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + ";" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + "|" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + ";" + String.valueOf(Xalvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo + i * TamanhoAlvo / DivisoesAlvo) + "|" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo) + "|" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo + i * TamanhoAlvo / DivisoesAlvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "|" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + "|" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + ";" + String.valueOf(Xalvo + TamanhoAlvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo + i * TamanhoAlvo / DivisoesAlvo) + "|" + String.valueOf(Xalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo) + ";" + String.valueOf(Xalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo) + "|" + String.valueOf(Xalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo) + ";" + String.valueOf(Xalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo) + "|" + String.valueOf(Xalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + ";" + String.valueOf(Xalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo + TamanhoAlvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + "|" + String.valueOf(Xalvo + i * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo + TamanhoAlvo) + ";" + String.valueOf(Xalvo + (i + 1) * TamanhoAlvo / DivisoesAlvo) + "," + String.valueOf(Yalvo) + "," + String.valueOf(Zalvo + TamanhoAlvo);

						if (i < DivisoesAlvo - 1) Espaco = Espaco + "|";
						}

					break;
				default:
				}

			long Tempo = System.currentTimeMillis();

			if (FlagPausa == 0) if (Tempo - TempoR > (int) (1000 / (FramesPorSegundo)))
				{
				x += Velocidade * Math.cos(Phi) * Math.cos(Teta) / FramesPorSegundo;

				y -= Velocidade * Math.cos(Phi) * Math.sin(Teta) / FramesPorSegundo;

				z -= Velocidade * Math.sin(Phi) / FramesPorSegundo;

				TempoR = Tempo;
				}

			long Tempo2 = System.currentTimeMillis();

			if (FlagFlashCatch == 1) if ((Tempo2 - TempoR2) > 100) FlagFlashCatch = 0;

			if (FlagPausa == 0)
				{
				double FatorCor = Math.pow(Math.min(Math.sqrt(((2 * Xalvo + TamanhoAlvo) / 2 - x) * ((2 * Xalvo + TamanhoAlvo) / 2 - x) + ((2 * Yalvo + TamanhoAlvo) / 2 - y) * ((2 * Yalvo + TamanhoAlvo) / 2 - y) + ((2 * Zalvo + TamanhoAlvo) / 2 - z) * ((2 * Zalvo + TamanhoAlvo) / 2 - z)), (2 * Math.sqrt(3) * Math.max(LimiteXalvo, Math.max(LimiteYalvo, LimiteZalvo)))) / (2 * Math.sqrt(3) * Math.max(LimiteXalvo, Math.max(LimiteYalvo, LimiteZalvo))), 1 / FatorTonalidadeAproximacao);

				if (FlagPausa == 0) if (FlagFlashCatch == 0) FameAV3DSpaceCatch.getContentPane().setBackground(new Color((int) (64 - 64 * FatorCor), (int) (255 - 255 * FatorCor), (int) (64 - 64 * FatorCor)));
				}

			if (FlagPausa == 0)
				AnguloVisao = Math.atan(Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 / DistanciaTela);

			if (FlagPausa == 0) if (Math.sqrt(((2 * Xalvo + TamanhoAlvo) / 2 - x) * ((2 * Xalvo + TamanhoAlvo) / 2 - x) + ((2 * Yalvo + TamanhoAlvo) / 2 - y) * ((2 * Yalvo + TamanhoAlvo) / 2 - y) + ((2 * Zalvo + TamanhoAlvo) / 2 - z) * ((2 * Zalvo + TamanhoAlvo) / 2 - z)) <= DistanciaCapturaAlvo)
				{
				try
					{
					(new Thread () {public void run ()
						{
						try {
							InputStream CatchIS = getClass().getResourceAsStream(ArquivoSomCatch);
							InputStream CatchBIS = new BufferedInputStream(CatchIS);
							AudioInputStream CatchAIS = AudioSystem.getAudioInputStream(CatchBIS);
							Catch = AudioSystem.getClip();  
							Catch.open(CatchAIS);
							Catch.start();

							FlagCatchSound = 1;
						} catch(Exception ex) {}
					}}).start();} catch (IllegalThreadStateException e) {}

				FlagBGM = 0;

				Pontuacao++;
				Espaco = "";
				FlagFlashCatch = 1;
				FameAV3DSpaceCatch.getContentPane().setBackground(new Color(0, 255, 0));
				TempoR2 = System.currentTimeMillis();

				if (FlagArquivo == 0)
					EscreverEstatisticas(ArquivoEstatisticas, Pontuacao);

				System.out.println("Pontuação: " + String.valueOf(Pontuacao) + ".\n");
				}

			if (FlagPausa == 0)
				LabelStatus.setText("<html>&nbsp;Pontuação = " + String.valueOf(Pontuacao) + ".<br><br>&nbsp;Velocidade = " + String.valueOf(Velocidade) + "<br><br>&nbsp;Setas para direcionar.<br><br>&nbsp;\"A\" para aumentar velocidade. \"Z\" para reduzir.<br><br>&nbsp;\"P\" para pausar.<br><br>&nbsp;Barra de espaço para resetar as<br>&nbsp;variáveis de localização.<br><br>&nbsp;ESC para sair.</html>");
			else
				LabelStatus.setText("<html>&nbsp;Pontuação: " + String.valueOf(Pontuacao) + ".<br><br>&nbsp;Jogo pausado.<br><br>&nbsp;Aperte \"P\" para continuar.</html>");

			if ((Math.abs(x) > Double.MAX_VALUE - MargemMaxValue) || (Math.abs(y) > Double.MAX_VALUE - MargemMaxValue) || (Math.abs(z) > Double.MAX_VALUE - MargemMaxValue)) {x = 50; y = 50; z = 50;}

			if (FlagPausa == 0) DesenharEspaco(comp);

			// Tricks scope.

			if ((Math.abs(Math.sin(Rot)) < TricksFactor / 2) || (Math.abs(Math.cos(Rot)) < TricksFactor / 2)) Rot += (Math.cos(Phi) >= 0 ? 1 : -1) * TricksFactor / 10 * DeslocamentoAngular;

			DeslocamentoAngular = DeslocamentoAngularStatic / Math.max(Math.abs(Math.cos(Phi)), TricksFactor);


			if (FlagCatchSound == 1)
				{try {Thread.sleep(100);} catch(InterruptedException e){}; Catch.close(); FlagCatchSound = 0;}
			else
				try {Thread.sleep(10);} catch(InterruptedException e) {}
			}

		System.exit(0);
		}

	public void DesenharEspaco(AV3DSpaceCatch comp)
		{
		String [] EspacoStr2 = Espaco.split("@");

		String [] EspacoLinhas = EspacoStr2[0].split("\\|");

		comp.clearLines();
		comp.clearLinesG();

		for (int i = 0; i < EspacoLinhas.length; i++)
			{
			String [] Pontos = EspacoLinhas[i].split(";");

			if (Pontos.length == 2)
				{
				String [] CoordenadasOrig = Pontos[0].split(",");
				String [] CoordenadasDest = Pontos[1].split(",");

				if (FlagPhiSuperior == 1)
					{
					comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 30 - CorrecaoX, 30 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 30 - CorrecaoX, 30 - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);

					FlagPhiSuperior = 0;
					}

				if (FlagPhiInferior == 1)
					{
					comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 30 - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 30 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 30 - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 30 - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);

					FlagPhiInferior = 0;
					}

				double xo = Double.parseDouble(CoordenadasOrig[0]) - x;

				double xd = Double.parseDouble(CoordenadasDest[0]) - x;

				double yo = Double.parseDouble(CoordenadasOrig[1]) - y;

				double yd = Double.parseDouble(CoordenadasDest[1]) - y;

				double zo = Double.parseDouble(CoordenadasOrig[2]) - z;

				double zd = Double.parseDouble(CoordenadasDest[2]) - z;

				int xi;
				int yi;
				int xf;
				int yf;

				double xit = (xo * Math.sin(Teta) + yo * Math.cos(Teta)) * Math.cos(Rot) - (xo * Math.cos(Teta) * Math.sin(Phi) - yo * Math.sin(Teta) * Math.sin(Phi) + zo * Math.cos(Phi)) * Math.sin(Rot);

				double yit = (xo * Math.sin(Teta) + yo * Math.cos(Teta)) * Math.sin(Rot) + (xo * Math.cos(Teta) * Math.sin(Phi) - yo * Math.sin(Teta) * Math.sin(Phi) + zo * Math.cos(Phi)) * Math.cos(Rot);

				double xft = (xd * Math.sin(Teta) + yd * Math.cos(Teta)) * Math.cos(Rot) - (xd * Math.cos(Teta) * Math.sin(Phi) - yd * Math.sin(Teta) * Math.sin(Phi) + zd * Math.cos(Phi)) * Math.sin(Rot);

				double yft = (xd * Math.sin(Teta) + yd * Math.cos(Teta)) * Math.sin(Rot) + (xd * Math.cos(Teta) * Math.sin(Phi) - yd * Math.sin(Teta) * Math.sin(Phi) + zd * Math.cos(Phi)) * Math.cos(Rot);

				double di = Math.sqrt(xo * xo + yo * yo + zo * zo);

				double df = Math.sqrt(xd * xd + yd * yd + zd * zd);

				double ProdutoEscalaro = xo * Math.cos(Teta) * Math.cos(Phi) - yo * Math.sin(Teta) * Math.cos(Phi) - zo * Math.sin(Phi);

				double ProdutoEscalard = xd * Math.cos(Teta) * Math.cos(Phi) - yd * Math.sin(Teta) * Math.cos(Phi) - zd * Math.sin(Phi);

				double ProdutoEscalarXo = xo * Math.cos(Teta) * Math.cos(Phi) - yo * Math.sin(Teta) * Math.cos(Phi);

				double ProdutoEscalarXd = xd * Math.cos(Teta) * Math.cos(Phi) - yd * Math.sin(Teta) * Math.cos(Phi);

				double ProdutoEscalarZo = xo * Math.cos(Teta) * Math.cos(Phi) - zo * Math.cos(Teta) * Math.cos(Phi) * Math.sin(Phi);

				double ProdutoEscalarZd = xd * Math.cos(Teta) * Math.cos(Phi) - zd * Math.cos(Teta) * Math.cos(Phi) * Math.sin(Phi);

				xi = (int) (TamanhoPlanoX / 2 + TamanhoPlanoX / 2 * DistanciaTela * xit / di) - CorrecaoX;

				yi = (int) (TamanhoPlanoY / 2 + TamanhoPlanoY / 2 * DistanciaTela * yit / di) - CorrecaoY;

				xf = (int) (TamanhoPlanoX / 2 + TamanhoPlanoX / 2 * DistanciaTela * xft / df) - CorrecaoX;

				yf = (int) (TamanhoPlanoY / 2 + TamanhoPlanoY / 2 * DistanciaTela * yft / df) - CorrecaoY;

				if ((Math.abs(Math.acos(ProdutoEscalaro / di)) < AnguloVisao + MargemAnguloVisao) && (Math.abs(Math.acos(ProdutoEscalard / df)) < AnguloVisao + MargemAnguloVisao))
					comp.addLine(xi, yi, xf, yf, CorAlvo, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);

				if ((Math.acos(ProdutoEscalarXo / Math.sqrt(xo * xo + yo * yo)) * Math.abs(Math.cos(Rot)) <= AnguloDirecaoIr) && (Math.acos(ProdutoEscalarXd / Math.sqrt(xd * xd + yd * yd)) * Math.abs(Math.cos(Rot)) <= AnguloDirecaoIr))
					{
					if (Math.min(xi - CorrecaoX, xf - CorrecaoX) < 0)
						{
						comp.addLineG(50 - CorrecaoX, (int) (TamanhoPlanoY / 2) - 20 - CorrecaoY, 40 - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i);

						comp.addLineG(50 - CorrecaoX, (int) (TamanhoPlanoY / 2) + 20 - CorrecaoY, 40 - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);
						}
		
					if (Math.max(xi - CorrecaoX, xf - CorrecaoX) > TamanhoPlanoX)
						{
						comp.addLineG(TamanhoPlanoX - 50 - CorrecaoX, (int) (TamanhoPlanoY / 2) - 20 - CorrecaoY, TamanhoPlanoX - 40  - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i);

						comp.addLineG(TamanhoPlanoX - 50 - CorrecaoX, (int) (TamanhoPlanoY / 2) + 20 - CorrecaoY, TamanhoPlanoX - 40  - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);
						}
					}
				else if ((Math.acos(ProdutoEscalarXo / Math.sqrt(xo * xo + yo * yo)) * Math.cos(Rot) > AnguloDirecaoIr) || (Math.acos(ProdutoEscalarXd / Math.sqrt(xd * xd + yd * yd)) * Math.cos(Rot) > AnguloDirecaoIr))
					{
					comp.addLineG(50 - CorrecaoX, (int) (TamanhoPlanoY / 2) - 20 - CorrecaoY, 40 - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i);

					comp.addLineG(50 - CorrecaoX, (int) (TamanhoPlanoY / 2) + 20 - CorrecaoY, 40 - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i);

					comp.addLineG(TamanhoPlanoX - 50 - CorrecaoX, (int) (TamanhoPlanoY / 2) - 20 - CorrecaoY, TamanhoPlanoX - 40  - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i);

					comp.addLineG(TamanhoPlanoX - 50 - CorrecaoX, (int) (TamanhoPlanoY / 2) + 20 - CorrecaoY, TamanhoPlanoX - 40  - CorrecaoX, (int) (TamanhoPlanoY / 2) - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);
					}

				if ((Math.acos(ProdutoEscalarZo / Math.sqrt(xo * xo + Math.cos(Teta) * Math.cos(Phi) * zo * Math.cos(Teta) * Math.cos(Phi) * zo)) * Math.abs(Math.cos(Rot)) <= AnguloDirecaoIr) && (Math.acos(ProdutoEscalarZd / Math.sqrt(xd * xd + zd * zd)) * Math.abs(Math.cos(Rot)) <= AnguloDirecaoIr))
					{
					if (Math.max(yi - CorrecaoY, yf - CorrecaoY) > Math.min(TamanhoPlanoX, TamanhoPlanoY))
						{
						comp.addLineG((int) (TamanhoPlanoX / 2) - 20 - CorrecaoX, TamanhoPlanoY - 50 - CorrecaoY, (int) (TamanhoPlanoX / 2) - CorrecaoX, TamanhoPlanoY - 40 - CorrecaoY, CorGuias, i);

						comp.addLineG((int) (TamanhoPlanoX / 2) - CorrecaoX, TamanhoPlanoY - 40 - CorrecaoY, (int) (TamanhoPlanoX / 2) + 20 - CorrecaoX, TamanhoPlanoY - 50 - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);
						}
		
					if (Math.min(yi - CorrecaoY, yf - CorrecaoY) < 0)
						{
						comp.addLineG((int) (TamanhoPlanoX / 2) - 20 - CorrecaoX, 50 - CorrecaoY, (int) (TamanhoPlanoX / 2) - CorrecaoX, 40 - CorrecaoY, CorGuias, i);

						comp.addLineG((int) (TamanhoPlanoX / 2) - CorrecaoX, 40 - CorrecaoY, (int) (TamanhoPlanoX / 2) + 20 - CorrecaoX, 50 - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);
						}
					}
				else if ((Math.acos(ProdutoEscalarZo / Math.sqrt(xo * xo + Math.cos(Teta) * Math.cos(Phi) * zo *  Math.cos(Teta) * Math.cos(Phi) * zo)) * Math.cos(Rot) > AnguloDirecaoIr) || (Math.acos(ProdutoEscalarZd / Math.sqrt(xd * xd + zd * zd)) * Math.cos(Rot) > AnguloDirecaoIr))
					{
					comp.addLineG((int) (TamanhoPlanoX / 2) - 20 - CorrecaoX, TamanhoPlanoY - 50 - CorrecaoY, (int) (TamanhoPlanoX / 2) - CorrecaoX, TamanhoPlanoY - 40 - CorrecaoY, CorGuias, i);

					comp.addLineG((int) (TamanhoPlanoX / 2) - CorrecaoX, TamanhoPlanoY - 40 - CorrecaoY, (int) (TamanhoPlanoX / 2) + 20 - CorrecaoX, TamanhoPlanoY - 50 - CorrecaoY, CorGuias, i);

					comp.addLineG((int) (TamanhoPlanoX / 2) - 20 - CorrecaoX, 50 - CorrecaoY, (int) (TamanhoPlanoX / 2) - CorrecaoX, 40 - CorrecaoY, CorGuias, i);

					comp.addLineG((int) (TamanhoPlanoX / 2) - CorrecaoX, 40 - CorrecaoY, (int) (TamanhoPlanoX / 2) + 20 - CorrecaoX, 50 - CorrecaoY, CorGuias, i == EspacoLinhas.length - 1 ? Integer.MAX_VALUE : i);
					}
				}
			}
		}

	public long LerEstatisticas(String ArquivoEstatisticasArg)
		{
		File file = new File(ArquivoEstatisticasArg);

		try
			{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String PontuacaoStr = "";
			PontuacaoStr = br.readLine();
			if (AntonioVandre.NumeroNaturalLong(PontuacaoStr))
				return Long.valueOf(PontuacaoStr);
			else
				return -1;
			} catch (IOException e) {return -1;}
		}

	public void EscreverEstatisticas (String ArquivoEstatisticasArg, long PontuacaoArg)
		{
		try
			{
			BufferedWriter writer = new BufferedWriter(new FileWriter(ArquivoEstatisticasArg));
			writer.write(String.valueOf(PontuacaoArg));
			writer.close();
			}
		catch (IOException e) {}
		}
	}
